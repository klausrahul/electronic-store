package com.rahul.electronic.store.serviceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.rahul.electronic.store.dto.PageableResponse;
import com.rahul.electronic.store.dto.UserDto;
import com.rahul.electronic.store.entity.User;
import com.rahul.electronic.store.exception.ResourceNotFoundException;
import com.rahul.electronic.store.helper.Helper;
import com.rahul.electronic.store.repo.UserRepo;
import com.rahul.electronic.store.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	private Logger log=LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	private UserRepo repo;
	@Autowired
	private ModelMapper mapper;
	@Value("${user.profile.image.path}")
	private String imagePath;

	@Override
	public UserDto createUser(UserDto userDto) {

		String id = UUID.randomUUID().toString();
		userDto.setUserId(id);
		User userResult = repo.save(dtoToEntity(userDto));

		return entityToDto(userResult);
	}

	@Override
	public UserDto updateUser(UserDto userDto, String userId) {

		User user = repo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("No User Found For Given Id !!"));

		user.setName(userDto.getName());
		user.setAbout(userDto.getAbout());
		user.setGender(userDto.getGender());
		user.setPassword(userDto.getPassword());
		user.setImageName(userDto.getImageName());

		// save user

		User upadatedDto = repo.save(user);

		return entityToDto(upadatedDto);
	}

	@Override
	public void deleteUser(String userId) {
		User user = repo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("No User Found For Given Id !!"));
		
		String fullpath =imagePath + user.getImageName();
		
		try {
			Path path=Paths.get(fullpath);
			Files.delete(path);
		} catch (NoSuchFileException e) {
			log.info("User image not found in folder");
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		repo.delete(user);

	}

	@Override
	public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {

		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<User> page = repo.findAll(pageable);
		PageableResponse<UserDto> response=Helper.getpageableResponse(page, UserDto.class);

		return response;

	}

	@Override
	public UserDto getUserById(String userId) {
		User user = repo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("No User Found For Given Id !!"));

		return entityToDto(user);
	}

	@Override
	public UserDto getUserByEmail(String emailId) {
		User user = repo.findByEmail(emailId)
				.orElseThrow(() -> new ResourceNotFoundException("No User Found For Given Email !!"));
		return entityToDto(user);
	}

	@Override
	public List<UserDto> searchUser(String keyword) {
		// TODO Auto-generated method stub
		List<User> allUsers = repo.findByNameContaining(keyword);

		List<UserDto> alluserDto = allUsers.stream().map(user -> entityToDto(user)).collect(Collectors.toList());

		return alluserDto;
	}

	private User dtoToEntity(UserDto dto) {
		User user = mapper.map(dto, User.class);
		return user;
	}

	private UserDto entityToDto(User user) {
		UserDto userDto = mapper.map(user, UserDto.class);
		return userDto;
	}

}
