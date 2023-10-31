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
import com.rahul.electronic.store.dto.ProductDto;
import com.rahul.electronic.store.service.FileService;
import com.rahul.electronic.store.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

	
	private Logger log = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private ProductService service;
	
	@Autowired
	private FileService fileService;

	@Value("${product.image.path}")
	private String imageUploadPath;



	@PostMapping("/create")
	public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto request) {
		ProductDto product = service.create(request);
		return new ResponseEntity<ProductDto>(product, HttpStatus.CREATED);
	}
	
	@PutMapping("/update/{productId}")
	public ResponseEntity<ProductDto> updateProduct(@PathVariable("productId") String productId,
			@Valid @RequestBody ProductDto request) {
		ProductDto product = service.update(request, productId);
		return new ResponseEntity<ProductDto>(product, HttpStatus.OK);
	}

	@DeleteMapping("/delete/{productId}")
	public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable("productId") String productId) {
		service.delete(productId);
		ApiResponseMessage response = ApiResponseMessage.builder().message("Deleted Successfully !!").success(true)
				.status(HttpStatus.OK).build();
		return new ResponseEntity<ApiResponseMessage>(response, HttpStatus.OK);
	}
	
	
	
	@GetMapping("/getProduct/{productId}")
	public ResponseEntity<ProductDto> getById(@PathVariable("productId") String productId) {
		ProductDto product = service.getById(productId);
		return new ResponseEntity<ProductDto>(product, HttpStatus.OK);
	}

	@GetMapping("/getAllProduct")
	public ResponseEntity<PageableResponse<ProductDto>> getAllProduct(
			@RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,

			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,

			@RequestParam(value = "sortBy", required = false, defaultValue = "title") String sortBy,

			@RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir) {
		PageableResponse<ProductDto> allProduct = service.getAll(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<PageableResponse<ProductDto>>(allProduct, HttpStatus.OK);
	}
	
	
	@GetMapping("/getLiveProducts")
	public ResponseEntity<PageableResponse<ProductDto>> getLiveProducts(
			@RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,

			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,

			@RequestParam(value = "sortBy", required = false, defaultValue = "title") String sortBy,

			@RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir) {
		PageableResponse<ProductDto> allProduct = service.getLiveProducts(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<PageableResponse<ProductDto>>(allProduct, HttpStatus.OK);
	}
	
	
	@GetMapping("/search/{subTitle}")
	public ResponseEntity<PageableResponse<ProductDto>> searchProducts(@PathVariable("subTitle") String subTitle,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,

			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,

			@RequestParam(value = "sortBy", required = false, defaultValue = "title") String sortBy,

			@RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir) {
		PageableResponse<ProductDto> allProduct = service.searchByTitle(subTitle,pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<PageableResponse<ProductDto>>(allProduct, HttpStatus.OK);
	}
	
	
	@PostMapping("/image/{productId}")
	public ResponseEntity<ImageReponse> uploadImage(@RequestParam("productImage") MultipartFile file,
			@PathVariable("productId") String productId) throws IOException {

		String imageName = fileService.uploadImage(file, imageUploadPath);

		ProductDto product = service.getById(productId);
		product.setProductImageName(imageName);
		service.update(product, productId);

		ImageReponse imageReponse = ImageReponse.builder().imageName(imageName).success(true).status(HttpStatus.OK).message("Product Image uploaded Successfully !!")
				.build();

		return new ResponseEntity<ImageReponse>(imageReponse, HttpStatus.OK);

	}
	
	@GetMapping("/getImage/{productId}")
	public void getImage(@PathVariable("productId") String productId, HttpServletResponse response)
			throws IOException {

		ProductDto product = service.getById(productId);

		log.info("Product Image Name {}", product.getProductImageName());
		InputStream coverImage = fileService.getResource(imageUploadPath, product.getProductImageName());
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(coverImage, response.getOutputStream());

	}

}
