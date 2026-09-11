package com.coloringshop.printshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coloringshop.printshop.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
	List<OrderItem> findByBook_Id(Long bookId);
}
