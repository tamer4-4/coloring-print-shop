package com.coloringshop.printshop.service;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class GuestOrderService {

	private OrderRepository orderRepository;
	private BookRepository bookRepository;

	public GuestOrderService(OrderRepository orderRepository, BookRepository bookRepository) {
		super();
		this.orderRepository = orderRepository;
		this.bookRepository = bookRepository;
	}


	@Transactional
	public orderRespons createOrder(OrderRequest req) {

		Order order = new Order();
		order.setCustomerName(req.customerName());
		order.setPhone(req.phone());
		order.setAddress(req.address());
		order.setStatus(Status.PENDING);
		order.setCreatedAt(LocalDateTime.now());
        order.setOrderCode("ORD-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase()); 
		BigDecimal totalPrice = BigDecimal.ZERO;
		List<OrderItem> orderItems = new ArrayList<>();

		for (OrderItemRequset itemRequest : req.items()) {

			Book book = bookRepository.findById(itemRequest.bookId())
					.orElseThrow(() -> new BookNotFoundException("الكتاب برقم " + itemRequest.bookId() + " غير موجود"));

			OrderItem orderItem = new OrderItem();
			orderItem.setBook(book);
			orderItem.setQuantity(itemRequest.quantity());
			orderItem.setPriceAtOrder(book.getPrice());
			orderItem.setOrder(order); // ربط بالطلب

			orderItems.add(orderItem);

			BigDecimal itemTotal = book.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity()));
			totalPrice = totalPrice.add(itemTotal);
		}

		order.getItems().addAll(orderItems);
         order.setTotalPrice(totalPrice);
		Order savedOrder = orderRepository.save(order);

		return new orderRespons(savedOrder.getId(), savedOrder.getCustomerName(), savedOrder.getAddress(),
				savedOrder.getPhone(), savedOrder.getStatus(), savedOrder.getPin(),savedOrder.getOrderCode(), savedOrder.getTotalPrice(), savedOrder.getCreatedAt());

	}

	public orderRespons getOrderStatus(String orderCode) throws AccessDeniedException, OrderNotFoundException {
             
		Order order = orderRepository.findByOrderCode(orderCode);
		if (order == null) {
			throw new OrderNotFoundException("الاورد غير موجود :" + orderCode);
		}
          


		return new orderRespons(order.getId(), order.getCustomerName(), order.getAddress(), order.getPhone(),
				order.getStatus(), order.getPin(),order.getOrderCode() ,order.getTotalPrice(), order.getCreatedAt());
	}

	@Transactional
	public orderRespons updateOrder(String orderCode
			,String pin
			, OrderRequest orderReq) throws AccessDeniedException, OrderNotFoundException {
		Order order = orderRepository.findByOrderCode(orderCode);
		if (order == null) {
			throw new OrderNotFoundException("الاورد غير موجود :" + orderCode);
		}
		
		if (!(order.getStatus().equals(Status.PENDING))) {
			throw new OrderNotFoundException("لا يمكن تعديل هذا الطلب بعد الان " + orderCode);
		}
		if(!(order.getPin().equals(pin))) {
			throw new AccessDeniedException("غير مسموح");
		}
		
		order.setCustomerName(orderReq.customerName());
		order.setPhone(orderReq.phone());
		order.setAddress(orderReq.address());
		order.setStatus(Status.PENDING);

		BigDecimal totalPrice = BigDecimal.ZERO;
		List<OrderItem> items = new ArrayList<>();
		for (OrderItemRequset it : orderReq.items()) {
			Book book = bookRepository.findById(it.bookId())
					.orElseThrow(() -> new BookNotFoundException("الكتاب غير موجود " + it.bookId()));
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
	
	@Transactional
	public void deleteOrder(String orderCode
			,String pin
             ) throws AccessDeniedException, OrderNotFoundException {
		Order order = orderRepository.findByOrderCode(orderCode);
		if (order == null) {
			throw new OrderNotFoundException("الاورد غير موجود :" + orderCode);
		}
		if (!(order.getStatus().equals(Status.PENDING))) {
			throw new OrderNotFoundException("لا يمكن حذف هذا الطلب بعد الان " + orderCode);
		}
		
		if(!(order.getPin().equals(pin))) {
			throw new AccessDeniedException("غير مسموح  لك حذف هذا الطلب");
		}
		 orderRepository.delete(order);
	}

}
