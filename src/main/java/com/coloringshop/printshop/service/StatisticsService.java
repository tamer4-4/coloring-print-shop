package com.coloringshop.printshop.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coloringshop.printshop.dto.StatisticsResponse.StatisticsResponse;
import com.coloringshop.printshop.model.Order;
import com.coloringshop.printshop.model.OrderItem;
import com.coloringshop.printshop.model.Status;
import com.coloringshop.printshop.repository.OrderRepository;

@Service
@Transactional(readOnly = true)
public class StatisticsService {

	  private final OrderRepository orderRepository;

	    public StatisticsService(OrderRepository orderRepository) {
	        this.orderRepository = orderRepository;
	    }
	    
	    
	    @PreAuthorize("hasRole('ADMIN')")
	    public StatisticsResponse getAllStatistics() {
	        StatisticsResponse stats = new StatisticsResponse();
	        List<Order> allOrders = orderRepository.findAll();
	        
	        stats.setTotalOrders(allOrders.size());
	        
	        stats.setOrdersByStatus(countOrdersByStatus(allOrders));

	        stats.setTodaysOrders(countTodaysOrders(allOrders));
	        
	        calculateRevenue(allOrders, stats);

	        calculateBookStatistics(allOrders, stats);

	        return stats;
	    }


		private Map<String, Long> countOrdersByStatus(List<Order> orders) {
			
			return orders.stream()
					.collect(Collectors.groupingBy(order -> order.getStatus().name()
							, Collectors.counting()));
		}


		private long countTodaysOrders(List<Order> orders) {
		     LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
		        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
		        
		        return orders.stream()
		                .filter(order -> order.getCreatedAt() != null)
		                .filter(order -> {
		                    LocalDateTime createdAt = order.getCreatedAt();
		                    return !createdAt.isBefore(startOfDay) && !createdAt.isAfter(endOfDay);
		                })
		                .count();
		}


		private void calculateRevenue(List<Order> orders, StatisticsResponse stats) {
		    BigDecimal totalRevenue = BigDecimal.ZERO;
	        BigDecimal pendingRevenue = BigDecimal.ZERO;

	        for (Order order : orders) {
	            BigDecimal orderTotal = order.getTotalPrice();

	            if (order.getStatus() == Status.PICKED_UP) {
	                //  إيرادات حقيقية
	                totalRevenue = totalRevenue.add(orderTotal);
	            } else if (order.getStatus() == Status.PENDING || 
	                       order.getStatus() == Status.PRINTING || 
	                       order.getStatus() == Status.READY) {
	                //  إيرادات متوقعة
	                pendingRevenue = pendingRevenue.add(orderTotal);
	            }
	        }

	        stats.setTotalRevenue(totalRevenue);
	        stats.setPendingRevenue(pendingRevenue);			
		}


		private void calculateBookStatistics(List<Order> orders, StatisticsResponse stats) {
	       
			Map<Long, BookStats> bookStatsMap = new HashMap<>();
			
	        long totalBooksSold = 0;
	        for (Order order : orders) {
	        	
	            if (order.getStatus() == Status.CANCELLED) continue;
	            
	            if (order.getItems() == null) continue;
	            
	            for (OrderItem item : order.getItems()) {
	                if (item.getBook() == null || item.getQuantity() == 0) continue;

	                Long bookId = item.getBook().getId();
	                String bookTitle = item.getBook().getTitle();
	                int quantity = item.getQuantity();
	                BigDecimal revenue = item.getPriceAtOrder() != null 
	                        ? item.getPriceAtOrder().multiply(BigDecimal.valueOf(quantity))
	                        : BigDecimal.ZERO;
	                
	                totalBooksSold += quantity;
	                bookStatsMap.computeIfAbsent(bookId, k -> new BookStats(bookTitle))
                    .add(quantity, revenue);

	            }
	            
	        }
	        
	        stats.setTotalBooksSold(totalBooksSold);

	        List<StatisticsResponse.TopBook> topBooks = bookStatsMap.entrySet().stream()
	                .sorted((e1, e2) -> Long.compare(e2.getValue().totalQuantity, e1.getValue().totalQuantity))
	                .limit(5)
	                .map(entry -> new StatisticsResponse.TopBook(
	                        entry.getKey(),
	                        entry.getValue().bookTitle,
	                        entry.getValue().totalQuantity,
	                        entry.getValue().totalRevenue
	                ))
	                .collect(Collectors.toList());

	        stats.setTopSellingBooks(topBooks);
	    }
			
		 private static class BookStats {
		        String bookTitle;
		        long totalQuantity = 0;
		        BigDecimal totalRevenue = BigDecimal.ZERO;

		        BookStats(String bookTitle) {
		            this.bookTitle = bookTitle;
		        }

		        void add(int quantity, BigDecimal revenue) {
		            this.totalQuantity += quantity;
		            this.totalRevenue = this.totalRevenue.add(revenue);
		        }
		    }
		

	
}
