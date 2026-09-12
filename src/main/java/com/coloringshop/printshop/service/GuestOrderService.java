package com.coloringshop.printshop.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.coloringshop.printshop.dto.OrderItemRequset;
import com.coloringshop.printshop.dto.OrderRequest;
import com.coloringshop.printshop.dto.orderRespons;
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

	public orderRespons createOrder(OrderRequest req) {
		
		Order order = new Order();
		order.setCustomerName(req.customerName());
		order.setPhone(req.phone());
		order.setAddress(req.address());
		order.setStatus(Status.PENDING);
		order.setCreatedAt(LocalDateTime.now());
		
        BigDecimal totalPrice = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();


        for (OrderItemRequset itemRequest : req.items()) {
            
            Book book = bookRepository.findById(itemRequest.bookId())
                    .orElseThrow(() -> new RuntimeException(
                            "الكتاب برقم " + itemRequest.bookId() + " غير موجود"));

            OrderItem orderItem = new OrderItem();
            orderItem.setBook(book);
            orderItem.setQuantity(itemRequest.quantity());
            orderItem.setPriceAtOrder(book.getPrice()); 
            orderItem.setOrder(order); // ربط بالطلب

            orderItems.add(orderItem);


            BigDecimal itemTotal = book.getPrice()
                    .multiply(BigDecimal.valueOf(itemRequest.quantity()));
            totalPrice = totalPrice.add(itemTotal);
        }
		
        order.setItems(orderItems);
        
        
        Order savedOrder = orderRepository.save(order);

		
        return new orderRespons(savedOrder.getId()
        		,savedOrder.getCustomerName() 
        		,savedOrder.getAddress()
        		, savedOrder.getPhone()
        		, savedOrder.getStatus()
        		,totalPrice
        		, savedOrder.getCreatedAt());
	
		
		
	}
	
	
    public orderRespons getOrderStatus(Long orderId) {
      
        Order order = orderRepository.findById(orderId)
                      .orElseThrow(()-> new RuntimeException("غير موجود الطلب"));
   
        

        BigDecimal totalPrice = BigDecimal.ZERO;
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                BigDecimal itemTotal = item.getPriceAtOrder()
                        .multiply(BigDecimal.valueOf(item.getQuantity()));
                totalPrice = totalPrice.add(itemTotal);
            }
        }
        
        return new orderRespons(
                order.getId(),
                order.getCustomerName(),
                order.getAddress(),
                order.getPhone(),
                order.getStatus(),
                totalPrice,
                order.getCreatedAt()
        );
    }
	 
	
	
}
