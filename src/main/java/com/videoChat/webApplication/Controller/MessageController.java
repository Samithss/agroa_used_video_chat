package com.videoChat.webApplication.Controller;

import com.videoChat.webApplication.Entities.Notification;
import com.videoChat.webApplication.model.Message;
import com.videoChat.webApplication.model.SessionIdHolder;
import com.videoChat.webApplication.repositories.UserInRoomRepo;
import com.videoChat.webApplication.services.NotificationService;
import com.videoChat.webApplication.services.RoomDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Controller
public class MessageController {

    @Autowired
    NotificationService notificationService;
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    HttpServletRequest request;
    @Autowired
    SessionIdHolder sessionIdHolder;

    @Autowired
    RoomDetailsService roomDetailsService;


    @Autowired
    public MessageController(SimpMessagingTemplate simpMessagingTemplate, HttpServletRequest request) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.request = request;
    }

    private String userSessionId;

    @MessageMapping("/application")
    @SendTo("/all/messages")
    public Message sendToSpecificUser(@Payload Message message) throws Exception {
        return message;
    }


    @PostMapping("/get-session-id")
    @ResponseBody
    public ResponseEntity getID(@RequestBody Map<String, String> requestData) {
        String email = requestData.get("param1");
        String sessionId = sessionIdHolder.getSessionIdByEmail(email);
        userSessionId = sessionId;
        if (sessionId != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("text", sessionId);
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
        } else {
            return null;
        }
    }

    @PostMapping("/putInviteDetails")
    @ResponseBody
    public void putDetails(@RequestBody Map<String, String> requestData) {
        String receiver = requestData.get("param1");
        String sender = requestData.get("param2");
        String inviteCode = requestData.get("param3");
        notificationService.details(receiver, sender, inviteCode);
    }

    @PostMapping("/get-notification-details")
    public ResponseEntity<List<Notification>> getDetails(@RequestBody Map<String, String> requestData) {
        String user = requestData.get("parameter1");
        List<Notification> notifications = notificationService.getDetailsIfPresent(user);
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @PostMapping("/delete-notification-details")
    @ResponseBody
    public void deleteAll(@RequestBody Map<String, String> requestData) {
        String sender = requestData.get("parameter1");
        String receiver = requestData.get("parameter2");
        notificationService.deleteAll(sender, receiver);
    }

    @PostMapping("/detailsOfStartRoom")
    @ResponseBody
    public ResponseEntity<String> detailsOfStartRoom(@RequestBody Map<String, String> requestData) {
        String hostEmail = requestData.get("param1");
        String startTime = requestData.get("param3");
        String roomId = roomDetailsService.roomDetails(hostEmail, startTime);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", roomId);
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }


    @PostMapping("/userJoinedToRoom")
    @ResponseBody
    public ResponseEntity<Object> userJoinedToRoom(@RequestBody Map<String, String> requestData) {
        String inviteCode = requestData.get("param1");
        String email = requestData.get("param2");
        int status = Integer.parseInt(requestData.get("param3"));
        String joinTime=requestData.get("param4");
        String result = roomDetailsService.userInRoomDetails(inviteCode, email, status,joinTime);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @PostMapping("/detailsOfUserInRoom")
    @ResponseBody
    public ResponseEntity<Object> detailsOfUserInRoom(@RequestBody Map<String, String> requestData) {
        String email = requestData.get("param1");
        String inviteCode = requestData.get("param2");
        String startTime=requestData.get("param3");
        roomDetailsService.userInRoom(email,inviteCode,startTime);
        return  new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/endTime")
    @ResponseBody
    public ResponseEntity<Object> endTime(@RequestBody Map<String, String> requestData){
        String inviteCode= requestData.get("param1");
        String endTime=requestData.get("param2");
        roomDetailsService.updateEndTime(inviteCode,endTime);
        return  new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/userLeftTheRoom")
    @ResponseBody
    public ResponseEntity<String> userLeftTheRoom(@RequestBody Map<String, String> requestData) throws Exception {
        String inviteCode=requestData.get("param1");
        String userid=requestData.get("param2");
        String endTimeOfUser=requestData.get("param3");
        roomDetailsService.updateUserEndTime(inviteCode,userid,endTimeOfUser);
        return  new ResponseEntity<>(HttpStatus.OK);
    }
}



