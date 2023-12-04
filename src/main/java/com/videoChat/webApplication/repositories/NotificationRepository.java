package com.videoChat.webApplication.repositories;

import com.videoChat.webApplication.Entities.Notification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findByReceiver(String receiverEmail);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM notification n WHERE n.sender_email = :sender AND n.receiver_email = :receiver",nativeQuery = true)
    void deleteBySenderAndReceiver(String sender, String receiver);
}
