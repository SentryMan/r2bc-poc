package com.jojo.r2bc.poc.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;

@Configuration
public class ApplicationConfig {

  @Bean
  Jackson2ObjectMapperBuilderCustomizer mapperconfig() {

    return b ->
        b.featuresToEnable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
            .visibility(PropertyAccessor.FIELD, Visibility.ANY);
  }
}
