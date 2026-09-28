package com.coloringshop.printshop.dto.OrderDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.coloringshop.printshop.model.Status;

public record orderResponsGuest(
    Long id,
    String customerName,
    Status status,
    String orderCode,
    BigDecimal totalPrice,
    LocalDateTime createdAt,
    List<OrderItemResponse> items   
    )
    {}