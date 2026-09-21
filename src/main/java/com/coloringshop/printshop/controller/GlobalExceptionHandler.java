package com.coloringshop.printshop.controller;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.coloringshop.printshop.excption.AdminNotFoundException;
import com.coloringshop.printshop.excption.BookNotFoundException;
import com.coloringshop.printshop.excption.OrderNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
	public ProblemDetail handleAccessDeniedException(AccessDeniedException ex ) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, 
				"Validation Failed");
		
		Map<String, Object> errMap = new HashMap<>();
		errMap.put("erorr", ex.getMessage());
		errMap.put("timestamp", LocalDateTime.now());
		problemDetail.setProperty("errors", errMap);
				
		return problemDetail;
		}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handelValideException(MethodArgumentNotValidException ex) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, 
				"Validation Failed");
		
		Map<String, Object> errMap = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(err ->{
			errMap.put(err.getField() , err.getDefaultMessage());
		});
		problemDetail.setProperty("errors", errMap);
		
		return problemDetail;
		
	}
	
	
	@ExceptionHandler(BookNotFoundException.class)
	public ProblemDetail handelOrderException(BookNotFoundException ex ) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, 
				"Book Failed");
		
		Map<String, Object> errMap = new HashMap<>();
		errMap.put("erorr", ex.getMessage());
		errMap.put("timestamp", LocalDateTime.now());
		problemDetail.setProperty("errors", errMap);
		
		return problemDetail;
		
	}

	
	@ExceptionHandler(OrderNotFoundException.class)
	public ProblemDetail handelOrderException(OrderNotFoundException ex ) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, 
				"Order Failed");
		
		Map<String, Object> errMap = new HashMap<>();
		errMap.put("erorr", ex.getMessage());
		errMap.put("timestamp", LocalDateTime.now());
		problemDetail.setProperty("errors", errMap);
		
		return problemDetail;
		
	}

	
	@ExceptionHandler(AdminNotFoundException.class)
	public ProblemDetail handelAdminException(AdminNotFoundException ex ) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, 
				"Admin Failed");
		
		Map<String, Object> errMap = new HashMap<>();
		errMap.put("erorr", ex.getMessage());
		errMap.put("timestamp", LocalDateTime.now());
		problemDetail.setProperty("errors", errMap);
		
		return problemDetail;
		
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ProblemDetail handleUsernameFailedLoginException(BadCredentialsException ex, WebRequest request) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, 
				"BadCredentials Failed Login");
		
		Map<String, Object> errMap = new HashMap<>();
		errMap.put("erorr", ex.getMessage());
		errMap.put("timestamp", LocalDateTime.now());
		problemDetail.setProperty("errors", errMap);
		
		return problemDetail;
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public ProblemDetail handleUsernameFailedLoginException(UsernameNotFoundException ex, WebRequest request) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, 
				"User Failed Login");
		
		Map<String, Object> errMap = new HashMap<>();
		errMap.put("erorr", ex.getMessage());
		errMap.put("timestamp", LocalDateTime.now());
		problemDetail.setProperty("errors", errMap);
		
		return problemDetail;
	}
}
