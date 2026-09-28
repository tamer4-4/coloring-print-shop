package com.coloringshop.printshop.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.coloringshop.printshop.dto.BookDto.BookRespons;
import com.coloringshop.printshop.excption.BookNotFoundException;
import com.coloringshop.printshop.model.Book;
import com.coloringshop.printshop.repository.BookRepository;

import jakarta.transaction.Transactional;

@Service
public class AdminBookService {

	private BookRepository bookRepository;
	private final R2Service r2Service;

	public AdminBookService(BookRepository bookRepository,  R2Service r2Service) {
		super();
		this.bookRepository = bookRepository;
		this.r2Service = r2Service;
	}

	public List<Book> getAllBooks() {
		List<Book> books = bookRepository.findAll();
		return books;
	}

	public Map<String, Object> getAllBooks(Pageable pageable) {

		Page<Book> books = bookRepository.findAll(pageable);

		Map<String, Object> response = new HashMap<>();

		response.put("Books", books.getContent());
		response.put("currentPage", books.getNumber());
		response.put("totalItems", books.getTotalElements());
		response.put("totalPages", books.getTotalPages());
		response.put("hasNext", books.hasNext());
		response.put("hasPrevious", books.hasPrevious());

		return response;
	}

	public BookRespons addBook(String title, String description, BigDecimal price, 
			String coverImage, String pdfFileUrl) throws IOException {

		Book book = new Book();
		book.setTitle(title);
		book.setDescription(description);
		book.setPrice(price);

		if (coverImage != null && !coverImage.isBlank()) {
			book.setCoverImageUrl(coverImage);
		}

		if (pdfFileUrl != null && !pdfFileUrl.isBlank()) {
			book.setPdfFileUrl(pdfFileUrl);
		} 

		Book savedBook = bookRepository.save(book);

		return new BookRespons(savedBook.getId(), savedBook.getTitle(), savedBook.getDescription(),
				savedBook.getPrice(), savedBook.getCoverImageUrl(), savedBook.getPdfFileUrl());
	}

	@Transactional
	public BookRespons updateBook(Long id, String title, String description, BigDecimal price,
	                              String pdfFile, String coverImage
	                             ) throws IOException {

	    Book book = bookRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("الكتاب غير موجود"));

	    book.setTitle(title);
	    book.setDescription(description);
	    book.setPrice(price);

	    // 🖼️ الصورة الغلاف: لو فيه صورة جديدة، استبدل القديم
	    if (coverImage != null && !coverImage.isEmpty()) {
	        // TODO: احذف الصورة القديمة من Cloudinary لو موجودة
	        book.setCoverImageUrl(coverImage);
	    }

	    if (pdfFile != null && !pdfFile.isBlank()) {
	        book.setPdfFileUrl(pdfFile);
	    } 
	    // لو الاتنين فاضيين = محتفظ بالـ pdfFileUrl القديم ✅

	    Book updatedBook = bookRepository.save(book);
	    
	    return new BookRespons(updatedBook.getId(), updatedBook.getTitle(),
	                          updatedBook.getDescription(), updatedBook.getPrice(),
	                          updatedBook.getCoverImageUrl(), updatedBook.getPdfFileUrl());
	}
	
	
	
	public void deleteBook(Long id) throws BookNotFoundException {
		Book book = bookRepository.findById(id)
				.orElseThrow(() -> new BookNotFoundException("غير موجود الكتاب : " + id));

		bookRepository.delete(book);
	}

}
