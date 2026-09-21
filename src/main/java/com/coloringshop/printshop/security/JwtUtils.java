package com.coloringshop.printshop.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
	
	@Value("${app.jwt.secret}")
	private String jwtSecret;
	
	@Value("${app.jwt.expiration-ms}")
	private int jwtExpirationMs;
	
	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
	}
	
	public String generateToken(Authentication authentication) {
	  UserDetails details = (UserDetails)  authentication.getPrincipal();
	
	  Date now = new Date();
	  Date expiryDate = new Date(now.getTime() + jwtExpirationMs);
	  
	  System.out.println("getSigningKey====> " +getSigningKey());
	  
	  return Jwts.builder()
			  .subject(details.getUsername())
			  .issuedAt(now)
			  .expiration(expiryDate)
			  .signWith(getSigningKey())
			  .compact();
	}
	
    public String getUsernameFromToken(String token) {
           return Jwts.parser()
        		   .verifyWith(getSigningKey())
        		   .build()
        		   .parseSignedClaims(token)
        		   .getPayload()
        		   .getSubject();
    }
    
    public boolean validateToken(String token) {
     try {
    	 Jwts.parser()
    	 .verifyWith(getSigningKey())
    	 .build()
    	 .parseSignedClaims(token);
		return true;
	} catch (JwtException e) {
		  System.err.println("❌ Invalid JWT: " + e.getMessage());
          return false;
	}
    	
    }

}
