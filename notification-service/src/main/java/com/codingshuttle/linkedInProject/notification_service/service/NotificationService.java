package com.codingshuttle.linkedInProject.notification_service.service;

import com.codingshuttle.linkedInProject.notification_service.entity.Notification;
import com.codingshuttle.linkedInProject.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void addNotification(Notification notification){
        log.info("adding notification with message {}",notification.getMessage());
        notificationRepository.save(notification);
    }
}


//      SendMailer to send email
//      FCM