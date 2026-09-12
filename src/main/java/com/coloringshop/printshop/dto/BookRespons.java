package com.coloringshop.printshop.dto;

import java.math.BigDecimal;

public record BookRespons(
		Long id,
		String title,
		String description,
		BigDecimal price,
		String coverImageUrl,
		String pdfFileUrl
		)
   
 


{

}
