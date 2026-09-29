package com.coloringshop.printshop.mapper;

import java.util.List;

import com.coloringshop.printshop.dto.OrderDto.OrderItemResponse;
import com.coloringshop.printshop.model.OrderItem;

public class toRespons {
	
	
	public static List<OrderItemResponse> orderItermToRespons(List<OrderItem> orderItems) {
		return orderItems
				.stream().
				map(item -> new OrderItemResponse(
			                            item.getBook().getId(),
			                            item.getBook().getTitle(),
			                            item.getBook().getCoverImageUrl(),
			                            item.getBook().getPdfFileUrl(),
			                            item.getQuantity(),
			                            item.getPriceAtOrder()          // لو مفيش عمود price في OrderItem استخدم: item.getBook().getPrice()
			                    ))
			                    .toList();
		
	}

}
