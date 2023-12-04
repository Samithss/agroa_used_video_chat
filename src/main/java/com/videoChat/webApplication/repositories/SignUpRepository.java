package com.videoChat.webApplication.repositories;

import com.videoChat.webApplication.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface SignUpRepository extends JpaRepository<User ,Long> {
    User findByEmail(String email);
    @Query(value = "select * from user where user_id=:user_id and email!=:email",nativeQuery = true)
    User findByIdAndEmail(@Param("user_id") Long user_id, @Param("email")String email);
}
