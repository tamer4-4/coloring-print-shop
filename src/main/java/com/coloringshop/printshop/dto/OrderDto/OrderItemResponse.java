package com.coloringshop.printshop.dto.OrderDto;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long bookId,
        String bookTitle,
        String coverImageUrl,
        Integer quantity,
        BigDecimal price
) {
}