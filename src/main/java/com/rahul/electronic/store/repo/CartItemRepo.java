package com.rahul.electronic.store.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rahul.electronic.store.entity.CartItem;

public interface CartItemRepo extends JpaRepository<CartItem, Integer> {

}
