package com.rahul.electronic.store.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//earlier  extends WebMvcConfigurerAdapter
@Configuration
public class SecurityConfig {

	@Autowired
	private UserDetailsService userDetailsService;

	
	//Hardcode Usernamw and password implementation
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
	
	//filter for accessing the urls
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		//Basic authentication like sending username password in headers
		
		http.csrf().disable().cors().disable().authorizeRequests().anyRequest().authenticated().and().httpBasic();
		return http.build();
	}

	
	//Loin By DB implementation
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
}
