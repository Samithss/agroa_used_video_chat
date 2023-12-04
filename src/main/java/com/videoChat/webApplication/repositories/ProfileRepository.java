package com.videoChat.webApplication.repositories;

import com.videoChat.webApplication.Entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Room,Long> {

}
