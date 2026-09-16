package com.coloringshop.printshop.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.coloringshop.printshop.model.Status;

public record orderRespons(
		
		 Long id,
		 
		 String customerName,
		 
		 String address,
		 
		 String phone,
		 
		 Status status,
		 
		 String pin,
		 
		 String orderCode,
		 
		 BigDecimal totalPrice,

		 LocalDateTime createdAt
		
		
		)

{
	

}
