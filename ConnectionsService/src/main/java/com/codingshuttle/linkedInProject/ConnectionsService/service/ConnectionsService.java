package com.codingshuttle.linkedInProject.ConnectionsService.service;

import com.codingshuttle.linkedInProject.ConnectionsService.entity.Person;
import com.codingshuttle.linkedInProject.ConnectionsService.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionsService {

    private final PersonRepository personRepository;

    public List<Person> getFirstDegreeConnectionOfUser(Long userId){
        log.info("Getting firs degree connections of user : "+userId);

        return personRepository.getFirstDegreeConnections(userId);

    }
}
