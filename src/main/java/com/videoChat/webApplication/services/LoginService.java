package com.videoChat.webApplication.services;

import com.videoChat.webApplication.Entities.User;
import com.videoChat.webApplication.Entities.UserPresence;
import com.videoChat.webApplication.model.LoginConstraints;
import com.videoChat.webApplication.repositories.LoginRepository;
import com.videoChat.webApplication.repositories.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginService {
    @Autowired
    LoginConstraints loginConstraints;
    @Autowired
    LoginRepository loginRepository;

    @Autowired
    UserStatus userStatus;
    PasswordConvertor passwordConvertor= new PasswordConvertor();
  public void validLogin(String email,String password){
      String newPassword=null;
      try {
           newPassword = passwordConvertor.getEncodedData(password);
      }catch (Exception e){
     e.printStackTrace();
      }

    if(email!=null && newPassword!=null){
       User user= loginRepository.findByEmailAndPassword(email, newPassword);
       if(user!=null){
           loginConstraints.setLOGIN_SUCCESS(true);
           UserPresence userPresence = userStatus.findByUserId(user.getUserId());
           if (userPresence == null) {
               userPresence = new UserPresence();
               userPresence.setUserId(user.getUserId());
               userPresence.setUserStatus(1);
               userStatus.save(userPresence);
       }
           else {
               userPresence.setUserStatus(1);
               userStatus.save(userPresence);
           }
       }
       else {
           loginConstraints.setLOGIN_SUCCESS(false);
       }
    }
  }

}
