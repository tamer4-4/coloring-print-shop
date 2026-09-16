package com.coloringshop.printshop.controller;


import jakarta.validation.Valid;

import java.nio.file.AccessDeniedException;

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
     * GET /api/v1/orders/status/{orderCode}
     * الاستعلام عن حالة الطلب
     * @throws AccessDeniedException 
     */
    @GetMapping("status/{orderCode}")
    public ResponseEntity<orderRespons> getOrderStatus(@PathVariable String orderCode) throws AccessDeniedException {
        orderRespons response = guestOrderService.getOrderStatus(orderCode);
        return ResponseEntity.ok(response);
    }
    
    /**
    * /api/v1/orders/update/{orderCode}
    * تعديل الطلب
     * @throws AccessDeniedException 
    */
    @PutMapping("/update/{orderCode}")
    public ResponseEntity<orderRespons> updateOrder(@PathVariable String orderCode, @RequestParam String pin , @RequestBody OrderRequest req) throws AccessDeniedException {
        orderRespons response = guestOrderService.updateOrder(orderCode , pin , req);
        return ResponseEntity.ok(response);
    }
    
    
    /**
     * /api/v1/orders/delete/{orderCode}
     * حذف الطلب
      * @throws AccessDeniedException 
     */
     @DeleteMapping("/delete/{orderCode}")
     public ResponseEntity<Void> updateOrder(@PathVariable String orderCode, @RequestParam String pin) throws AccessDeniedException {
         guestOrderService.deleteOrder(orderCode , pin);
         return ResponseEntity.noContent().build();
     }
    
}
