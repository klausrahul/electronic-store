package com.rahul.electronic.store.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

/**
 * @author RAHUL MITTAL For any upcoming request this class get executed and it
 *         will check the JWT token wether it is valid or not if valid it will
 *         set authentication in context to specify that current user is
 *         authenticate.
 *
 */
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

	private Logger log = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
	@Autowired
	private JWTHelper jwtHelper;
	@Autowired
	private UserDetailsService detailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String requestHeader = request.getHeader("Authorization");
		log.info("Header {}", requestHeader);

		String username = null;
		String token = null;

		if (requestHeader != null && requestHeader.startsWith("Bearer")) {
			token = requestHeader.substring(7);

			try {
				username = jwtHelper.getUsernameFromToken(token);
			} catch (IllegalArgumentException e) {
				log.info("Illegal Argument while fetching the username !!");
				e.printStackTrace();
			} catch (ExpiredJwtException e) {
				log.info("Given jwt token is expired !!");
				e.printStackTrace();
			} catch (MalformedJwtException e) {
				log.info("Some changes has done in tolen  !! invalid token");
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			log.info("Invalid Header Value !!");
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetail = detailsService.loadUserByUsername(username);
			Boolean validateToken = jwtHelper.validateToken(token, userDetail);

			if (validateToken) {

				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail,
						null, userDetail.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else {
				log.info("Validation Fails !!");
			}

		}

		filterChain.doFilter(request, response);
	}

}
