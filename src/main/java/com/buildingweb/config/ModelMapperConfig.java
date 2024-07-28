package com.buildingweb.config;

import org.modelmapper.Condition;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ModelMapperConfig {
    @Bean
    @Primary
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean(name = "update")
    public ModelMapper modelMapperUpdate() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(skipNullAndEmpty());
        return modelMapper;
    }

    // Bỏ qua các giá trị null và giá trị rỗng
    @Bean
    Condition<?, ?> skipNullAndEmpty() {
        return (MappingContext<Object, Object> context) -> context.getSource() != null &&
                !(context.getSource() instanceof String && ((String) context.getSource()).trim().isEmpty());
    }
}