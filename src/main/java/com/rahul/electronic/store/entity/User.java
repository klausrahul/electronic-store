package com.rahul.electronic.store.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "USERS")
public class User {
	
	@Id
	private String userId;
	@Column(name="user_name")
	private String name;
	@Column(name="user_email", unique =true)
	private String email;    
	@Column(name="user_password", length = 10)
	private String password;
	
	private String gender;
	
	@Column(length = 1000)
	private String about;
	
	@Column(name="user_image_name")
	private String imageName;
	
	@OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
	@JsonManagedReference
	private List<Order> orders=new ArrayList<>();

}
