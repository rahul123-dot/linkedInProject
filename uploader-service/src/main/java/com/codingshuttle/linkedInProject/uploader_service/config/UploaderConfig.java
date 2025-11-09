package com.codingshuttle.linkedInProject.uploader_service.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class UploaderConfig {

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecrete;

    @Bean
    public Cloudinary cloudinary(){

        Map<String,String> config  = Map.of(

                "cloud_name",cloudName,
                "api_key",apiKey,
                "api_secret",apiSecrete
        );
        return new Cloudinary(config);
    }
}
