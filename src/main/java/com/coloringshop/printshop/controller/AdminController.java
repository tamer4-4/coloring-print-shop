package com.coloringshop.printshop.controller;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.coloringshop.printshop.dto.BookRequset;
import com.coloringshop.printshop.dto.BookRespons;
import com.coloringshop.printshop.model.Book;
import com.coloringshop.printshop.repository.BookRepository;
import com.coloringshop.printshop.repository.OrderRepository;
import com.coloringshop.printshop.service.AdminBookService;
import com.coloringshop.printshop.service.FileUploadService;



@RestController
@RequestMapping("api/v1/admin")
public class AdminController {
 

    private final AdminBookService bookService;
    private final FileUploadService fileUploadService;

    
    
    public AdminController(AdminBookService bookService, 
                              FileUploadService fileUploadService) {
        this.bookService = bookService;
        this.fileUploadService = fileUploadService;
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
	
	
}
