package dev.backendintegratedproject;

import dev.backendintegratedproject.util.ListMapper;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendIntegratedProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendIntegratedProjectApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	@Bean
	public ListMapper listMapper() {
		return ListMapper.getListMapper();
	}

}