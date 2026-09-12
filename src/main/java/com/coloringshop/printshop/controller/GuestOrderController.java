package com.coloringshop.printshop.controller;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.coloringshop.printshop.dto.OrderRequest;
import com.coloringshop.printshop.dto.orderRespons;
import com.coloringshop.printshop.service.GuestOrderService;

@RestController
@RequestMapping("/api/v1/orders")
public class GuestOrderController {

    private final GuestOrderService guestOrderService;

    public GuestOrderController(GuestOrderService guestOrderService) {
        this.guestOrderService = guestOrderService;
    }

    /**
     * POST /api/v1/orders/guest
     * إنشاء طلب جديد كضيف
     */
    @PostMapping("/guest")
    public ResponseEntity<orderRespons> createGuestOrder(
            @Valid @RequestBody OrderRequest request) {
        
        orderRespons response = guestOrderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/v1/orders/{orderId}/status
     * الاستعلام عن حالة الطلب
     */
    @GetMapping("/{orderId}/status")
    public ResponseEntity<orderRespons> getOrderStatus(@PathVariable Long orderId) {
        orderRespons response = guestOrderService.getOrderStatus(orderId);
        return ResponseEntity.ok(response);
    }
}
