package com.rahul.electronic.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.rahul.electronic.store.entity.Role;
import com.rahul.electronic.store.repo.RoleRepo;

@SpringBootApplication
@EnableWebMvc
public class ElectronicStoreApplication implements CommandLineRunner {

	@Autowired
	private RoleRepo repo;
	
	@Value("${normal.role.id}")
	private String normal_role_id;
	@Value("${admin.role.id}")
	private String admin_role_id;
	
	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}

	@Autowired
	private PasswordEncoder encoder;

	@Override
	public void run(String... args) throws Exception {
		System.out.println(encoder.encode("abcd"));
		
		
		
		Role rold_admin =Role.builder().roleId(admin_role_id).roleName("ROLE_ADMIN").build();
		Role rold_normal =Role.builder().roleId(normal_role_id).roleName("ROLE_NORMAL").build();
		
		repo.save(rold_admin);
		repo.save(rold_normal);
	}

}
