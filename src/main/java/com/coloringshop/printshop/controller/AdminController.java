package com.coloringshop.printshop.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.coloringshop.printshop.dto.BookDto.BookRequset;
import com.coloringshop.printshop.dto.BookDto.BookRespons;
import com.coloringshop.printshop.dto.OrderDto.OrderRequest;
import com.coloringshop.printshop.dto.OrderDto.orderRespons;
import com.coloringshop.printshop.excption.OrderNotFoundException;
import com.coloringshop.printshop.model.Book;
import com.coloringshop.printshop.model.Order;
import com.coloringshop.printshop.model.Status;
import com.coloringshop.printshop.repository.BookRepository;
import com.coloringshop.printshop.repository.OrderRepository;
import com.coloringshop.printshop.service.AdminBookService;
import com.coloringshop.printshop.service.AdminOrderService;
import com.coloringshop.printshop.service.FileUploadService;
import com.coloringshop.printshop.service.GuestOrderService;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;



@RestController
@RequestMapping("api/v1/admin")
public class AdminController {
 

    private final AdminBookService bookService;
    private final FileUploadService fileUploadService;
    private final AdminOrderService adminOrderService;
    private final GuestOrderService guestOrderService;
      


    
   
    public AdminController(AdminBookService bookService, FileUploadService fileUploadService,
			AdminOrderService adminOrderService, GuestOrderService guestOrderService) {
		super();
		this.bookService = bookService;
		this.fileUploadService = fileUploadService;
		this.adminOrderService = adminOrderService;
		this.guestOrderService = guestOrderService;
	}


	/**
     * إضافة كتاب جديد مع رفع الملفات
     * POST /api/v1/admin/books
     * 
     * Content-Type: multipart/form-data
     */
	  @PostMapping(value = "/books" , consumes = "multipart/form-data")
	    public ResponseEntity<BookRespons> addBook(
	            @RequestParam("title") String title,
	            @RequestParam("description") String description,
	            @RequestParam("price") BigDecimal price,
	            @RequestParam(value = "coverImage", required = false) MultipartFile coverImage,
	            @RequestParam(value = "pdfFile", required = false) MultipartFile pdfFile
	    ) {
                   	        
	    BookRespons savedBook;
		try {
			savedBook = bookService
					.addBook(title, description, price, pdfFile, coverImage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	        return ResponseEntity.ok(savedBook);
	    }
	  
	   
	  /**
	     * تعدل كتاب جديد مع رفع الملفات
	     * POST /api/v1/admin/books/{bookId}
	     * 
	     * Content-Type: multipart/form-data
	     */
	  @PutMapping(value = "/books/{bookId}" , consumes = "multipart/form-data")
	    public ResponseEntity<BookRespons> updateBook(
	    		@PathVariable Long bookId,
	            @RequestParam("title") String title,
	            @RequestParam("description") String description,
	            @RequestParam("price") BigDecimal price,
	            @RequestParam(value = "coverImage", required = false) MultipartFile coverImage,
	            @RequestParam(value = "pdfFile", required = false) MultipartFile pdfFile
	    ) {
                 	        
	    BookRespons savedBook;
		try {
			savedBook = bookService
		       .updateBook(bookId 
							,title
							, description
							, price
							, coverImage
							, pdfFile );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
		
	        return ResponseEntity.ok(savedBook);
	        
	    }

	  /**
	     * حذف كتاب مع  الملفات
	     * POST /api/v1/admin/books/delete/{bookId}
	     
	     */
	  @DeleteMapping("/books/delete/{bookId}")
	  public ResponseEntity<Void> delteBook(@PathVariable Long bookId ){
		  bookService.deleteBook(bookId);
		  return ResponseEntity.noContent().build();
	  }
	  
	  
	  // ====================Orders===========================
	  
		/**
	     * GET /api/v1/admin/orders
	     * عرض كل الطلبات للأدمن
	     */
	    @GetMapping("/orders")
	    public ResponseEntity<List<orderRespons>> getAllOrders() {
	        return ResponseEntity.ok(adminOrderService.getAllOrders());
	    }
	    
	    @PostMapping("/orders/add")
	    public ResponseEntity<orderRespons> createOrder(@Valid @RequestBody OrderRequest req){
	    	orderRespons ordrRespons =   guestOrderService.createOrder(req);
	    	return ResponseEntity.ok(ordrRespons);
	    }
	    
	    @PutMapping("/orders/update/{orderCode}")
	    public ResponseEntity<orderRespons> updateOrder(@PathVariable String orderCode,
	    		@RequestBody OrderRequest req) throws AccessDeniedException, OrderNotFoundException {
	        orderRespons response = adminOrderService.updateOrder(orderCode , req);
	        return ResponseEntity.ok(response);
	    }
	    
	    @DeleteMapping("/orders/delete/{orderCode}")
	    public ResponseEntity<Void> DeleteOrder(@PathVariable String orderCode) throws AccessDeniedException, OrderNotFoundException {
	        adminOrderService.deleteOrder(orderCode);
	        return ResponseEntity.noContent().build();
	    }	  
	  
	    @PutMapping("/orders/{status}")
	    public ResponseEntity<?> updateStatus(@PathVariable Status status ,
	    		@RequestParam String orderCode ) throws OrderNotFoundException{
	        orderRespons ordre = adminOrderService.getOrderByCode(orderCode , status);
	       
           return ResponseEntity.ok(ordre);   	
	    }
	  
	
	
}
