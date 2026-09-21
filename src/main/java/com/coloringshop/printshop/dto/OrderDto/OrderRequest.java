package com.coloringshop.printshop.dto.OrderDto;

import java.util.List;

import com.coloringshop.printshop.model.OrderItem;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record OrderRequest(
	     @NotNull(message = "الاسم مطلوب")
		String customerName,
	     @NotNull(message = "العنوان مطلوب")

		String address,
		
	    @NotNull(message = "الهاتف مطلوب")
	     @Size(max = 11 , min = 11 , message = "الرقم غير صالح")
		String phone,
		
		List<OrderItemRequset> items
		
		
		)
{

}
