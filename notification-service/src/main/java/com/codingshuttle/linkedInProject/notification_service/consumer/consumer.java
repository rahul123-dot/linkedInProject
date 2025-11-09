package com.codingshuttle.linkedInProject.notification_service.consumer;

import com.codingshuttle.linkedInProject.ConnectionsService.event.PersonConnection;
import com.codingshuttle.linkedInProject.notification_service.entity.Notification;
import com.codingshuttle.linkedInProject.notification_service.service.NotificationService;
import com.codingshuttle.linkedInProject.postsService.event.PostCreated;
import com.codingshuttle.linkedInProject.postsService.event.PostLiked;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class consumer {

    private final NotificationService notificationService;

    @KafkaListener(topicPattern = "Post_Created_Topic")
    public void receivedPostCreatedMessage(PostCreated postCreated){
        log.info("handle postCreate {}",postCreated);
        String message = String.format("User with id:%d is created this post with message:%s"
                                        ,postCreated.getOwnerUserId(),postCreated.getContent());

        Notification notification = Notification.builder()
                .message(message)
                .userId(postCreated.getUserId())
                .build();
        notificationService.addNotification(notification);
    }

    @KafkaListener(topicPattern = "Post_Liked_Topic")
    public void receivedPostLikedMessage(PostLiked postLiked){
        log.info("handle postLiked {}",postLiked);
        String message = String.format("User with id:%d is liked your post with id: %d",
                postLiked.getLikeByUserId(),postLiked.getPostId());

        Notification notification = Notification.builder()
                .message(message)
                .userId(postLiked.getOwnerUserId())
                .build();
        notificationService.addNotification(notification);
    }


    @KafkaListener(topicPattern = "Connection-Request-Topic")
    public void receivedConnectionRequestMessage(PersonConnection personConnection){
        log.info("receiving Connection Request {}",personConnection);

        String message = String.format("name:  connection request send with senderId:%d to ReceiverId:%d"
                                    ,personConnection.getSenderId(),personConnection.getReceiverId());

        Notification notification = Notification.builder()
                .message(message)
                .userId(personConnection.getSenderId())
                .build();
        notificationService.addNotification(notification);
    }
    @KafkaListener(topicPattern = "Connection-Request-Accept-Topic")
    public void receivedConnectionAcceptMessage(PersonConnection personConnection){
        log.info("receiving Connection Request for accept {}",personConnection);

        String message = String.format("name: {} connection request Accept with receiverId:%d to SenderId:%d"
                ,personConnection.getName(),personConnection.getReceiverId(),personConnection.getSenderId());

        Notification notification = Notification.builder()
                .message(message)
                .userId(personConnection.getReceiverId())
                .build();
        notificationService.addNotification(notification);
    }

    @KafkaListener(topicPattern = "Connection-Request-Reject-Topic")
    public void receivedConnectionRejectMessage(PersonConnection personConnection){
        log.info("receiving Connection Request for reject {}",personConnection);

        String message = String.format("name: {} connection request Reject with receiverId:%d to SenderId:%d"
                ,personConnection.getName(),personConnection.getReceiverId(),personConnection.getSenderId());

        Notification notification = Notification.builder()
                .message(message)
                .userId(personConnection.getReceiverId())
                .build();
        notificationService.addNotification(notification);
    }

}
