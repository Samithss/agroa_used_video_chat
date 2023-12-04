package com.videoChat.webApplication.services;

import com.videoChat.webApplication.Entities.User;
import com.videoChat.webApplication.model.SignUpConstraints;
import com.videoChat.webApplication.repositories.SignUpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;

@Component
public class SignUpService {

    PasswordConvertor passwordConvertor= new PasswordConvertor();
    @Autowired
    SignUpRepository signUpRepository;

    @Autowired
    SignUpConstraints signUpConstraints;
    public  void validate(String email,String password,String firstName,String lastName,Long contactNo){
        User user=signUpRepository.findByEmail(email);
        if(user!=null){
          signUpConstraints.setSIGNUP_SUCCESS(false);
        }
        else {
           signUpConstraints.setSIGNUP_SUCCESS(true);
        }
    }

    public void saveUser(String email, String password, String firstname, String lastName, Long contactNo)  {
        String newPassword=null;
        try {
           newPassword= passwordConvertor.getEncodedData(password);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        User newUser =new User();
        newUser.setEmail(email);
        newUser.setContactNo(contactNo);
        newUser.setFirstName(firstname);
        newUser.setLastName(lastName);
        newUser.setPassword(newPassword);
        signUpRepository.save(newUser);
    }
}
