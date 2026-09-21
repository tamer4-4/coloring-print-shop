package com.coloringshop.printshop.dto.BookDto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BookRequset(
		@NotBlank(message = "اسم الكتاب مطلوب")
		String title,
		@Size(max = 100 , min = 5)
		String description,
		@NotBlank(message = "سعر الكتاب مطلوب")
		BigDecimal price
		
		) {

}
