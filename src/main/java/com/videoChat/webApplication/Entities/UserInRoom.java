package com.videoChat.webApplication.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "user_im_room")
public class UserInRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name="active_user_inRoom_status")
    private int active_user_inRoom;

    @Column(name = "userId")
    private String userId;

    @Column(name="roomCode")
    private String roomCode;

    @Column(name="user_joined_time")
    private LocalDateTime userJoinedTime;

    @Column(name = "user_left_time")
    private LocalDateTime userLeftTime;

    @Column(name = "duration")
    private String duration;
}
