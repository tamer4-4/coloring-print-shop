package com.coloringshop.printshop.dto;

import java.util.List;

import com.coloringshop.printshop.model.OrderItem;


public record OrderRequest(
		String customerName,
		String address,
		String phone,
		List<OrderItemRequset> items
		
		
		)
{

}
