package com.rahul.electronic.store.service;

import com.rahul.electronic.store.dto.AddItemToCartRequest;
import com.rahul.electronic.store.dto.CartDTO;

public interface CartService {

	//Cart items  to cart
	//Case1 : cart for user not available : we will create the cart
	//case 2 : cart available we will add the item
	
	CartDTO addItemToCart(String userId,AddItemToCartRequest request);
	
	//Remove item from cart
	
	void removeItemFromCart(String userId,int cartItem);
	void clearCart(String userId);
	
	
	CartDTO getCartByUser(String userId);
}
