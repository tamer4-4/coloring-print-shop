package com.coloringshop.printshop.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String customerName;
    
    private String address;
   
    private String phone;
      
    public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	private String orderCode;


	private String pin;
    
    @PrePersist
    public void generatePin() {
        this.pin = String.format("%04d", new Random().nextInt(999999));
    }
    
    @Enumerated(EnumType.STRING)
    private Status status;
    
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>() ;
    
    
    
	public Order() {
		super();
	}





	public Order(Long id, String customerName, String address, String phone, Status status, LocalDateTime createdAt,
			List<OrderItem> items) {
		super();
		this.id = id;
		this.customerName = customerName;
		this.address = address;
		this.phone = phone;
		this.status = status;
		this.createdAt = createdAt;
		this.items = items;
	}





	public Long getId() {
		return id;
	}



    public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}


	public void setId(Long id) {
		this.id = id;
	}





	public String getCustomerName() {
		return customerName;
	}





	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}





	public String getAddress() {
		return address;
	}





	public void setAddress(String address) {
		this.address = address;
	}





	public String getPhone() {
		return phone;
	}





	public void setPhone(String phone) {
		this.phone = phone;
	}





	public Status getStatus() {
		return status;
	}





	public void setStatus(Status status) {
		this.status = status;
	}





	public LocalDateTime getCreatedAt() {
		return createdAt;
	}





	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}





	public List<OrderItem> getItems() {
		return items;
	}





	public void setItems(List<OrderItem> items) {
		this.items = items;
	}





	
	
}
