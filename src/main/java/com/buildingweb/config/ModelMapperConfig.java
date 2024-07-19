package com.buildingweb.config;

import org.modelmapper.Condition;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    // Bỏ qua các giá trị null và giá trị rỗng
    @Bean
    Condition<?, ?> skipNullAndEmpty() {
        return new Condition<Object, Object>() {
            @Override
            public boolean applies(MappingContext<Object, Object> context) {
                return context.getSource() != null &&
                        !(context.getSource() instanceof String && ((String) context.getSource()).trim().isEmpty());
            }
        };
    }
}