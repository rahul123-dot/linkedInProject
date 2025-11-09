package com.codingshuttle.linkedInProject.ConnectionsService.event;

import lombok.Data;

@Data
public class PersonConnection {
    private Long senderId;
    private Long receiverId;
    private String name;
}
