package com.videoChat.webApplication.services;

import com.videoChat.webApplication.Entities.Room;
import com.videoChat.webApplication.Entities.User;
import com.videoChat.webApplication.Entities.UserInRoom;
import com.videoChat.webApplication.repositories.RoomDetailsRepo;
import com.videoChat.webApplication.repositories.SignUpRepository;
import com.videoChat.webApplication.repositories.UserInRoomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDetailsService {
    @Autowired
    SignUpRepository signUpRepository;
    @Autowired
    RoomDetailsRepo roomDetailsRepo;
    @Autowired
    UserInRoomRepo userInRoomRepo;
    public User fetchAllInformation(String email) {

       User user= signUpRepository.findByEmail(email);
       if(user!=null){
           return user;
       }
       return null;
    }

    public List<Room> roomDetailsOfUser(String email) {
        List<Room> room=roomDetailsRepo.findByHostEmail(email);
        if(room!=null){
            return room;
        }
        return null;
    }

    private LocalDate convertStringToLocalDateAndTime(String startTime) {
        LocalDate localDate = LocalDate.parse(startTime);
        return localDate;
    }

    public  List<UserInRoom>  fetchInformationOfVideoCalls(String date, String remoteUserid,String currentUserId,String pageNumberr) {
        if (remoteUserid.equals(currentUserId)) {
            return null;
        } else {
            Integer pageSize = 10;
            Long pageNumber = Long.parseLong(pageNumberr.toString());
            System.out.println("pageNumber" + pageNumber);
            Long offset = (pageNumber - 1L) * pageSize;
            LocalDate localDateTime = convertStringToLocalDateAndTime(date);
            LocalDateTime startOfDay = localDateTime.atStartOfDay();
            LocalDateTime endOfDay = startOfDay.plusDays(1);
            List<UserInRoom> list = userInRoomRepo.fetchCallHistoryWithRoomCodeAndUserAndRemoteUser(currentUserId, startOfDay, endOfDay, remoteUserid, offset, pageSize);
            return list;
        }
    }


    public  List<UserInRoom> fetchCallHistory(String email,String date,String pageNumberr) {
        Integer pageSize=10;
        Long pageNumber=Long.parseLong(pageNumberr.toString());
        System.out.println("pageNumber"+pageNumber);
        Long offset=(pageNumber-1L)*pageSize;
        LocalDate localDateTime = convertStringToLocalDateAndTime(date);
        LocalDateTime startOfDay = localDateTime.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        List<UserInRoom> list=userInRoomRepo.fetchCallHistoryWithRoomCodeAndUser(email,startOfDay,endOfDay,offset,pageSize);
        return list;
    }


    public Integer fetchCallHistoryTotalRecords(String email, String date) {
        LocalDate localDateTime = convertStringToLocalDateAndTime(date);
        LocalDateTime startOfDay = localDateTime.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        List<UserInRoom> list=userInRoomRepo.fetchCallHistoryWithRoomCodeAndUserss(email,startOfDay,endOfDay);
        return list.size();
    }

    public Integer fetchInformationOfVideoCallsTotalRecord(String date, String remoteUserid, String email) {
        LocalDate localDateTime = convertStringToLocalDateAndTime(date);
        LocalDateTime startOfDay = localDateTime.atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        List<UserInRoom> list=userInRoomRepo.fetchCallHistoryWithRoomCodeAndUserAndRemoteUserss(email,startOfDay,endOfDay,remoteUserid);
        return list.size();
    }
}
