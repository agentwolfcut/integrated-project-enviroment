package dev.backendintegratedproject;

import dev.backendintegratedproject.dtos.PutTaskDTO;
import dev.backendintegratedproject.entities.StatusEntity;
import dev.backendintegratedproject.services.ListMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ApplicationConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Customize mapping for PutTaskDTO
        modelMapper.createTypeMap(StatusEntity.class, PutTaskDTO.class)
                .addMappings(mapper -> mapper.map(src -> src.getName(), PutTaskDTO::setStatus));

        return modelMapper;
    }
    @Bean
    public ListMapper listMapper() { return new ListMapper();}


}


