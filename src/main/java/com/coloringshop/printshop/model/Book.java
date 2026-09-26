package com.coloringshop.printshop.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   
   private String title;
   private String description;
   private BigDecimal price;
   @Column(nullable = true)
   private String coverImageUrl;
   @Column(nullable = true)
   private String pdfFileUrl;
   
   
   
   public Book() {
	super();
}

   public Book(Long id, String title, String description, BigDecimal price, String coverImageUrl, String pdfFileUrl) {
	super();
	this.id = id;
	this.title = title;
	this.description = description;
	this.price = price;
	this.coverImageUrl = coverImageUrl;
	this.pdfFileUrl = pdfFileUrl;
}
   
   public Long getId() {
	return id;
   }
   public void setId(Long id) {
	this.id = id;
   }
   public String getTitle() {
	return title;
   }
   public void setTitle(String title) {
	this.title = title;
   }
   public String getDescription() {
	return description;
   }
   public void setDescription(String description) {
	this.description = description;
   }
   public BigDecimal getPrice() {
	return price;
   }
   public void setPrice(BigDecimal price) {
	this.price = price;
   }
   public String getCoverImageUrl() {
	return coverImageUrl;
   }
   public void setCoverImageUrl(String coverImageUrl) {
	this.coverImageUrl = coverImageUrl;
   }
   public String getPdfFileUrl() {
	return pdfFileUrl;
   }
   public void setPdfFileUrl(String pdfFileUrl) {
	this.pdfFileUrl = pdfFileUrl;
   }
   
	
}
