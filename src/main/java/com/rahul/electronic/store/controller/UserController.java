package com.rahul.electronic.store.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rahul.electronic.store.dto.ApiResponseMessage;
import com.rahul.electronic.store.dto.ImageReponse;
import com.rahul.electronic.store.dto.PageableResponse;
import com.rahul.electronic.store.dto.UserDto;
import com.rahul.electronic.store.service.FileService;
import com.rahul.electronic.store.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/user")
@Api(value = "UserController",description = "This is User Related APIS")
public class UserController {
	
	private Logger log=LoggerFactory.getLogger(UserController.class);
	
	
	@Autowired
	private UserService service;
	@Autowired
	private FileService fileService;
	
	@Value("${user.profile.image.path}")
	private String imageUploadPath;

	@PostMapping("/create")
	@ApiOperation(value = "Create User!!!!!!",tags = {"user-controller"})
	@ApiResponses(value = {
			@ApiResponse(code = 200,message = "SUCCESS | OK"),
			@ApiResponse(code = 401,message = "Not Auhtorized"),
			@ApiResponse(code = 201,message = "New Uesr Created !!")
	})
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto request) {
		UserDto user = service.createUser(request);
		return new ResponseEntity<UserDto>(user, HttpStatus.CREATED);
	}

	@PutMapping("/update/{userId}")
	public ResponseEntity<UserDto> updateUser(@PathVariable("userId") String userId, @Valid @RequestBody UserDto request) {
		UserDto user = service.updateUser(request, userId);
		return new ResponseEntity<UserDto>(user, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable("userId") String userId) {
		service.deleteUser(userId);
		ApiResponseMessage message = ApiResponseMessage.builder().message("Deleted Successfully !!").success(true)
				.status(HttpStatus.OK).build();
		return new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
	}

	
	@GetMapping("/getall")
	@ApiOperation(value = "get All Users",response = ResponseEntity.class,tags = {"user-controller","user-api"})
	@ApiResponses(value = {
			@ApiResponse(code = 200,message = "SUCCESS | OK"),
			@ApiResponse(code = 401,message = "Not Auhtorizes")
	})
	public ResponseEntity<PageableResponse<UserDto>> getAllUser(
			@RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,

			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,

			@RequestParam(value = "sortBy", required = false, defaultValue = "name") String sortBy,

			@RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir) {
		PageableResponse<UserDto> allUser = service.getAllUser(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<PageableResponse<UserDto>>(allUser, HttpStatus.OK);
	}
	 

	@GetMapping("/get/{userId}")
	public ResponseEntity<UserDto> getById(@PathVariable("userId") String userId) {
		UserDto user = service.getUserById(userId);
		return new ResponseEntity<UserDto>(user, HttpStatus.OK);
	}

	@GetMapping("/getemail/{email}")
	public ResponseEntity<UserDto> getByEmail(@PathVariable("email") String email) {
		UserDto user = service.getUserByEmail(email);
		return ResponseEntity.ok(user);
	}

	@GetMapping("/search/{keyword}")
	public ResponseEntity<List<UserDto>> searchUser(@PathVariable("keyword") String keyword) {
		List<UserDto> user = service.searchUser(keyword);
		return ResponseEntity.ok(user);
	}
	
	
	@PostMapping("/image/{userId}")
	public ResponseEntity<ImageReponse> uploadImage(@RequestParam("userImage") MultipartFile file,
			@PathVariable("userId") String userId) throws IOException{
		
		String imageName=fileService.uploadImage(file, imageUploadPath);
		
		UserDto user=service.getUserById(userId);
		user.setImageName(imageName);
		UserDto userDto =service.updateUser(user, userId);
		
		
		ImageReponse imageReponse=ImageReponse.builder().imageName(imageName).success(true).status(HttpStatus.OK).build();
		
		return new ResponseEntity<ImageReponse>(imageReponse,HttpStatus.OK);
		
	}
	
	
	@GetMapping("/getImage/{userId}")
	public void getImage(@PathVariable("userId") String userId, HttpServletResponse response) throws IOException{
		
		UserDto user=service.getUserById(userId);
		
		log.info("User Image Name {}" , user.getImageName());
		InputStream userImage=fileService.getResource(imageUploadPath, user.getImageName());
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(userImage, response.getOutputStream());
		
	}
	
	
	@GetMapping("/downloadImage/{userId}")
	public ResponseEntity<Resource> downloadImage(@PathVariable("userId") String userId) throws IOException{
		log.error("Entered in downloadImage " );
		UserDto user=service.getUserById(userId);
		log.error("User Image Name {}" , user.getImageName());
		
		
		 HttpHeaders header = new HttpHeaders();
	        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;" +"filename="+ user.getImageName());
	        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
	        header.add("Pragma", "no-cache");
	        header.add("Expires", "0");
		
		
		
		
		
		InputStreamResource userImage=fileService.downloadImage(imageUploadPath, user.getImageName());
		 return ResponseEntity.ok()
		            .headers(header)
		            .contentLength(user.getImageName().length())
		            .contentType(MediaType.APPLICATION_OCTET_STREAM)
		            .body(userImage);	
	}

}
