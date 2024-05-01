package dev.backendintegratedproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BackendIntegratedProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendIntegratedProjectApplication.class, args);
	}

//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/itb-kk/v1/**").allowedOrigins("http://localhost:5173",
//						"ip23kk3@sit.kmutt.ac.th");
//			}
//		};
//	}

}
