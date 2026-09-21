package com.coloringshop.printshop.dto.BookDto;

import java.math.BigDecimal;
import java.util.UUID;

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
