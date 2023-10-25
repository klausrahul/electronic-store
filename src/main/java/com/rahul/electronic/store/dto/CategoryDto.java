package com.rahul.electronic.store.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
public class CategoryDto {
	private String categoryId;
	
	@NotBlank
	@Size(min=4,max = 30,message = "title must be of minimum 4 characters !!")
	private String title;
	@NotBlank
	private String description;
	private String coverImage;

}
