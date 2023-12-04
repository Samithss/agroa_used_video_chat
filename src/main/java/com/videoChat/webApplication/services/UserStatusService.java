package com.videoChat.webApplication.services;

import com.videoChat.webApplication.Entities.User;
import com.videoChat.webApplication.Entities.UserPresence;
import com.videoChat.webApplication.model.UserPresenceWithUserEmail;
import com.videoChat.webApplication.repositories.SignUpRepository;
import com.videoChat.webApplication.repositories.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Component
public class UserStatusService {
    @Autowired
    UserStatus userStatus;

    @Autowired
    SignUpRepository signUpRepository;
    public List<UserPresenceWithUserEmail> getAllInformationWithUserEmail(String email) {
        List<UserPresence> userPresences = userStatus.getAllInformation();
        List<UserPresenceWithUserEmail> result = new ArrayList<>();

        for (UserPresence userPresence : userPresences) {
            String userEmail = getUserEmailByUserId(userPresence.getUserId(),email);
            if(userEmail!=null) {
                result.add(new UserPresenceWithUserEmail(userPresence, userEmail));
            }
        }
        return result;
    }

    private String getUserEmailByUserId(Long userId,String email) {
        User user = signUpRepository.findByIdAndEmail(userId,email);
        if (user!=null){
          return user.getEmail();
        }
        return null;
    }
}
