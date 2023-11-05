package com.rahul.electronic.store.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.rahul.electronic.store.security.JWTAuthenticationEntryPoint;
import com.rahul.electronic.store.security.JWTAuthenticationFilter;

//earlier  extends WebMvcConfigurerAdapter
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JWTAuthenticationEntryPoint authenticationEntryPoint;
	@Autowired
	private JWTAuthenticationFilter authenticationFilter;
	
	private final String[] PUBLIC_URLS= {
			"/swagger-ui/**","/webjars/**", "/swagger-resources/**","/v3/api-docs","/v2/api-docs"
	};

	// Hardcode Usernamw and password implementation
	/*
	 * @Bean public UserDetailsService userDetailsService() {
	 * 
	 * UserDetails
	 * normal=User.builder().username("Rahul").password(passwordEncoder().encode(
	 * "rahul")).roles("NORMAL").build();
	 * 
	 * UserDetails
	 * admin=User.builder().username("ajay").password(passwordEncoder().encode(
	 * "ajay")).roles("ADMIN").build();
	 * 
	 * 
	 * //Users Create
	 * 
	 * return new InMemoryUserDetailsManager(normal,admin); }
	 */

	// filter for accessing the urls
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		// Basic authentication like sending username password in headers
//		http.csrf().disable().cors().disable().authorizeRequests().anyRequest().authenticated().and().httpBasic();

		// JWT

		http.csrf().disable().cors().disable().authorizeRequests().antMatchers("/auth/login").permitAll()
				.antMatchers(HttpMethod.POST, "/user/create").permitAll().
				antMatchers(HttpMethod.DELETE,"/user/**").hasRole("ADMIN").
				antMatchers(PUBLIC_URLS).permitAll().

				anyRequest().authenticated().and().exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	// Loin By DB implementation
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
		return builder.getAuthenticationManager();
	}
}
