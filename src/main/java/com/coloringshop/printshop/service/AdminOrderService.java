package com.coloringshop.printshop.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.coloringshop.printshop.dto.orderRespons;
import com.coloringshop.printshop.model.Book;
import com.coloringshop.printshop.model.Order;
import com.coloringshop.printshop.model.OrderItem;
import com.coloringshop.printshop.repository.BookRepository;
import com.coloringshop.printshop.repository.OrderRepository;



@Service
public class AdminOrderService {
	
	private OrderRepository orderRepository;
	private BookRepository  bookRepository; 
	public AdminOrderService(OrderRepository orderRepository , BookRepository bookRepository) {
		super();
		this.orderRepository = orderRepository;
		this.bookRepository = bookRepository;
	}

	public List<orderRespons> getAllOrders() {
		List<Order> orders =  orderRepository.findAll();
		List<orderRespons> ordersList = orders  
				.stream()
				.map((it) -> {
					BigDecimal totalPrice = BigDecimal.ZERO;

					for (OrderItem itOrder : it.getItems()) {
					Book book = bookRepository.findById(itOrder.getBook().getId()).
								orElseThrow(() -> new RuntimeException("غير موجود الكتاب " + itOrder.getBook().getId()));

					BigDecimal itemTotal = book.getPrice()
							.multiply(BigDecimal.valueOf(itOrder.getQuantity()));
					totalPrice = totalPrice.add(itemTotal);
					}
					
					return new orderRespons(
							it.getId(),
							it.getCustomerName(),
							it.getAddress(),
							it.getPhone(),
							it.getStatus(),
							it.getPin(),
							it.getOrderCode(),
						     totalPrice,
						     it.getCreatedAt()
							);
				}).collect(Collectors.toList());
		return ordersList;
	}
	

	
	
	

}
