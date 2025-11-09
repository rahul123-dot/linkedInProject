package com.codingshuttle.linkedInProject.ConnectionsService.service;

import com.codingshuttle.linkedInProject.ConnectionsService.auth.AuthContextHolder;
import com.codingshuttle.linkedInProject.ConnectionsService.entity.Person;
import com.codingshuttle.linkedInProject.ConnectionsService.event.PersonConnection;
import com.codingshuttle.linkedInProject.ConnectionsService.exception.BadRequestException;
import com.codingshuttle.linkedInProject.ConnectionsService.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.web.reactive.AdditionalHealthEndpointPathsWebFluxHandlerMapping;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionsService {

    private final PersonRepository personRepository;
    private final KafkaTemplate<Long,PersonConnection> personConnectionKafkaTopic;

    public List<Person> getFirstDegreeConnectionOfUser(Long userId){
        log.info("Getting firs degree connections of user : "+userId);
        return personRepository.getFirstDegreeConnections(userId);
    }

    public void SendConnectionRequest(Long receiverId){
        Long senderId = AuthContextHolder.getCurrentUserId();

        Optional<Person> person= personRepository.findByUserId(senderId);

        log.info("sending connection Request with SenderId:{} to ReceiverId:{}",senderId,receiverId);

        if(senderId.equals(receiverId)){
            throw new BadRequestException("Both SenderId and ReceiverId are same");
        }
        boolean isRequestExist = personRepository.connectionRequestExists(senderId,receiverId);
        if(isRequestExist){
            throw  new BadRequestException("connection Request is already send , cannot be send again");
        }
        boolean isAlreadyConnected=personRepository.alreadyConnected(senderId,receiverId);
        if(isAlreadyConnected){
            throw new BadRequestException("Already Connected");
        }

        personRepository.addConnectionRequest(senderId,receiverId);
        log.info("Connection Request send Successfully");

//         Todo: sending event to receiver "name connection request send with SenderId:{} To ReceiverId"

        PersonConnection personConnection = PersonConnection.builder()
                .name(person.get().getName())
                .senderId(senderId)
                .receiverId(receiverId)
                .build();
        personConnectionKafkaTopic.send("Connection-Request-Topic",personConnection);
        log.info("after kafka");

    }

    public void acceptConnectionRequest(Long senderId){
        Long receiverId = AuthContextHolder.getCurrentUserId();
        Optional<Person> person= personRepository.findByUserId(senderId);

        log.info("Accepting connection Request with SenderId:{} ReceiverId:{}",senderId,receiverId);

        if(senderId.equals(receiverId)){
            throw new BadRequestException("Both SenderId and ReceiverId are same");
        }

        boolean isRequestExist = personRepository.connectionRequestExists(senderId,receiverId);
        if(!isRequestExist){
            throw  new BadRequestException("No Connection request exists, cannot accept without request");
        }

        boolean isAlreadyConnected=personRepository.alreadyConnected(senderId,receiverId);
        if(isAlreadyConnected){
            throw new BadRequestException("Already connected users, cannot accept connection request again");
        }

        personRepository.acceptConnectionRequest(senderId,receiverId);
        log.info("Accepted Connection request with SenderId:{} ReceiverId:{}",senderId,receiverId);

//        Todo: sending event to sender m "name connection request accepted with ReceiverId:{} To senderId"
        PersonConnection personConnection = PersonConnection.builder()
                .name(person.get().getName())
                .senderId(receiverId)
                .receiverId(senderId)
                .build();
        personConnectionKafkaTopic.send("Connection-Request-Accept-Topic",personConnection);
    }
    public void rejectConnectionRequest(Long senderId){
        Long receiverId = AuthContextHolder.getCurrentUserId();
        Optional<Person> person= personRepository.findByUserId(senderId);

        log.info("rejecting connection Request with SenderId:{} ReceiverId:{}",senderId,receiverId);

        if(senderId.equals(receiverId)){
            throw new BadRequestException("Both SenderId and ReceiverId are same");
        }

        boolean isRequestExist = personRepository.connectionRequestExists(senderId,receiverId);
        if(!isRequestExist){
           throw new BadRequestException("request not exist, cannot be rejected");
        }

        personRepository.rejectConnectionRequest(senderId,receiverId);
        log.info("rejected Connection request with SenderId:{} and ReceiverId:{}",senderId,receiverId);

        //Todo: sending event to sender m "name connection request rejected with ReceiverId:{} To senderId"

        PersonConnection personConnection = PersonConnection.builder()
                .name(person.get().getName())
                .senderId(receiverId)
                .receiverId(senderId)
                .build();
        personConnectionKafkaTopic.send("Connection-Request-Reject-Topic",personConnection);


    }
}
