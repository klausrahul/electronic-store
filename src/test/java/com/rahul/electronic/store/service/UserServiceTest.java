package com.rahul.electronic.store.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.rahul.electronic.store.dto.PageableResponse;
import com.rahul.electronic.store.dto.UserDto;
import com.rahul.electronic.store.entity.Role;
import com.rahul.electronic.store.entity.User;
import com.rahul.electronic.store.repo.RoleRepo;
import com.rahul.electronic.store.repo.UserRepo;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserServiceTest {

	@MockBean
	private UserRepo userRepo;

	@MockBean
	private RoleRepo roleRepo;

	@Autowired
	// @InjectMocks
	UserService userService;

	User user;
	Role role;

	String roleId;

	@Autowired
	private ModelMapper mapper;

	@BeforeEach
	public void init() {

		role = Role.builder().roleId("abc").roleName("NORMAL").build();

		user = User.builder().name("Rahul").email("rahul@dev.in").about("This is testing user").gender("Male")
				.imageName("abc.png").password("abcd").roles(Set.of(role)).build();

		roleId = "abc";
	}

	@Test
	public void createUserTest() {

		Mockito.when(userRepo.save(Mockito.any())).thenReturn(user);

		Mockito.when(roleRepo.findById(Mockito.anyString())).thenReturn(Optional.of(role));

		UserDto dto = userService.createUser(mapper.map(user, UserDto.class));

		System.out.println(dto.getName());

		Assertions.assertNotNull(dto);

		Assertions.assertEquals("Rahul", dto.getName());

	}

	@Test
	public void updateUserTest() {

		String userId = "sfcsfsfsf";

		UserDto userDto = UserDto.builder().name("Rahul Mittal").about("This is testing new user").gender("Male")
				.imageName("xyz.png").build(); // expected

		Mockito.when(userRepo.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		Mockito.when(userRepo.save(Mockito.any())).thenReturn(user);

		UserDto updatedUser = userService.updateUser(userDto, userId); // actual
		// UserDto updatedUser = mapper.map(user, UserDto.class);// actual
		System.out.println("updateUserTest :" + updatedUser.getName());

		Assertions.assertNotNull(updatedUser);

		Assertions.assertEquals(userDto.getName(), updatedUser.getName(), "Name is not Matching !!");

	}

	@Test
	public void deleteUserTest() {
		String userId = "sfcsfsfsf";
		Mockito.when(userRepo.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		userService.deleteUser(userId);
		Mockito.verify(userRepo, Mockito.times(1)).delete(user);
	}

	@Test
	public void getAllUser() {
		User user1 = User.builder().name("Ankit").email("Ankit@dev.in").about("This is testing Ankit user").gender("Male")
				.imageName("abc.png").password("abcd").roles(Set.of(role)).build();
		User user2 = User.builder().name("SRK").email("SRK@dev.in").about("This is testing SRK user").gender("Male")
				.imageName("abc.png").password("abcd").roles(Set.of(role)).build();
		
		List<User> userList=Arrays.asList(user,user1,user2);
		System.out.println("List Of UserList" + userList);
		Page<User> page =new PageImpl<User>(userList);
		
		Mockito.when(userRepo.findAll((Pageable)Mockito.any())).thenReturn(page);
		PageableResponse<UserDto> actual =userService.getAllUser(1, 2,"name","asc");
		
		Assertions.assertEquals(3, actual.getContent().size()); 
		
	}
	
	@Test
	public void gatUserByIdTest() {
		String userid="asfdsfa";
		
		Mockito.when(userRepo.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		UserDto actualresultDto=userService.getUserById(userid);
		Assertions.assertNotNull(actualresultDto);
		Assertions.assertEquals(user.getName(), actualresultDto.getName(),"Name Not Matched");
	}
	
	@Test
	public void getUserByEmailTest() {
		String email="asfdsfa@gamil.com";
		
		Mockito.when(userRepo.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));
		UserDto actualresultDto=userService.getUserByEmail(email);
		Assertions.assertNotNull(actualresultDto);
		Assertions.assertEquals(user.getName(), actualresultDto.getName(),"Name Not Matched");
	}
	
	@Test
	public void searchUserTest() {
		String keyWord="Kumar";
		
		User user1 = User.builder().name("Ankit Kumar").email("Ankit@dev.in").about("This is testing Ankit user").gender("Male")
				.imageName("abc.png").password("abcd").roles(Set.of(role)).build();
		User user2 = User.builder().name("SRK Kumar").email("SRK@dev.in").about("This is testing SRK user").gender("Male")
				.imageName("abc.png").password("abcd").roles(Set.of(role)).build();
		
		List<User> userList=Arrays.asList(user,user1,user2);
		
		Mockito.when(userRepo.findByNameContaining(keyWord)).thenReturn(userList);
		List<UserDto> actualresultDto=userService.searchUser(keyWord);
		System.out.println("In searchUserTest" + actualresultDto);
		Assertions.assertNotNull(actualresultDto);
		Assertions.assertEquals(3, actualresultDto.size(),"Size Not Matched");
	}
}
