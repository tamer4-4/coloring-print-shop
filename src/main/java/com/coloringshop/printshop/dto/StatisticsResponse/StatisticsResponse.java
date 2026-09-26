package com.coloringshop.printshop.dto.StatisticsResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
public class StatisticsResponse {
	  private long totalOrders;
	    private Map<String, Long> ordersByStatus; // {"PENDING": 5, "READY": 12, ...}
	    private long todaysOrders;
	    
	    
	    private BigDecimal totalRevenue;      // إيرادات الطلبات المكتملة
	    private BigDecimal pendingRevenue;    // إيرادات الطلبات تحت التنفيذ

	    
	    private long totalBooksSold;
	    private List<TopBook> topSellingBooks;
	    
	    
	    public static class TopBook {
	        private Long bookId;
	        private String bookTitle;
	        private long totalSold;
	        private BigDecimal totalRevenue;

	        public TopBook(Long bookId, String bookTitle, long totalSold, BigDecimal totalRevenue) {
	            this.bookId = bookId;
	            this.bookTitle = bookTitle;
	            this.totalSold = totalSold;
	            this.totalRevenue = totalRevenue;
	        }

	        // Getters
	        public Long getBookId() { return bookId; }
	        public String getBookTitle() { return bookTitle; }
	        public long getTotalSold() { return totalSold; }
	        public BigDecimal getTotalRevenue() { return totalRevenue; }
	    }
	    
	    public long getTotalOrders() { return totalOrders; }
	    public void setTotalOrders(long totalOrders) { this.totalOrders = totalOrders; }

	    public Map<String, Long> getOrdersByStatus() { return ordersByStatus; }
	    public void setOrdersByStatus(Map<String, Long> ordersByStatus) { this.ordersByStatus = ordersByStatus; }

	    public long getTodaysOrders() { return todaysOrders; }
	    public void setTodaysOrders(long todaysOrders) { this.todaysOrders = todaysOrders; }

	    public BigDecimal getTotalRevenue() { return totalRevenue; }
	    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

	    public BigDecimal getPendingRevenue() { return pendingRevenue; }
	    public void setPendingRevenue(BigDecimal pendingRevenue) { this.pendingRevenue = pendingRevenue; }

	    public long getTotalBooksSold() { return totalBooksSold; }
	    public void setTotalBooksSold(long totalBooksSold) { this.totalBooksSold = totalBooksSold; }

	    public java.util.List<TopBook> getTopSellingBooks() { return topSellingBooks; }
	    public void setTopSellingBooks(java.util.List<TopBook> topSellingBooks) { this.topSellingBooks = topSellingBooks; }
	}
