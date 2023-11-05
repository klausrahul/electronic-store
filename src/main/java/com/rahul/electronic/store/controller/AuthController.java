package com.rahul.electronic.store.controller;

import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rahul.electronic.store.dto.JWTRequest;
import com.rahul.electronic.store.dto.JWTResponse;
import com.rahul.electronic.store.dto.UserDto;
import com.rahul.electronic.store.exception.BadApiRequestException;
import com.rahul.electronic.store.security.JWTHelper;
import com.rahul.electronic.store.service.UserService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/auth")
@Api(value = "AuthController",description = "This is AUTH Related APIS")
public class AuthController {

	@Autowired
	private UserDetailsService detailsService;
	@Autowired
	ModelMapper mapper;
	@Autowired
	private AuthenticationManager manager;
	@Autowired
	private UserService service;
	
	@Autowired
	private JWTHelper jwtHelper;

	@PostMapping("/login")
	public ResponseEntity<JWTResponse> login(@RequestBody JWTRequest request) {
		this.doAuthticate(request.getEmail(), request.getPassword());
		UserDetails userDetail=detailsService.loadUserByUsername(request.getEmail());
		String token=jwtHelper.generateToken(userDetail);
		
		UserDto dto=mapper.map(userDetail, UserDto.class);
		
		JWTResponse response=JWTResponse.builder().token(token).user(dto).build();
		return new ResponseEntity<JWTResponse>(response,HttpStatus.OK);
	}

	private void doAuthticate(String email, String password) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email,
				password);

		try {
			manager.authenticate(authenticationToken);
		} catch (BadCredentialsException exception) {
			throw new BadApiRequestException("Invalid Username and password !!");

		}
	}

	@GetMapping("/current")
	public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
		String name = principal.getName();
		return new ResponseEntity<UserDto>(mapper.map(detailsService.loadUserByUsername(name), UserDto.class),
				HttpStatus.OK);
	}

}
