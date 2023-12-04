package com.videoChat.webApplication.Controller;

import com.videoChat.webApplication.Entities.UserPresence;
import com.videoChat.webApplication.model.UserPresenceWithUserEmail;
import com.videoChat.webApplication.repositories.UserStatus;
import com.videoChat.webApplication.services.RoomDetailsService;
import com.videoChat.webApplication.services.UserStatusService;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    UserStatusService userStatusService;
    @Autowired
   HttpServletRequest request;
    @Autowired
    RoomDetailsService roomDetailsService;

    @GetMapping("/home")
    public String login(Model model) {
        String email= (String) request.getSession().getAttribute("email");
        if(email!=null){
        return "home";}
       return "login";
    }

    @GetMapping("/activeUser/fetch")
    public ResponseEntity<List<UserPresenceWithUserEmail>> getAllInformation() {
        String email= (String) request.getSession().getAttribute("email");
        List<UserPresenceWithUserEmail> informationList =userStatusService.getAllInformationWithUserEmail(email);
        return new ResponseEntity<>(informationList, HttpStatus.OK);
    }

    @GetMapping("/get-session")
    public ResponseEntity<Object> fetchUserSession(HttpServletRequest session){
        String email=(String)session.getSession().getAttribute("email");
        if (email != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", email);
            return  new ResponseEntity<>(jsonObject.toString(),HttpStatus.OK);
        }
        else
            return null;
    }

    @PostMapping("/roomDetails")
    @ResponseBody
    public  void roomDetails(@RequestBody Map<String, String> requestData){
        String email=requestData.get("param1");
        String startTime = requestData.get("param2");
        String endTime = requestData.get("param3");
        roomDetailsService.detailsOfRoom(email,startTime,endTime);
    }
    @PostMapping ("/get-userInCall")
    @ResponseBody
    public List<String> userInCall(@RequestBody Map<String,String> requestData)
    {
        String roomCode=requestData.get("parameter1");
        System.out.println(roomCode);
       List<String> list =roomDetailsService.getAllUserInRoom(roomCode);
       return list;
    }
}


