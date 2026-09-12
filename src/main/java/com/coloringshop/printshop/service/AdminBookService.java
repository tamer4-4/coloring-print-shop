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

import com.coloringshop.printshop.dto.BookRespons;
import com.coloringshop.printshop.model.Book;
import com.coloringshop.printshop.repository.BookRepository;
import com.coloringshop.printshop.repository.OrderRepository;

@Service
public class AdminBookService {

	private BookRepository bookRepository;
	private OrderRepository orderRepository;
	private FileUploadService fileUploadService;

	public AdminBookService(BookRepository bookRepository, OrderRepository orderRepository,
			FileUploadService fileUploadService) {
		super();
		this.bookRepository = bookRepository;
		this.orderRepository = orderRepository;
		this.fileUploadService = fileUploadService;
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

	public BookRespons addBook(String title, String description, BigDecimal price, MultipartFile pdfFile,
			MultipartFile coverImage) throws IOException {

		Book book = new Book();
		book.setTitle(title);
		book.setDescription(description);
		book.setPrice(price);

		if (coverImage != null && !coverImage.isEmpty()) {
			// TODO: احذف الصورة القديمة من السيرفر
			String coverUrl = fileUploadService.uploadCoverImage(coverImage);
			book.setCoverImageUrl(coverUrl);
		}

		if (pdfFile != null && !pdfFile.isEmpty()) {
			// TODO: احذف ملف PDF القديم
			String pdfUrl = fileUploadService.uploadPdf(pdfFile);
			book.setPdfFileUrl(pdfUrl);
		}
              Book savedBook = bookRepository.save(book);
              
		return new BookRespons(savedBook.getId(), savedBook.getTitle(), savedBook.getDescription(), savedBook.getPrice(),
				savedBook.getCoverImageUrl(), savedBook.getPdfFileUrl());
	}

	public BookRespons updateBook(Long id, String title, String description, BigDecimal price, MultipartFile coverImage,
			MultipartFile pdfFile) throws IOException {

		Book oldBook = bookRepository.findById(id).orElseThrow(() -> new RuntimeException("غير موجود الكتاب"));

		oldBook.setTitle(title);
		oldBook.setDescription(description);
		oldBook.setPrice(price);

		if (coverImage != null && !coverImage.isEmpty()) {
			// TODO: احذف الصورة القديمة من السيرفر
			String coverUrl = fileUploadService.uploadCoverImage(coverImage);
			oldBook.setCoverImageUrl(coverUrl);
		}

		if (pdfFile != null && !pdfFile.isEmpty()) {
			// TODO: احذف ملف PDF القديم
			String pdfUrl = fileUploadService.uploadPdf(pdfFile);
			oldBook.setPdfFileUrl(pdfUrl);
		}

		Book book = bookRepository.save(oldBook);
		return new BookRespons(book.getId(), book.getTitle(), book.getDescription(), book.getPrice(),
				book.getCoverImageUrl(), book.getPdfFileUrl());

	}

}
