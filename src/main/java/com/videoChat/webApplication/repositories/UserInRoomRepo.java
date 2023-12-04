package com.videoChat.webApplication.repositories;

import com.videoChat.webApplication.Entities.Room;
import com.videoChat.webApplication.Entities.UserInRoom;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserInRoomRepo extends JpaRepository<UserInRoom,Long> {
    @Query(value = "select * from user_im_room where room_code=:inviteCode and active_user_in_room_status=:i",nativeQuery = true)
    List<UserInRoom> findAllCurrentInRoom(@Param("inviteCode") String inviteCode,@Param("i") int i);
    @Modifying
    @Transactional
    @Query(value = "update user_im_room set active_user_in_room_status=0,user_left_time=:endTimeOfUser,duration=:duration where user_id=:userid and room_code=:inviteCode and active_user_in_room_status=1 ",nativeQuery = true)
    void updateDetailsOfUser(@Param("inviteCode") String inviteCode,@Param("userid") String userid,@Param("endTimeOfUser") LocalDateTime endTimeOfUser,@Param("duration") String duration);
 @Modifying
 @Transactional
     @Query(value = "select * from user_im_room where room_code=:inviteCode and active_user_in_room_status=1",nativeQuery = true)
    List<UserInRoom> fetchDetailsOfRoom(String inviteCode);

   @Query(value = "select user_joined_time from user_im_room where user_id = :userid and room_code = :inviteCode and user_left_time IS NULL ", nativeQuery = true)
   LocalDateTime getStartTime(@Param("inviteCode") String inviteCode, @Param("userid") String userid);


    @Query(value = "select * from user_im_room where user_joined_time>=:startOfDay and user_left_time<=:endOfDay and room_code IN (select room_code from user_im_room where user_id=:email)LIMIT :offset, :pageSize",nativeQuery = true)
    List<UserInRoom> fetchCallHistoryWithRoomCodeAndUser(@Param("email") String email,@Param("startOfDay") LocalDateTime startOfDay,@Param("endOfDay") LocalDateTime endOfDay,@Param("offset")Long offset,@Param("pageSize")Integer pageSize);

    @Query(value = "select * from user_im_room where (user_joined_time>=:startOfDay and user_left_time<=:endOfDay) and (user_id=:email or user_id LIKE :remoteUser%) and room_code IN(select room_code from user_im_room where user_id LIKE :remoteUser% and room_code IN(select room_code from user_im_room where user_id=:email))order by room_code LIMIT :offset, :pageSize",nativeQuery = true)
    List<UserInRoom> fetchCallHistoryWithRoomCodeAndUserAndRemoteUser(@Param("email") String email,@Param("startOfDay") LocalDateTime startOfDay,@Param("endOfDay") LocalDateTime endOfDay,@Param("remoteUser") String remoteUser,@Param("offset")Long offset,@Param("pageSize")Integer pageSize);

    @Query(value = "select * from user_im_room where user_joined_time>=:startOfDay and user_left_time<=:endOfDay and room_code IN (select room_code from user_im_room where user_id=:email)",nativeQuery = true)
    List<UserInRoom> fetchCallHistoryWithRoomCodeAndUserss(@Param("email") String email,@Param("startOfDay") LocalDateTime startOfDay,@Param("endOfDay") LocalDateTime endOfDay);

    @Query(value = "select * from user_im_room where user_joined_time>=:startOfDay and user_left_time<=:endOfDay and (user_id=:email or user_id LIKE :remoteUser%) and room_code IN(select room_code from user_im_room where user_id LIKE :remoteUser% and room_code IN(select room_code from user_im_room where user_id=:email))",nativeQuery = true)
    List<UserInRoom> fetchCallHistoryWithRoomCodeAndUserAndRemoteUserss(@Param("email") String email,@Param("startOfDay") LocalDateTime startOfDay,@Param("endOfDay") LocalDateTime endOfDay,@Param("remoteUser") String remoteUser);
    @Query(value = "select user_id from user_im_room where room_code=:roomCode and active_user_in_room_status=1",nativeQuery = true)
    List<String> getAlUser(@Param("roomCode") String roomCode);
}
