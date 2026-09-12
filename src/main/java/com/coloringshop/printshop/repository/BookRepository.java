package com.coloringshop.printshop.repository;

import org.hibernate.query.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.coloringshop.printshop.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
  
}
