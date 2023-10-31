package com.rahul.electronic.store.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.rahul.electronic.store.entity.Category;
import com.rahul.electronic.store.entity.Product;

public interface ProductRepo extends JpaRepository<Product, String> {
	
	
		Page<Product> findByTitleContaining(String subTitle,Pageable page);
		Page<Product> findByLiveTrue(Pageable page);
		Page<Product> findByStockTrue(Pageable page);
		
		Page<Product> findByCategory(Category category,Pageable page);
		
		

}
