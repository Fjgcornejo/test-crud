package com.minsait.onesait.microservice.security;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableOAuth2Client
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private OAuth2ClientContext oauth2ClientContext;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// http.formLogin().loginPage("/login.html").loginProcessingUrl("/login").permitAll();

		http.csrf().disable();
		http.logout();
		http.authorizeRequests().antMatchers("/login**", "/**/*.css", "/img/**", "/third-party/**", "/").permitAll();
		http.authorizeRequests().antMatchers("/health/", "/info", "/metrics", "/trace", "/logfile","/actuator/**").permitAll();
		http.authorizeRequests().antMatchers("/**")
				.permitAll();
		http.authorizeRequests().regexMatchers("^/swagger.*", "^/v2/api-docs.*").permitAll();
		http.headers().frameOptions().disable();
		http.exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/"));
		http.addFilterAfter(ssoFilter(), BasicAuthenticationFilter.class);

	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**", "/webjars/**");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("admin").password(passwordEncoder().encode("admin"))
				.roles("ADMINISTRATOR");
		auth.inMemoryAuthentication().withUser("developer").password(passwordEncoder().encode("developer"))
				.roles("ADMINISTRATOR");
		auth.inMemoryAuthentication().withUser("user").password(passwordEncoder().encode("user")).roles("USER");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	private Filter ssoFilter() {
		final OAuth2ClientAuthenticationProcessingFilter oauthFilter = new OAuth2ClientAuthenticationProcessingFilter(
				"/login/oauth");
		final OAuth2RestTemplate oauthTemplate = new OAuth2RestTemplate(getClient(), oauth2ClientContext);
		oauthFilter.setRestTemplate(oauthTemplate);
		oauthFilter
				.setTokenServices(new UserInfoTokenServices(getResource().getUserInfoUri(), getClient().getClientId()));
		oauthFilter.setAllowSessionCreation(true);
		oauthFilter.setAuthenticationSuccessHandler(new AuthenticationHandler());
		return oauthFilter;
	}

	@Bean
	public FilterRegistrationBean<OAuth2ClientContextFilter> oauth2ClientFilterRegistration(
			OAuth2ClientContextFilter filter) {
		final FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(filter);
		registration.setOrder(-100);
		return registration;
	}

	@Bean
	@ConfigurationProperties("security.oauth2.client")
	public AuthorizationCodeResourceDetails getClient() {
		return new AuthorizationCodeResourceDetails();
	}

	@Bean
	@ConfigurationProperties("security.oauth2.resource")
	@Primary
	public ResourceServerProperties getResource() {
		return new ResourceServerProperties();
	}

	@Bean
	@ConfigurationProperties("security.oauth2.client")
	public RemoteTokenServices remoteTokenService() {
		return new RemoteTokenServices();
	}

}
