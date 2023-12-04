package com.videoChat.webApplication.Controller;

import com.videoChat.webApplication.services.LoginService;
import com.videoChat.webApplication.services.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LogoutController {
    @Autowired
    LogoutService logoutService;
    @Autowired
    HttpServletRequest session;
    @GetMapping ("/logout")
    public String logoutUser(){
       String email= (String) session.getSession().getAttribute("email");
       logoutService.userEmail(email);
       HttpSession session1=session.getSession(false);// this will give current session without creating new one
       session1.invalidate();
        System.out.println( "after invalidate"+  (String) session.getSession().getAttribute("email"));
        return "redirect:/login";
    }
}
