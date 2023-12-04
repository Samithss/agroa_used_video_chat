package com.videoChat.webApplication.model;

import com.videoChat.webApplication.Entities.UserPresence;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserPresenceWithUserEmail {
    private UserPresence userPresence;
    private String userEmail;
}
