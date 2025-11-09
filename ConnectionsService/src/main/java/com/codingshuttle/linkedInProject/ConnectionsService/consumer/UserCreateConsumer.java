package com.codingshuttle.linkedInProject.ConnectionsService.consumer;

import com.codingshuttle.linkedInProject.ConnectionsService.entity.Person;
import com.codingshuttle.linkedInProject.ConnectionsService.service.PersonService;
import com.codingshuttle.linkedInProject.userService.event.UserCreated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserCreateConsumer {

    private final PersonService personService;

    @KafkaListener(topics = "User_Created_Topic")
    public void userCreatedConsumer(UserCreated userCreated){
        log.info("handle userCreated {}",userCreated);
        personService.createPerson(userCreated.getUserId(),userCreated.getName());
    }

}
