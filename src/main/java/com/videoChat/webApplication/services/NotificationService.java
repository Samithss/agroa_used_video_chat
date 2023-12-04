package com.videoChat.webApplication.services;

import com.videoChat.webApplication.Entities.Notification;
import com.videoChat.webApplication.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    NotificationRepository notificationRepository;
    public void details(String receiver, String sender,String inviteCode) {
        Notification notification= new Notification();
        notification.setReceiver(receiver);
        notification.setSender(sender);
        notification.setInviteCode(inviteCode);
        notificationRepository.save(notification);
    }

    public List<Notification> getDetailsIfPresent(String user) {
     List<Notification> notifications =notificationRepository.findByReceiver(user);
     return notifications;
    }

    public void deleteAll(String sender, String receiver) {
        notificationRepository.deleteBySenderAndReceiver( sender, receiver);
    }
}
