package com.rahul.electronic.store.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rahul.electronic.store.entity.Cart;
import com.rahul.electronic.store.entity.User;

public interface CartRepo extends JpaRepository<Cart, String>{
	
	Optional<Cart>  findByUser(User user);
	

}
