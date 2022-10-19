package com.minsait.onesait.microservice.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class AuthenticationHandler implements AuthenticationSuccessHandler {
	private final String BLOCK_PRIOR_LOGIN = "block_prior_login";
	private final String URI_MAIN = "/user/info";

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {

		final HttpSession session = request.getSession();
		if (session != null) {
			final String redirectUrl = (String) session.getAttribute(BLOCK_PRIOR_LOGIN);
			if (redirectUrl != null) {
				// we do not forget to clean this attribute from session
				session.removeAttribute(BLOCK_PRIOR_LOGIN);
				// then we redirect
				response.sendRedirect(redirectUrl);
			} else {
				response.sendRedirect(request.getContextPath() + URI_MAIN);
			}

		} else {
			response.sendRedirect(request.getContextPath() + URI_MAIN);
		}

	}

}
