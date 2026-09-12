package com.coloringshop.printshop.service;

import org.springframework.stereotype.Service;

import com.coloringshop.printshop.model.Order;
import com.coloringshop.printshop.repository.AdminRepository;
import com.coloringshop.printshop.repository.OrderRepository;


@Service
public class AdminOrderService {
	
	private AdminRepository adminRepository;
	private OrderRepository orderRepository;
	
	public AdminOrderService(AdminRepository adminRepository, OrderRepository orderRepository) {
		super();
		this.adminRepository = adminRepository;
		this.orderRepository = orderRepository;
	}
//	
//	public Order createNewOrder(Order order) {
//		
//	}
	

	
	
	

}
