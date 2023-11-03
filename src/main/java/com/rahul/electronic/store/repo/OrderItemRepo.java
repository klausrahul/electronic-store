package com.rahul.electronic.store.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rahul.electronic.store.entity.OrderItems;

public interface OrderItemRepo extends JpaRepository<OrderItems, Integer>{

}
