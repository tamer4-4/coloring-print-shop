package com.coloringshop.printshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


	@Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html"
        );
    }
	
	@Bean
	public WebMvcConfigurer resourceConfigurer(@Value("${app.upload.cover-dir}") String coverDir, 
	                                           @Value("${app.upload.pdf-dir}") String pdfDir) {
	    return new WebMvcConfigurer() {
	        @Override
	        public void addResourceHandlers(ResourceHandlerRegistry registry) {
	            // ربط مسار /uploads/covers/ بالمجلد الفعلي للصور
	            registry.addResourceHandler("/uploads/covers/**")
	                    .addResourceLocations("file:" + coverDir + "/covers/");
	            
	            // ربط مسار /uploads/pdfs/ بالمجلد الفعلي للـ PDF
	            registry.addResourceHandler("/uploads/pdfs/**")
	                    .addResourceLocations("file:" + pdfDir + "/pdfs/");
	        }
	    };
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) 
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/books/**" ).permitAll()
                .requestMatchers("/api/v1/orders/**" ).permitAll()
                .requestMatchers("/api/v1/admin/**").permitAll()
                .requestMatchers("/h2-console/**" ).permitAll()
                .anyRequest().authenticated()
                
            )
		.httpBasic(Customizer.withDefaults());


        return http.build();
    }
	
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // تشفير الباسوردات القوي
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
	
	
}

