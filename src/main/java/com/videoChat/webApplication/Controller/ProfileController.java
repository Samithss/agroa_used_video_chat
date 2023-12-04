package com.videoChat.webApplication.Controller;

import com.videoChat.webApplication.Entities.Room;
import com.videoChat.webApplication.Entities.User;

import com.videoChat.webApplication.Entities.UserInRoom;
import com.videoChat.webApplication.services.UserDetailsService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class ProfileController {

    private String email;

    @Autowired
    HttpServletRequest session;
    @Autowired
    UserDetailsService userDetailsService;

    @GetMapping("/profile")
    public String profile(Model model) {
        email = (String) session.getSession().getAttribute("email");
        if (email != null) {
            return "profile";
        }
        return "login";
    }

    @GetMapping("/activeDetails/fetch")
    public ResponseEntity<User> getAllInformation() {
        System.out.println(email);
        User user = userDetailsService.fetchAllInformation(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/roomDetails")
    public ResponseEntity<List<Room>> getInfo() {
        List<Room> room = userDetailsService.roomDetailsOfUser(email);
        return new ResponseEntity<>(room, HttpStatus.OK);
    }

    @PostMapping("/searchVideoCAllHistory")
    @ResponseBody
    public ResponseEntity<List<UserInRoom>> searchVideoCAllHistory(@RequestBody Map<String, String> requestData) {
        String date = requestData.get("currentDateE");
        String remoteUserid = requestData.get("currentUserId");
        String pageNumber=requestData.get("pageNo");
        if (!date.equals("")) {
            if (remoteUserid.equals("")) {
                List<UserInRoom> list = userDetailsService.fetchCallHistory(email, date,pageNumber);
                return new ResponseEntity<>(list, HttpStatus.OK);
            } else {
                List<UserInRoom> map = userDetailsService.fetchInformationOfVideoCalls(date, remoteUserid, email,pageNumber);
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/totalNumberOfRecords")
    @ResponseBody
    public ResponseEntity<Integer> totalNumberOfRecords(@RequestBody Map<String,String > data){
        String date = data.get("currentDate");
        String remoteUserid = data.get("userId");
        if (!date.equals("")) {
            if (remoteUserid.equals("")) {
                Integer list= userDetailsService.fetchCallHistoryTotalRecords(email, date);
                return new ResponseEntity<>(list, HttpStatus.OK);
            } else {
                Integer list1 = userDetailsService.fetchInformationOfVideoCallsTotalRecord(date, remoteUserid, email);
                return new ResponseEntity<>(list1, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
