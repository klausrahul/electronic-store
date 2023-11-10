package com.rahul.electronic.store.serviceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

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

import com.rahul.electronic.store.dto.PageableResponse;
import com.rahul.electronic.store.dto.ProductDto;
import com.rahul.electronic.store.entity.Category;
import com.rahul.electronic.store.entity.Product;
import com.rahul.electronic.store.exception.ResourceNotFoundException;
import com.rahul.electronic.store.helper.Helper;
import com.rahul.electronic.store.repo.CategoryRepo;
import com.rahul.electronic.store.repo.ProductRepo;
import com.rahul.electronic.store.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	private Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
	@Autowired
	ProductRepo productRepo;
	@Autowired
	ModelMapper mapper;
	@Autowired
	CategoryRepo categoryRepo;
	
	@Value("${product.image.path}")
	private String imagePath;

	@Override
	public ProductDto create(ProductDto productDto) {

		Product product = mapper.map(productDto, Product.class);
		String uuid = UUID.randomUUID().toString();
		product.setProductId(uuid);
		//Added Date
		Date date=new Date();
		product.setAddedDate(date);
		Product saveProduct = productRepo.save(product);
		return mapper.map(saveProduct, ProductDto.class);
	}

	@Override
	public ProductDto update(ProductDto productDto, String productId) {
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Given Product not avaliable"));

		product.setTitle(productDto.getTitle());
		product.setDescription(productDto.getDescription());
		product.setQuantatity(productDto.getQuantatity());
		product.setPrice(productDto.getPrice());
		product.setDiscountedPrice(productDto.getDiscountedPrice());
		product.setLive(productDto.isLive());
		product.setStock(productDto.isStock());
		product.setProductImageName(productDto.getProductImageName());
		Product updatedProduct = productRepo.save(product);

		return mapper.map(updatedProduct, ProductDto.class);
	}

	@Override
	public void delete(String productId) {
		Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Given Product not avaliable"));
				
		String fullpath = imagePath + product.getProductImageName();
		try {
			Path path = Paths.get(fullpath);
			Files.delete(path);
		} catch (NoSuchFileException e) {
			log.info("User image not found in folder");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		productRepo.delete(product);

	}

	@Override
	public PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<Product> page = productRepo.findAll(pageable);
		PageableResponse<ProductDto> response = Helper.getpageableResponse(page, ProductDto.class);
		return response;
	}

	@Override
	public ProductDto getById(String productId) {
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Given Product not avaliable"));
		return mapper.map(product, ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> getLiveProducts(int pageNumber, int pageSize, String sortBy, String sortDir) {

		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<Product> page = productRepo.findByLiveTrue(pageable);
		PageableResponse<ProductDto> response = Helper.getpageableResponse(page, ProductDto.class);
		return response;

	}

	@Override
	public PageableResponse<ProductDto> searchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy,
			String sortDir) {
		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<Product> page = productRepo.findByTitleContaining(subTitle, pageable);
		PageableResponse<ProductDto> response = Helper.getpageableResponse(page, ProductDto.class);
		return response;
	}

	@Override
	public ProductDto createProductWithCategory(String categoryId, ProductDto productDto) {
		log.info("createProductWithCategory is===");
		Category category=categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Not Found For Given ID !!"));
		System.err.println("category is==="+category.getTitle());
		log.info("category is==="+category);
		Product product = mapper.map(productDto, Product.class);
		String uuid = UUID.randomUUID().toString();
		product.setProductId(uuid);
		//Added Date
		Date date=new Date();
		product.setAddedDate(date);
		product.setCategory(category);
		
		
		log.info("Product is==="+product);
		Product saveProduct = productRepo.save(product);
		log.info("Product is==="+saveProduct);
		
		log.info("Product is==="+mapper.map(saveProduct, ProductDto.class));
		return mapper.map(saveProduct, ProductDto.class);
	}

	@Override
	public ProductDto assignCategoryToProduct(String categoryId, String productId) {
		log.info("-----IN assignCategoryToProduct---------");
		Product product=productRepo.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product Not Found For Given ID !!"));

		Category category=categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Not Found For Given ID !!"));
		log.info("category is==="+category);
		
		product.setCategory(category);
		Product saveProduct = productRepo.save(product);
		return mapper.map(saveProduct, ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> getAllOFCategory(String categoryId, int pageNumber, int pageSize, String sortBy,
			String sortDir) {
		Category category=categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category Not Found For Given ID !!"));
		log.info("category is==="+category);
		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> products=productRepo.findByCategory(category,pageable);
		return Helper.getpageableResponse(products, ProductDto.class);
	}

}
