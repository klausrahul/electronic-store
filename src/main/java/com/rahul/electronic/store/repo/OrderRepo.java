package com.rahul.electronic.store.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rahul.electronic.store.entity.Order;
import com.rahul.electronic.store.entity.User;

public interface OrderRepo extends JpaRepository<Order, String> {
	
	List<Order> findByUser(User user);

}
