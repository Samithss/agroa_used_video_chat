package com.videoChat.webApplication.services;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.videoChat.webApplication.Entities.Room;
import com.videoChat.webApplication.Entities.UserInRoom;
import com.videoChat.webApplication.repositories.RoomDetailsRepo;
import com.videoChat.webApplication.repositories.UserInRoomRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomDetailsService {
    @Autowired
    RoomDetailsRepo roomDetailsRepo;

    @Autowired
    UserInRoomRepo userInRoomRepo;
    @Autowired
    HttpServletRequest request;


    public void detailsOfRoom(String email, String startTime, String endTime) {
//        String userEmail=email;
//        String duration=findDuration(startTime,endTime);
//        Room r= new Room();
//        r.setHostEmail(email);
//        r.setDuration(duration);
//        r.setRoom_end_time(endTime);
//        r.setRoom_start_time(startTime);
//        r.setRoomStatus(0);
//        roomDetailsRepo.save(r);
    }
    private String findDuration(LocalDateTime startTime, LocalDateTime endTime) {//15/8/2023 14:30:45
                Duration duration = Duration.between(startTime, endTime);

                String hours = String.valueOf(duration.toHours());
                String minutes = String.valueOf(duration.toMinutes() % 60);
                String seconds = String.valueOf(duration.getSeconds() % 60);

                String roomDuration=hours+":"+minutes+":"+seconds;
                return roomDuration;
            }

    public String roomDetails(String hostEmail, String startTime) {
        String roomId=generateRandomString(15);
        LocalDateTime startTimeOfUser=convertStringToLocalDateAndTime(startTime);
        Room r= new Room();
        r.setRoom_start_time(startTimeOfUser);
        r.setHostEmail(hostEmail);
        r.setRoomCode(roomId);
        r.setRoomStatus(0);
        roomDetailsRepo.save(r);
        return roomId;
    }

    public String userInRoomDetails(String inviteCode, String email, int status,String joinTime) {
        roomDetailsRepo.updateActiveStatusByRoomCode(inviteCode);
        List<Room> room=roomDetailsRepo.findByRoomCodeAndRoomStatus(inviteCode,1);
        List<UserInRoom> userInRooms=userInRoomRepo.findAllCurrentInRoom(inviteCode,1);
        System.out.println("total user in room"+userInRooms.size());
        if(userInRooms.size()==4){
            return "Full";
        }
         else if(room.size()==1){
         LocalDateTime localDateTime=convertStringToLocalDateAndTime(joinTime);
         UserInRoom users= new UserInRoom();
         users.setActive_user_inRoom(status);
         users.setRoomCode(inviteCode);
         users.setUserId(email);
         users.setUserJoinedTime(localDateTime);
         userInRoomRepo.save(users);
     }else {
            return "error";
        }
     return null;
    }

    public  String generateRandomString(int length) {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    public void userInRoom(String email, String inviteCode,String startTime) {
        LocalDateTime startTimeOfUser=convertStringToLocalDateAndTime(startTime);
        UserInRoom userInRoom= new UserInRoom();
        userInRoom.setRoomCode(inviteCode);
        userInRoom.setActive_user_inRoom(1);
        userInRoom.setUserId(email);
        userInRoom.setUserJoinedTime(startTimeOfUser);
        userInRoomRepo.save(userInRoom);
        roomDetailsRepo.updateActiveStatusByRoomCode(inviteCode);
    }

    private LocalDateTime convertStringToLocalDateAndTime(String startTime) {
        String format = "d/M/yyyy H:m:s";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime startDateTime = LocalDateTime.parse(startTime, formatter);
        return startDateTime;
    }

    public void updateEndTime(String inviteCode, String endTime) {
        LocalDateTime localDateTime=convertStringToLocalDateAndTime(endTime);
        roomDetailsRepo.updateEndTime(inviteCode,localDateTime);
    }

    public void updateUserEndTime(String inviteCode, String userid, String endTimeOfUser)  {
        try {
            LocalDateTime localDateTime = convertStringToLocalDateAndTime(endTimeOfUser);
            LocalDateTime startTime = userInRoomRepo.getStartTime(inviteCode, userid);
            String Duration = findDuration(startTime, localDateTime);
            userInRoomRepo.updateDetailsOfUser(inviteCode, userid, localDateTime, Duration);
            List<UserInRoom> room = userInRoomRepo.fetchDetailsOfRoom(inviteCode);
            if (room.size() == 0) {
                String state = "inactive";
                LocalDateTime startOfRoom = roomDetailsRepo.getStartTime(inviteCode);
                String DurationOfRoom = findDuration(startOfRoom, localDateTime);
                roomDetailsRepo.updateStatus(inviteCode, localDateTime, DurationOfRoom);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public List<String> getAllUserInRoom(String roomCode) {
        String email=(String)request.getSession().getAttribute("email");
     if(roomCode!=null){
         List<String> list=userInRoomRepo.getAlUser(roomCode);
         List<String> updatedList = new ArrayList<>();
         for (String s : list) {
             if (s.equals(email)) {
                 s = s + " (you)";
             }else {
                 s=s+"(Participants)";
             }
             updatedList.add(s);
         }
         return updatedList;
     }
     else
         return null;
    }
}


