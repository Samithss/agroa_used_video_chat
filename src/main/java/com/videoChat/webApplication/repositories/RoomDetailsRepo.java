package com.videoChat.webApplication.repositories;

import com.videoChat.webApplication.Entities.Room;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoomDetailsRepo extends JpaRepository<Room,Long> {
    List<Room> findByHostEmail(String hostEmail);
    @Query(value = "SELECT * FROM room r WHERE r.room_code = :roomCode AND r.room_status = :roomStatus and  room_end_time IS NULL",nativeQuery = true)
    List<Room> findByRoomCodeAndRoomStatus(@Param("roomCode") String roomCode, @Param("roomStatus") int roomStatus);
    @Modifying
    @Transactional
    @Query(value = "update room set room_status =1 where room_code=:inviteCode",nativeQuery = true)
    void updateActiveStatusByRoomCode(@Param("inviteCode")String inviteCode);
    @Modifying
    @Transactional
    @Query(value = "update room set room_end_time =:endTime,room_status=0 where room_code=:inviteCode",nativeQuery = true)
    void updateEndTime(@Param("inviteCode")String inviteCode, @Param("endTime") LocalDateTime endTime);
    @Modifying
    @Transactional
    @Query(value = "update room set room_status=0 ,room_end_time=:endTimeOfUser,duration=:duration where room_code=:inviteCode",nativeQuery = true)
    void updateStatus(@Param("inviteCode")String inviteCode,@Param("endTimeOfUser") LocalDateTime endTimeOfUser,@Param("duration")String duration);
    @Modifying
    @Transactional
    @Query(value = "select * from room where host_email=:currentUserId and room_start_time>=:startOfDay and room_start_time<=:endOfDay",nativeQuery = true)
        List<Room> fetchDetails(@Param("currentUserId") String currentUserId,@Param("startOfDay") LocalDateTime startOfDay,@Param("endOfDay") LocalDateTime endOfDay);
    @Query(value = "select room_start_time from room where room_code=:inviteCode ",nativeQuery = true)
    LocalDateTime getStartTime(@Param("inviteCode") String inviteCode);
}
