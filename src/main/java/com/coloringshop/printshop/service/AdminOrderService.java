package com.coloringshop.printshop.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.coloringshop.printshop.dto.OrderDto.OrderItemRequset;
import com.coloringshop.printshop.dto.OrderDto.OrderRequest;
import com.coloringshop.printshop.dto.OrderDto.orderRespons;
import com.coloringshop.printshop.excption.BookNotFoundException;
import com.coloringshop.printshop.excption.OrderNotFoundException;
import com.coloringshop.printshop.model.Book;
import com.coloringshop.printshop.model.Order;
import com.coloringshop.printshop.model.OrderItem;
import com.coloringshop.printshop.model.Status;
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
					return new orderRespons(
							it.getId(),
							it.getCustomerName(),
							it.getAddress(),
							it.getPhone(),
							it.getStatus(),
							it.getPin(),
							it.getOrderCode(),
						    it.getTotalPrice(),
						     it.getCreatedAt()
							);
				}).collect(Collectors.toList());
		return ordersList;
	}

	public orderRespons updateOrder(String orderCode, OrderRequest orderReq) throws OrderNotFoundException, BookNotFoundException {
		Order order = orderRepository.findByOrderCode(orderCode);
		if (order == null) {
			throw new OrderNotFoundException("الاورد غير موجود " + orderCode);
		}

		order.setCustomerName(orderReq.customerName());
		order.setPhone(orderReq.phone());
		order.setAddress(orderReq.address());
		order.setStatus(Status.PENDING);

		BigDecimal totalPrice = BigDecimal.ZERO;
		List<OrderItem> items = new ArrayList<>();
		for (OrderItemRequset it : orderReq.items()) {
			Book book = bookRepository.findById(it.bookId())
					.orElseThrow(() -> new BookNotFoundException("الكتاب مش موجود :" + it.bookId()));
			OrderItem orderItem = new OrderItem();
			orderItem.setBook(book);
			orderItem.setPriceAtOrder(book.getPrice());
			orderItem.setQuantity(it.quantity());
			orderItem.setOrder(order);

			items.add(orderItem);

			BigDecimal itemTotal = book.getPrice()
					.multiply(BigDecimal.valueOf(it.quantity()));
			totalPrice = totalPrice.add(itemTotal);

		}
        order.getItems().clear();
		order.getItems().addAll(items);
		order.setTotalPrice(totalPrice);

		Order savedOrder = orderRepository.save(order);

		return new orderRespons(savedOrder.getId(), savedOrder.getCustomerName(), savedOrder.getAddress(),
				savedOrder.getPhone(), savedOrder.getStatus(), savedOrder.getPin(),savedOrder.getOrderCode(), savedOrder.getTotalPrice(), savedOrder.getCreatedAt());


	}

	public void deleteOrder(String orderCode) throws OrderNotFoundException {
		Order order = orderRepository.findByOrderCode(orderCode);
		if (order == null) {
			throw new OrderNotFoundException("الاورد غير موجود :" + orderCode);
		}
		 orderRepository.delete(order);
	}

	public orderRespons getOrderByCode(String orderCode , Status status) throws OrderNotFoundException {
		// TODO Auto-generated method stub
		Order order = orderRepository.findByOrderCode(orderCode);
		if(order == null) {
			throw new OrderNotFoundException("الاورد غير موجود :" + orderCode);
		}
		order.setStatus(status);
		Order savedOrder = orderRepository.save(order);
		 return new orderRespons(savedOrder.getId(), savedOrder.getCustomerName(), savedOrder.getAddress(),
					savedOrder.getPhone(), savedOrder.getStatus(), savedOrder.getPin(),savedOrder.getOrderCode(), savedOrder.getTotalPrice() , savedOrder.getCreatedAt());

	}
	}
	
	
	
	
	

	
	
	


