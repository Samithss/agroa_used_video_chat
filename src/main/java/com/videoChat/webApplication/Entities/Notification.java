package com.videoChat.webApplication.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id", nullable = false)
    private Long notification_id;

    @Column(name = "senderEmail")
    private String sender;

    @Column(name = "receiverEmail")
    private String receiver;

    @Column(name="inviteCode")
    private String inviteCode;
}
