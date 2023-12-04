package com.videoChat.webApplication.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
public class LoginConstraints {
    public  final String INVALID_CREDENTIAL="INVALID_CREDENTIALS";
    private Boolean LOGIN_SUCCESS;
}
