package com.codingshuttle.linkedInProject.ConnectionsService.controller;

import com.codingshuttle.linkedInProject.ConnectionsService.entity.Person;
import com.codingshuttle.linkedInProject.ConnectionsService.service.ConnectionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/core")
public class ConnectionsController {

    private final ConnectionsService connectionsService;

    @GetMapping("/{userId}/first-degree")
    ResponseEntity<List<Person>> getFirstDegreeConnectionsOfUser(@PathVariable Long userId){
        List<Person> personList = connectionsService.getFirstDegreeConnectionOfUser(userId);
        return ResponseEntity.ok(personList);
    }

    @PostMapping("/request/{userId}")
    ResponseEntity<Void> sendConnectionRequest(@PathVariable Long userId){
        connectionsService.SendConnectionRequest(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/accept/{userId}")
    ResponseEntity<Void> acceptConnectionRequest(@PathVariable Long userId){
        connectionsService.acceptConnectionRequest(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reject/{userId}")
    ResponseEntity<Void> rejectConnectionRequest(@PathVariable Long userId){
        connectionsService.rejectConnectionRequest(userId);
        return ResponseEntity.noContent().build();
    }

}
