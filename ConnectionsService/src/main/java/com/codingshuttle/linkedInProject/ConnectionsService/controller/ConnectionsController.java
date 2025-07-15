package com.codingshuttle.linkedInProject.ConnectionsService.controller;

import com.codingshuttle.linkedInProject.ConnectionsService.entity.Person;
import com.codingshuttle.linkedInProject.ConnectionsService.service.ConnectionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/core")
public class ConnectionsController {

    private final ConnectionsService connectionsService;

    @PostMapping("/{userId}/first-degree")
    ResponseEntity<List<Person>> getFirstDegreeConnectionsOfUser(@PathVariable Long userId){

        List<Person> personList = connectionsService.getFirstDegreeConnectionOfUser(userId);
        return ResponseEntity.ok(personList);
    }
}
