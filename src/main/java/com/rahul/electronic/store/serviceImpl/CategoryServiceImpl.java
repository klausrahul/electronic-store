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

import com.rahul.electronic.store.dto.CategoryDto;
import com.rahul.electronic.store.dto.PageableResponse;
import com.rahul.electronic.store.entity.Category;
import com.rahul.electronic.store.exception.ResourceNotFoundException;
import com.rahul.electronic.store.helper.Helper;
import com.rahul.electronic.store.repo.CategoryRepo;
import com.rahul.electronic.store.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	private Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);
	@Autowired
	CategoryRepo categoryRepo;
	@Autowired
	ModelMapper mapper;

	@Value("${category.cover.image.path}")
	private String imagePath;

	@Override
	public CategoryDto create(CategoryDto categoryDto) {
		Category category = mapper.map(categoryDto, Category.class);
		String uuid = UUID.randomUUID().toString();
		category.setCategoryId(uuid);

		Category saveCategory = categoryRepo.save(category);
		return mapper.map(saveCategory, CategoryDto.class);
	}

	@Override
	public CategoryDto update(CategoryDto categoryDto, String categoryId) {
		Category category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Given Category not avaliable"));

		category.setTitle(categoryDto.getTitle());
		category.setDescription(categoryDto.getDescription());
		category.setCoverImage(categoryDto.getCoverImage());
		Category updatedCategory = categoryRepo.save(category);

		return mapper.map(updatedCategory, CategoryDto.class);
	}

	@Override
	public void delete(String categoryId) {
		Category category = categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Given Category not avaliable"));

		String fullpath = imagePath + category.getCoverImage();
		try {
			Path path = Paths.get(fullpath);
			Files.delete(path);
		} catch (NoSuchFileException e) {
			log.info("User image not found in folder");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		categoryRepo.delete(category);
	}

	@Override
	public PageableResponse<CategoryDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {

		log.info("Inside getAll");
		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		log.info("sort getAll" + sort);
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		log.info("pageable getAll" + pageable);

		Page<Category> page = categoryRepo.findAll(pageable);
		log.info("page getAll" + page);

		PageableResponse<CategoryDto> response = Helper.getpageableResponse(page, CategoryDto.class);
		log.info("response getAll" + response);

		return response;
	}

	@Override
	public CategoryDto getById(String categoryId) {
		Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Given Category not avaliable"));
				
		return mapper.map(category, CategoryDto.class);
	}

}
