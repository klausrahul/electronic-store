package com.rahul.electronic.store.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.rahul.electronic.store.dto.CategoryDto;
import com.rahul.electronic.store.dto.ImageReponse;
import com.rahul.electronic.store.dto.PageableResponse;
import com.rahul.electronic.store.service.CategoryService;
import com.rahul.electronic.store.service.FileService;

@RestController
@RequestMapping("/categories")
public class CategoryController {
	private Logger log = LoggerFactory.getLogger(CategoryController.class);

	@Autowired
	private CategoryService service;

	@Autowired
	private FileService fileService;

	@Value("${category.cover.image.path}")
	private String imageUploadPath;

	@PostMapping("/create")
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto request) {
		CategoryDto user = service.create(request);
		return new ResponseEntity<CategoryDto>(user, HttpStatus.CREATED);
	}

	@PutMapping("/update/{categoryId}")
	public ResponseEntity<CategoryDto> updateCategory(@PathVariable("categoryId") String categoryId,
			@Valid @RequestBody CategoryDto request) {
		CategoryDto user = service.update(request, categoryId);
		return new ResponseEntity<CategoryDto>(user, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{categoryId}")
	public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable("categoryId") String categoryId) {
		service.delete(categoryId);
		ApiResponseMessage response = ApiResponseMessage.builder().message("Deleted Successfully !!").success(true)
				.status(HttpStatus.OK).build();
		return new ResponseEntity<ApiResponseMessage>(response, HttpStatus.OK);
	}

	@GetMapping("/getCategory/{categoryId}")
	public ResponseEntity<CategoryDto> getById(@PathVariable("categoryId") String categoryId) {
		CategoryDto category = service.getById(categoryId);
		return new ResponseEntity<CategoryDto>(category, HttpStatus.OK);
	}

	@GetMapping("/getAllCategory")
	public ResponseEntity<PageableResponse<CategoryDto>> getAllCategory(
			@RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,

			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,

			@RequestParam(value = "sortBy", required = false, defaultValue = "title") String sortBy,

			@RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir) {
		PageableResponse<CategoryDto> allCategory = service.getAll(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<PageableResponse<CategoryDto>>(allCategory, HttpStatus.OK);
	}

	@PostMapping("/image/{categoryId}")
	public ResponseEntity<ImageReponse> uploadImage(@RequestParam("categoryImage") MultipartFile file,
			@PathVariable("categoryId") String categoryId) throws IOException {

		String imageName = fileService.uploadImage(file, imageUploadPath);

		CategoryDto category = service.getById(categoryId);
		category.setCoverImage(imageName);
		service.update(category, categoryId);

		ImageReponse imageReponse = ImageReponse.builder().imageName(imageName).success(true).status(HttpStatus.OK)
				.build();

		return new ResponseEntity<ImageReponse>(imageReponse, HttpStatus.OK);

	}

	@GetMapping("/getImage/{categoryId}")
	public void getImage(@PathVariable("categoryId") String categoryId, HttpServletResponse response)
			throws IOException {

		CategoryDto category = service.getById(categoryId);

		log.info("Cover Image Name {}", category.getCoverImage());
		InputStream coverImage = fileService.getResource(imageUploadPath, category.getCoverImage());
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(coverImage, response.getOutputStream());

	}

}
