package com.codingshuttle.linkedInProject.ConnectionsService.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {


    @Bean
    public NewTopic sendConnectionRequest(){
        return new NewTopic("Connection-Request-Topic",3,(short) 1);
    }

    @Bean
    public NewTopic acceptConnectionRequest(){
        return new NewTopic("Connection-Request-Accept-Topic",3,(short) 1);
    }

    @Bean
    public NewTopic rejectConnectionRequest(){
        return new NewTopic("Connection-Request-Reject-Topic",3,(short) 1);
    }
}