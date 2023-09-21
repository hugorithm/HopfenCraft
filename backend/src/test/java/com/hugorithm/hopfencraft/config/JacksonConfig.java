package com.hugorithm.hopfencraft.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hugorithm.hopfencraft.utils.PageModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();
        objectMapper.registerModule(new PageModule());
        return objectMapper;
    }
}
