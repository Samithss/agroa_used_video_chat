package com.videoChat.webApplication.services;

import com.videoChat.webApplication.Entities.User;
import com.videoChat.webApplication.repositories.SignUpRepository;
import com.videoChat.webApplication.repositories.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LogoutService {
    @Autowired
    SignUpRepository signUpRepository;
    @Autowired
    UserStatus userStatus;
    public void userEmail(String email) {
        User user=signUpRepository.findByEmail(email);
        if(user!=null){
            userStatus.updateUserStatusByUserId(user.getUserId(),0);
        }
    }

}
