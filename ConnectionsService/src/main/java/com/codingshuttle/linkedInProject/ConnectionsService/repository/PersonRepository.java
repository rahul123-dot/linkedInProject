package com.codingshuttle.linkedInProject.ConnectionsService.repository;

import com.codingshuttle.linkedInProject.ConnectionsService.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends Neo4jRepository<Person,Long> {

    Optional<Person> findByUserId(Long userId);

    @Query("match (personA:User)-[:CONNECTED_TO]-(personB:User) " +
            "where personA.userid = $userId " +
            "return personB")
    List<Person> getFirstDegreeConnections(Long userId);
}
