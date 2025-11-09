package com.codingshuttle.linkedInProject.ConnectionsService.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonConnection {
    private Long senderId;
    private Long receiverId;
    private String name;
}
