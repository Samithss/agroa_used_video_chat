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
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "room_start_time")
    private LocalDateTime room_start_time;

    @Column(name = "room_end_time")
    private LocalDateTime room_end_time;

    @Column(name="duration")
    private String duration;

    @Column(name="roomStatus")
    private int roomStatus;

    @Column(name="hostEmail")
    private String hostEmail;

    @Column(name="roomCode")
    private String roomCode;

}
