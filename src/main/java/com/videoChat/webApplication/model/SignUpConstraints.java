package com.videoChat.webApplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
public class SignUpConstraints {
    public  String INVALID="USER ALREADY EXISTS";
    private boolean SIGNUP_SUCCESS;
}
