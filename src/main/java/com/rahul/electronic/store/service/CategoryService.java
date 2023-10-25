package com.rahul.electronic.store.service;

import java.util.List;

import com.rahul.electronic.store.dto.CategoryDto;
import com.rahul.electronic.store.dto.PageableResponse;

public interface CategoryService {

	
	CategoryDto create(CategoryDto categoryDto);
	
	CategoryDto update(CategoryDto categoryDto,String categoryId);
	void delete(String categoryId);
	PageableResponse<CategoryDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir);
	
	CategoryDto getById(String categoryId);
	
	
	
}
