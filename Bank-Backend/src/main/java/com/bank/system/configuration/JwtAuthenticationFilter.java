package com.bank.system.configuration;

import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bank.system.service.user.UserService;
import com.bank.system.utils.JWTUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JWTUtil jwtHelper;
	private final UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");
		final String userEmail;
		final String jwt;

		if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		// Extract the token from the Authorization header
		jwt = authHeader.substring(7);
		userEmail = jwtHelper.extractUserName(jwt);

		if (StringUtils.hasText(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
			// Load user details by username (email)
			UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userEmail);

			if (jwtHelper.isTokenValid(jwt, userDetails)) {
				jwtHelper.extractAllClaims(jwt);

				// Create authentication token with user details
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				// Set the authentication in the security context
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}

		// Continue with the filter chain
		filterChain.doFilter(request, response);
	}
}
