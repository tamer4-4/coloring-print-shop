package com.coloringshop.printshop.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.coloringshop.printshop.model.Admin;
import com.coloringshop.printshop.repository.AdminRepository;

public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	private AdminRepository adominRepo;
	
	public UserDetailsService(AdminRepository adominRepo) {
		super();
		this.adominRepo = adominRepo;
	}




	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Admin admin  = adominRepo.findByUsername(username).orElseThrow(() ->
				new RuntimeException("User not found"));
		
		return User.withUsername(username)
				.password(admin.getPassword())
				.roles("ADMIN")
				.build();
	}

}
