package com.coloringshop.printshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coloringshop.printshop.model.Order;
import com.coloringshop.printshop.model.Status;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByStatus(Status status);
}
