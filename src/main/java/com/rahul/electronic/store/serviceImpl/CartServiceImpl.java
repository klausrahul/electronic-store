package com.rahul.electronic.store.serviceImpl;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rahul.electronic.store.dto.AddItemToCartRequest;
import com.rahul.electronic.store.dto.CartDTO;
import com.rahul.electronic.store.entity.Cart;
import com.rahul.electronic.store.entity.CartItem;
import com.rahul.electronic.store.entity.Product;
import com.rahul.electronic.store.entity.User;
import com.rahul.electronic.store.exception.BadApiRequestException;
import com.rahul.electronic.store.exception.ResourceNotFoundException;
import com.rahul.electronic.store.repo.CartItemRepo;
import com.rahul.electronic.store.repo.CartRepo;
import com.rahul.electronic.store.repo.ProductRepo;
import com.rahul.electronic.store.repo.UserRepo;
import com.rahul.electronic.store.service.CartService;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private ProductRepo productRepo;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private CartRepo cartRepo;
	
	@Autowired
	private CartItemRepo cartItemRepo;

	@Autowired
	private ModelMapper mapper;

	@Override
	public CartDTO addItemToCart(String userId, AddItemToCartRequest request) {

		int quantity = request.getQuantity();
		String productId = request.getProductId();
		
		if(quantity<=0) {
			throw new BadApiRequestException("Requested quantity has not valid");
		}

		// get product
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product Not Found For Given Id !!"));

		// get User
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("No User Found For Given Id !!"));
		Cart cart = null;
		try {
			cart = cartRepo.findByUser(user).get();
		} catch (NoSuchElementException e) {

			cart = new Cart();
			cart.setCartId(UUID.randomUUID().toString());
			cart.setCreatedAT(new Date());
		}

		// perform cart opration
		AtomicReference<Boolean> updated=new AtomicReference<>(false);
		List<CartItem> items = cart.getItems();
		items =items.stream().map(item ->{
			if(item.getProduct().getProductId().equals(productId)) {
				//item already present
				item.setQuantity(quantity);
				item.setTotalPrice(quantity * product.getDiscountedPrice());
				updated.set(true);
			}
			return item;
			
		}).collect(Collectors.toList());

		//cart.setItems(updatedItems);
		
		//create items
		if(!updated.get()) {
			CartItem cartItem=CartItem.builder().quantity(quantity).totalPrice(quantity * product.getDiscountedPrice()).cart(cart).product(product)
					.build();
			cart.getItems().add(cartItem);
		}
		
		
		
		cart.setUser(user);
		Cart updatedCart=cartRepo.save(cart);

		return mapper.map(updatedCart, CartDTO.class);
	}

	@Override
	public void removeItemFromCart(String userId, int cartItemId) {
			
		CartItem cartItem1=cartItemRepo.findById(cartItemId).orElseThrow(() -> new ResourceNotFoundException("Cart Item Not Found !!"));
		cartItemRepo.delete(cartItem1);

	}

	@Override
	public void clearCart(String userId) {
		// get User
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("No User Found For Given Id !!"));
		Cart cart = cartRepo.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart of given User not Found  !!"));
		cart.getItems().clear();
		cartRepo.save(cart);

	}

	@Override
	public CartDTO getCartByUser(String userId) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("No User Found For Given Id !!"));
		Cart cart = cartRepo.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart of given User not Found  !!"));
		return mapper.map(cart, CartDTO.class);
	}

}
