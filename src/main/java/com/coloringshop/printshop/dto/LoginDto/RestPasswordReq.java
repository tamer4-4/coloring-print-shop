package com.coloringshop.printshop.dto.LoginDto;

import jakarta.validation.constraints.NotBlank;

public record RestPasswordReq(
	
		@NotBlank(message = "كلمة المرور الجديدة مطلوبة")
		String newPassword,
		@NotBlank(message = "أكد كلمة المرور ")
		String confirmPassword
		
		) {

}
