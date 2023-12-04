package com.videoChat.webApplication.repositories;

import com.videoChat.webApplication.Entities.UserPresence;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserStatus extends JpaRepository<UserPresence,Long> {
    UserPresence findByUserId(Long user_id);
   @Query(value = "SELECT * FROM user_presence where user_status=1", nativeQuery = true)
   List<UserPresence> getAllInformation();

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_presence SET user_status = :status WHERE user_id = :userId", nativeQuery = true)
    void updateUserStatusByUserId(@Param("userId") Long userId, @Param("status") int status);
}

