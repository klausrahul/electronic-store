package com.rahul.electronic.store.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rahul.electronic.store.entity.Category;

public interface CategoryRepo extends JpaRepository<Category, String> {

}
