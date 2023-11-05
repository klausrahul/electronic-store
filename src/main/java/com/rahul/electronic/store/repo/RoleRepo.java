package com.rahul.electronic.store.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rahul.electronic.store.entity.Role;

public interface RoleRepo extends JpaRepository<Role, String> {

}
