package com.videoChat.webApplication.repositories;

import com.videoChat.webApplication.Entities.User;
import com.videoChat.webApplication.model.LoginModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LoginRepository extends JpaRepository<User ,Long> {
    User findByEmailAndPassword(String email, String password);
}
