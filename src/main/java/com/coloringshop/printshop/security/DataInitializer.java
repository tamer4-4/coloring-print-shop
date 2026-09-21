package com.coloringshop.printshop.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.coloringshop.printshop.model.Admin;
import com.coloringshop.printshop.repository.AdminRepository;

@Component
public class DataInitializer implements CommandLineRunner {

	  private final AdminRepository adminRepository;
	    private final PasswordEncoder passwordEncoder;

	    public DataInitializer(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
	        this.adminRepository = adminRepository;
	        this.passwordEncoder = passwordEncoder;
	    }

	    @Override
	    public void run(String... args) {
	        if (adminRepository.count() == 0) {
	            Admin admin = new Admin();
	            admin.setUsername("admin");
	            admin.setPassword(passwordEncoder.encode("123")); 
	            adminRepository.save(admin);
	            System.out.println("✅ تم إنشاء أدمن افتراضي - Username: admin, Password: 123");
	        }
	    }
	
}
