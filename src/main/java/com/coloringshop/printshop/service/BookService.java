package com.coloringshop.printshop.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.coloringshop.printshop.model.Book;
import com.coloringshop.printshop.repository.BookRepository;

@Service
public class BookService {

	
	 private final BookRepository bookrepo;

	    public BookService(BookRepository bookrepo) {
	        this.bookrepo = bookrepo;
	    }
	    
	    
	public List<Book> getAllBooks() {
	 List<Book>	books =  bookrepo.findAll();
		return books;
	}
	
	public Map<String , Object> getAllBooks(Pageable pageable){
		
	   Page<Book> books = 	bookrepo.findAll(pageable);
	   
	   
	   
       Map<String, Object> response = new HashMap<>();
       
       
       
       response.put("Books", books.getContent());
       response.put("currentPage", books.getNumber());
       response.put("totalItems", books.getTotalElements());
       response.put("totalPages", books.getTotalPages());
       response.put("hasNext", books.hasNext());
       response.put("hasPrevious", books.hasPrevious());
       
       return response;
	}

}
