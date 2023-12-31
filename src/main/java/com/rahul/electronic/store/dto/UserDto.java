package com.rahul.electronic.store.dto;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.rahul.electronic.store.entity.Role;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	
	private String userId;
	@Size(min=3,max=20,message = "Invalid name !!")
	@ApiModelProperty(value = "user_name",name = "UserName",required = true,notes = "User Name of new user!!")
	private String name;
	
	@Email(message = "Invalid User Email")
	//@Pattern(regexp = " ^[a-zA-Z0-9_!#$%&'*+/=?{|}~^-]+@[a-zA-Z]{2,6}$", message = "Invalid User Email")
	@NotBlank(message = "Email is Required !!")
	private String email;    
	@NotBlank(message = "Password is required !!")
	private String password;
	@Size(min = 4 , max = 6 ,message = "Invalid Gender !!")
	private String gender;
	@NotBlank(message = "Write about your self !!")
	private String about;
	
	private String imageName;
	private Set<RoleDto> roles=new HashSet<>();
}
