package com.coloringshop.printshop.dto;

import org.springframework.stereotype.Component;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequset(
		
	     @NotNull(message = "يجب اختيار كتاب")
		 Long bookId,
		 
		 
		 @NotNull(message = "يجب تحديد الكمية")
		 @Min(value = 1, message = "الكمية يجب أن تكون 1 على الأقل")
	     Integer quantity
	       
		)
      
{

}
