package com.coloringshop.printshop.controller;


import java.nio.file.AccessDeniedException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coloringshop.printshop.dto.OrderDto.OrderRequest;
import com.coloringshop.printshop.dto.OrderDto.orderRespons;
import com.coloringshop.printshop.excption.OrderNotFoundException;
import com.coloringshop.printshop.service.GuestOrderService;

import jakarta.validation.Valid;

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
     * @throws OrderNotFoundException 
     */
    @GetMapping("status/{orderCode}")
    public ResponseEntity<orderRespons> getOrderStatus(@PathVariable String orderCode) throws AccessDeniedException, OrderNotFoundException {
        orderRespons response = guestOrderService.getOrderStatus(orderCode);
        return ResponseEntity.ok(response);
    }
    
    /**
    * /api/v1/orders/update/{orderCode}
    * تعديل الطلب
     * @throws AccessDeniedException 
     * @throws OrderNotFoundException 
    */
    @PutMapping("/update/{orderCode}")
    public ResponseEntity<orderRespons> updateOrder(@PathVariable String orderCode, @RequestParam String pin , @RequestBody OrderRequest req) throws AccessDeniedException, OrderNotFoundException {
        orderRespons response = guestOrderService.updateOrder(orderCode , pin , req);
        return ResponseEntity.ok(response);
    }
    
    
    /**
     * /api/v1/orders/delete/{orderCode}
     * حذف الطلب
      * @throws AccessDeniedException 
     * @throws OrderNotFoundException 
     */
     @DeleteMapping("/delete/{orderCode}")
     public ResponseEntity<Void> deleteOrder(@PathVariable String orderCode, @RequestParam String pin) throws AccessDeniedException, OrderNotFoundException {
         guestOrderService.deleteOrder(orderCode , pin);
         return ResponseEntity.noContent().build();
     }
    
}
