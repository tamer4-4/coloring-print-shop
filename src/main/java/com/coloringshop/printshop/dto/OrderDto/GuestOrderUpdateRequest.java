package com.coloringshop.printshop.dto.OrderDto;

import java.util.List;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record GuestOrderUpdateRequest(
    @NotNull(message = "الـ PIN مطلوب")
    String pin,

    @NotNull(message = "العنوان مطلوب")
    String address,

    @NotNull(message = "الهاتف مطلوب")
    @Size(min = 11, max = 11, message = "الرقم غير صالح")
    String phone,

    @NotEmpty(message = "الطلب لازم يحتوي على كتاب واحد على الأقل")
    List<OrderItemRequset> items    // ✅ بنعيد استخدام الـ DTO الموجود
) {}