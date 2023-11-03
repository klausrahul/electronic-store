package com.rahul.electronic.store.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rahul.electronic.store.dto.AddItemToCartRequest;
import com.rahul.electronic.store.dto.ApiResponseMessage;
import com.rahul.electronic.store.dto.CartDTO;
import com.rahul.electronic.store.service.CartService;

@RestController
@RequestMapping("/carts")
public class CartController {

	@Autowired
	private CartService cartService;

	@PostMapping("/{userId}")
	public ResponseEntity<CartDTO> addItemToCart(@RequestBody AddItemToCartRequest request,
			@PathVariable String userId){

		CartDTO cartDto=cartService.addItemToCart(userId, request);
		
		return new ResponseEntity<CartDTO>(cartDto,HttpStatus.OK);
	}

	@DeleteMapping("/{userId}/items/{itemId}")
	public  ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable String userId,@PathVariable int itemId){
		
		cartService.removeItemFromCart(userId, itemId);
		ApiResponseMessage apiResponseMessage= ApiResponseMessage.builder().message("Item is removed !!").status(HttpStatus.OK).success(true).build();
		return new ResponseEntity<ApiResponseMessage>(apiResponseMessage,HttpStatus.OK);
	
	}
	
	
	@DeleteMapping("/{userId}")
	public  ResponseEntity<ApiResponseMessage> clearCart(@PathVariable String userId){
		
		cartService.clearCart(userId);
		ApiResponseMessage apiResponseMessage= ApiResponseMessage.builder().message("Cart is blank !!").status(HttpStatus.OK).success(true).build();
		return new ResponseEntity<ApiResponseMessage>(apiResponseMessage,HttpStatus.OK);
	
	}
	
	@GetMapping("/{userId}")
	public  ResponseEntity<CartDTO> getCart(@PathVariable String userId){
		
		CartDTO dto=cartService.getCartByUser(userId);
		return new ResponseEntity<CartDTO>(dto,HttpStatus.OK);
	
	}
}
