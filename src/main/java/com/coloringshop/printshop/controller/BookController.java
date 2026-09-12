package com.coloringshop.printshop.controller;

import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coloringshop.printshop.service.AdminBookService;

@RestController
@RequestMapping("api/v1/books")
public class BookController {
	
	private AdminBookService bookService;
	
	
	public BookController(AdminBookService bookService) {
		super();
		this.bookService = bookService;
	}


	@GetMapping()
	public ResponseEntity<?> getAllBook(
			    @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "10") int size,
	            @RequestParam(defaultValue = "title") String sortBy,
	            @RequestParam(defaultValue = "DESC") String sortDir
			){
		
		      Sort sort = sortDir.equalsIgnoreCase("ASC")?
   		  	  Sort.by(sortBy).ascending()
   			  : Sort.by(sortBy).descending();
		
		Pageable pageable =  PageRequest.of(page, size, sort);
		
	   Map<String , Object> books =	bookService.getAllBooks(pageable);
		return ResponseEntity.ok(books);
	}
	

}
