package com.coloringshop.printshop.dto.OrderDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.coloringshop.printshop.model.Status;

public record orderResponsGuestCreate( Long id,
	    String customerName,
	    Status status,
	    String orderCode,
	    String pin,
	    BigDecimal totalPrice,
	    LocalDateTime createdAt,
	    List<OrderItemResponse> items  ) {

}
