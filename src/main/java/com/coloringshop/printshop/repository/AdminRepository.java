package com.coloringshop.printshop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coloringshop.printshop.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
	Optional<Admin> findByUsername(String username);
}
