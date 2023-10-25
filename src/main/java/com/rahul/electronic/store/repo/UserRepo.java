package com.rahul.electronic.store.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rahul.electronic.store.entity.User;

public interface UserRepo extends JpaRepository<User, String>{

	Optional<User> findByEmail(String email);
	
	List<User> findByNameContaining(String keywords);
}
