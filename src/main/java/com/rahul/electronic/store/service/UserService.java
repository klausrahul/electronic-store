package com.rahul.electronic.store.service;

import java.util.List;

import com.rahul.electronic.store.dto.PageableResponse;
import com.rahul.electronic.store.dto.UserDto;
import com.rahul.electronic.store.entity.User;

public interface UserService {

	UserDto createUser(UserDto userDto);
	
	UserDto updateUser(UserDto userDto,String userId);
	
	void deleteUser(String userId);
	
	PageableResponse<UserDto> getAllUser(int pageNumber,int pageSize,String sortBy,String sortDir);
	
	UserDto getUserById(String userId);
	UserDto getUserByEmail(String emailId);
	
	//search user
	
	List<UserDto> searchUser(String keyword);
}
