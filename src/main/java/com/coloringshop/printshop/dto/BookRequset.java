package com.coloringshop.printshop.dto;

import java.math.BigDecimal;

public record BookRequset(
		String title,
		String description
		,BigDecimal price
		
		) {

}
