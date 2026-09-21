package com.coloringshop.printshop.security;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.coloringshop.printshop.model.Admin;
import com.coloringshop.printshop.repository.AdminRepository;

@Service
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {

	private AdminRepository adominRepo;
	
	public UserDetailsServiceImpl(AdminRepository adominRepo) {
		super();
		this.adominRepo = adominRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Admin admin  = adominRepo.findByUsername(username).orElseThrow(() ->
				new UsernameNotFoundException("User not found"));
		
		return new User(
                admin.getUsername(),
                admin.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
	}

}
