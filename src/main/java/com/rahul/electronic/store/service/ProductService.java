package com.rahul.electronic.store.service;

import java.util.List;

import com.rahul.electronic.store.dto.PageableResponse;
import com.rahul.electronic.store.dto.ProductDto;

public interface ProductService {

	
	ProductDto create(ProductDto productDto);
	
	ProductDto update(ProductDto productDto,String productId);
	void delete(String productId);
	PageableResponse<ProductDto> getAll(int pageNumber,int pageSize,String sortBy,String sortDir);
	
	ProductDto getById(String productId);
	
	PageableResponse<ProductDto> getLiveProducts(int pageNumber,int pageSize,String sortBy,String sortDir);
	
	PageableResponse<ProductDto> searchByTitle(String subTitle,int pageNumber,int pageSize,String sortBy,String sortDir);
	
	ProductDto createProductWithCategory(String categoryId,ProductDto productDto);
	
	ProductDto assignCategoryToProduct(String categoryId,String productId);
	
	
	PageableResponse<ProductDto> getAllOFCategory(String categoryId, int pageNumber, int pageSize, String sortBy,
			String sortDir);
	
	
}
