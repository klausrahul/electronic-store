package com.rahul.electronic.store.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahul.electronic.store.dto.PageableResponse;
import com.rahul.electronic.store.dto.UserDto;
import com.rahul.electronic.store.entity.Role;
import com.rahul.electronic.store.entity.User;
import com.rahul.electronic.store.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

	@MockBean
	private UserService userService;

	private Role role;
	private User user;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private MockMvc mockMvc;
	
	
	@BeforeEach
	public void init() {

		role = Role.builder().roleId("abc").roleName("NORMAL").build();

		user = User.builder().name("Rahul").email("rahul@dev.in").about("This is testing user").gender("Male")
				.imageName("abc.png").password("abcd").roles(Set.of(role)).build();

	//	roleId = "abc";
	}

	@Test
	public void createUserTest() throws Exception {
		UserDto dto=mapper.map(user, UserDto.class);
		Mockito.when(userService.createUser(Mockito.any())).thenReturn(dto);
		
		//actual request for url
		
		mockMvc.perform(
				MockMvcRequestBuilders.post("/user/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(convertObjectToJsonString(user))
				.accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").exists());

	}

	
	@Test
	public void updateUserTest() throws Exception {
		String userId="123";
		UserDto dto=mapper.map(user, UserDto.class);
		Mockito.when(userService.updateUser(Mockito.any(), Mockito.anyString())).thenReturn(dto);
		
		mockMvc.perform(MockMvcRequestBuilders.put("/user/update/" + userId).
				header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyYWpwYWxAZGV2LmluIiwiaWF0IjoxNjk5NDQ1ODI1LCJleHAiOjE2OTk0NjM4MjV9.hqKALqHiLujSUapq6GmWhjL4Smi6su0w5CWpug_cKXVr0-vl2rPVj_CE-qRuGxmeQ6hHJPd3iIy6jfGc4XeQHQ")
				.contentType(MediaType.APPLICATION_JSON)
				.content(convertObjectToJsonString(user)).accept(MediaType.APPLICATION_JSON))
		.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.name").exists());
		
		
	}
	
	
	
	
	private String convertObjectToJsonString(Object user) {
		try {
			return new ObjectMapper().writeValueAsString(user);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	//get all controller
	@Test
	 public void getAllUsers() throws Exception {
		 
		UserDto userDto = UserDto.builder().name("Rahul Mittal").email("rahul@gma.com").password("abc").about("This is testing new user").gender("Male")
				.imageName("xyz.png").build();
		UserDto userDto1 = UserDto.builder().name("XYX Mittal").email("XYX@gma.com").password("abc").about("This is testing new user").gender("Male")
				.imageName("xyz.png").build();
		
		UserDto userDto2 = UserDto.builder().name("abc Mittal").email("abc@gma.com").password("abc").about("This is testing new user").gender("Male")
				.imageName("xyz.png").build();
		
		
		
		PageableResponse<UserDto> pageableResponse=new PageableResponse<>();
		pageableResponse.setContent(Arrays.asList(userDto,userDto1,userDto2));
		pageableResponse.setLastPage(false);
		pageableResponse.setPageNumber(100);
		pageableResponse.setPageSize(10);
		pageableResponse.setTotalElements(1000);
		pageableResponse.setTotalPages(10);
		
		Mockito.when(userService.getAllUser(0,10,"name","asc")).thenReturn(pageableResponse);
		
		
		mockMvc.perform(MockMvcRequestBuilders.get("/user/getall").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andDo(print()).andExpect(status().isOk());
	 }
}
