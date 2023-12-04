package com.videoChat.webApplication.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "user",schema = "samith")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long userId;

    @Column(name = "email",nullable = false,unique = true)
    private String email;

    @Column(name = "firstName",nullable = false)
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "contactNo",nullable = false)
    private Long contactNo;

    @Column(name="password",nullable = false)
    private String password;
}
