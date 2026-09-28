package com.coloringshop.printshop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coloringshop.printshop.dto.LoginDto.LoginRequest;
import com.coloringshop.printshop.dto.LoginDto.LoginResponse;
import com.coloringshop.printshop.dto.LoginDto.RestPasswordReq;
import com.coloringshop.printshop.dto.LoginDto.RestPasswordResons;
import com.coloringshop.printshop.excption.AdminNotFoundException;
import com.coloringshop.printshop.model.Admin;
import com.coloringshop.printshop.repository.AdminRepository;
import com.coloringshop.printshop.security.JwtUtils;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;
	private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;



	public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils,
			AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
		super();
		this.authenticationManager = authenticationManager;
		this.jwtUtils = jwtUtils;
		this.adminRepository = adminRepository;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * POST /api/auth/login تسجيل دخول الأدمن
	 */
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			String token = jwtUtils.generateToken(authentication);
			return ResponseEntity.ok(new LoginResponse(token, request.username()));

		} catch (BadCredentialsException e) {
			System.err.println("❌ BadCredentialsException: " + e.getMessage());
			return ResponseEntity.badRequest().body("❌ اسم المستخدم أو كلمة المرور غير صحيحة");
		}
	}

	/**
	 * POST /api/auth/restpass تعديل الباسورد
	 * @throws AdminNotFoundException 
	 */
	@PostMapping("/restpass")
	public ResponseEntity<?> restPassword(@Valid @RequestBody RestPasswordReq request) throws AdminNotFoundException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User admin = (User) auth.getPrincipal();
		
		if (!(request.newPassword().equals(request.confirmPassword()))) {
			return ResponseEntity.badRequest().body("❌ كلمة المرور غير صحيحة الجديدة");
		}


			Admin newAdmin = adminRepository.findByUsername(admin.getUsername())
					.orElseThrow(()-> new AdminNotFoundException("الادمن دا غير موجود" + admin.getUsername()));
			newAdmin.setPassword(passwordEncoder.encode(request.newPassword()));
			adminRepository.save(newAdmin);
			String message = "تم تغيير كلمة المرور بنجاح";

			return ResponseEntity.ok(new RestPasswordResons(message));

	
	}

}
