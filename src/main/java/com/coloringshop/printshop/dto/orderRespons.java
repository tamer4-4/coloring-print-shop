package com.coloringshop.printshop.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.coloringshop.printshop.model.Status;

public record orderRespons(
		
		 Long id,
		 
		 String customerName,
		 
		 String address,
		 
		 String phone,
		 
		 Status status,
		 
		 BigDecimal totalPrice,

		 LocalDateTime createdAt
		
		
		)

{
	

}
