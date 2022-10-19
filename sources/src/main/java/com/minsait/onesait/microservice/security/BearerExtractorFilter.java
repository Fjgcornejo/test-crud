package com.minsait.onesait.microservice.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BearerExtractorFilter implements Filter {

	private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";
	@Autowired
	private RemoteTokenServices remoteTokenServices;

	private final TokenExtractor tokenExtractor = new BearerTokenExtractor();

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (((HttpServletRequest) request).getHeader(HttpHeaders.AUTHORIZATION) != null) {
			final Authentication auth = tokenExtractor.extract((HttpServletRequest) request);
			if (auth != null && auth instanceof PreAuthenticatedAuthenticationToken) {
				final OAuth2Authentication oauth = remoteTokenServices.loadAuthentication((String) auth.getPrincipal());
				final SecurityContext securityContext = SecurityContextHolder.getContext();
				securityContext.setAuthentication(oauth);
				((HttpServletRequest) request).getSession(true).setAttribute(SPRING_SECURITY_CONTEXT, securityContext);
				chain.doFilter(request, response);
				((HttpServletRequest) request).getSession().removeAttribute(SPRING_SECURITY_CONTEXT);
			}
		}
		chain.doFilter(request, response);
	}

}
