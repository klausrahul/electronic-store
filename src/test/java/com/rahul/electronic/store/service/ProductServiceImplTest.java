package com.rahul.electronic.store.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockitoSession;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.rahul.electronic.store.dto.PageableResponse;
import com.rahul.electronic.store.dto.ProductDto;
import com.rahul.electronic.store.entity.Category;
import com.rahul.electronic.store.entity.Product;
import com.rahul.electronic.store.repo.CategoryRepo;
import com.rahul.electronic.store.repo.ProductRepo;

@SpringBootTest
public class ProductServiceImplTest {

	@MockBean
	ProductRepo productRepo;

	@Autowired
	ProductService productService;

	@Autowired
	private ModelMapper mapper;

	@MockBean
	private CategoryRepo categoryRepo;
	
	
	Product product;
	
	Category category;

	@BeforeEach
	public void init() {
		category=Category.builder().title("Mobile").description("This category contain Mobile phone").coverImage("abc.png").build();
		product = Product.builder().title("Iphone 15").description("New iphone launched").price(80000)
				.discountedPrice(79000).quantatity(10).stock(true).live(true).category(category).build();
		
		
	}

	@Test
	public void createTest() {
		Mockito.when(productRepo.save(Mockito.any())).thenReturn(product);

		ProductDto dto = productService.create(mapper.map(product, ProductDto.class));
		System.out.println("Inside create Test Method" + dto.getTitle());
		Assertions.assertNotNull(dto);
		Assertions.assertEquals(product.getTitle(), dto.getTitle());

	}

	@Test
	public void updateTest() {
		String productId = "asa";

		ProductDto productDto = ProductDto.builder().title("Iphone 16 ").description("New iphone launched").price(80000)
				.discountedPrice(79000).quantatity(5).stock(true).live(true).build();

		Mockito.when(productRepo.findById(Mockito.anyString())).thenReturn(Optional.of(product));
		Mockito.when(productRepo.save(Mockito.any())).thenReturn(product);

		ProductDto updateDto = productService.update(productDto, productId);
		System.out.println(
				"Inside update Test Method" + updateDto.getTitle() + "Get Quantity" + updateDto.getQuantatity());
		Assertions.assertNotNull(updateDto);
		Assertions.assertEquals(productDto.getTitle(), updateDto.getTitle());
		Assertions.assertEquals(productDto.getQuantatity(), updateDto.getQuantatity());

	}

	@Test
	public void deleteTest() {

		String productId = "asa";
		Mockito.when(productRepo.findById(Mockito.anyString())).thenReturn(Optional.of(product));

		productService.delete(productId);
		Mockito.verify(productRepo, Mockito.times(1)).delete(product);
	}

	@Test
	public void getAllTest() {

		String productId = "asa";

		Product product1 = Product.builder().title("Iphone 16").description("New Iphone 16 launched").price(80000)
				.discountedPrice(79000).quantatity(10).stock(true).live(true).build();

		Product product2 = Product.builder().title("Samsung s20").description("New Samsung s20 launched").price(80000)
				.discountedPrice(79000).quantatity(10).stock(true).live(true).build();

		List<Product> productsList = Arrays.asList(product, product1, product2);

		Page<Product> page = new PageImpl<Product>(productsList);
		Mockito.when(productRepo.findAll((Pageable) Mockito.any())).thenReturn(page);

		PageableResponse<ProductDto> actualResult = productService.getAll(2, 5, "title", "asc");
		Assertions.assertNotNull(actualResult);
		Assertions.assertEquals(3, actualResult.getContent().size());

	}
	
	@Test
	public void getByIdTest() {
		String productId = "asa";
		Mockito.when(productRepo.findById(Mockito.anyString())).thenReturn(Optional.of(product));
		ProductDto result=productService.getById(productId);
		Assertions.assertNotNull(result);
		System.out.println("getByIdTest :: name is ::" + product.getTitle());
		System.out.println("getByIdTest :: name is ::" + result.getTitle());
		Assertions.assertEquals(product.getTitle(), result.getTitle());
		
	}
	
	@Test
	public void getLiveProductsTest() {
		Product product1 = Product.builder().title("Iphone 16").description("New Iphone 16 launched").price(80000)
				.discountedPrice(79000).quantatity(10).stock(true).live(true).build();

		Product product2 = Product.builder().title("Samsung s20").description("New Samsung s20 launched").price(80000)
				.discountedPrice(79000).quantatity(10).stock(true).live(false).build();
		 
		List<Product> list =Arrays.asList(product,product1,product2);
 		Page<Product> page =new PageImpl<>(list);
		Mockito.when(productRepo.findByLiveTrue(Mockito.any())).thenReturn(page);
		
		PageableResponse<ProductDto> result=productService.getLiveProducts(1, 10, "title", "asc");
		Assertions.assertNotNull(result);
		Assertions.assertEquals(3, result.getContent().size());
	}
	
	@Test
	public void searchByTitleTest() {
		
		Product product1 = Product.builder().title("Iphone 16").description("New Iphone 16 launched").price(80000)
				.discountedPrice(79000).quantatity(10).stock(true).live(true).build();

		Product product2 = Product.builder().title("Samsung s20").description("New Samsung s20 launched").price(80000)
				.discountedPrice(79000).quantatity(10).stock(true).live(false).build();
		List<Product> list =Arrays.asList(product,product1,product2);
		Page<Product> page =new PageImpl<>(list);
		Mockito.when(productRepo.findByTitleContaining(Mockito.anyString(), Mockito.any())).thenReturn(page);
		
		PageableResponse<ProductDto> result=productService.searchByTitle("dsdas", 1, 10,"title","asc");
		Assertions.assertNotNull(result);
		
	}
	
	@Test
	public void createProductWithCategoryTest() {
		
		Mockito.when(categoryRepo.findById(Mockito.anyString())).thenReturn(Optional.of(category));
		Mockito.when(productRepo.save(any())).thenReturn(product);
		ProductDto result=productService.createProductWithCategory("sfds",mapper.map(product, ProductDto.class) );
		
		System.out.println("Category is" + result);
		Assertions.assertNotNull(result);
	}
	
	
	
	
}
