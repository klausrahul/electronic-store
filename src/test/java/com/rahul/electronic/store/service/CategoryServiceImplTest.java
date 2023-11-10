package com.rahul.electronic.store.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.rahul.electronic.store.repo.CategoryRepo;

@SpringBootTest
public class CategoryServiceImplTest {

	
	@MockBean
	CategoryRepo categoryRepo;
	@Autowired
	ModelMapper mapper;
}
