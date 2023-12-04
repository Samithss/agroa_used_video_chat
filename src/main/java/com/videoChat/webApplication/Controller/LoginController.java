package com.videoChat.webApplication.Controller;

import com.videoChat.webApplication.model.LoginConstraints;
import com.videoChat.webApplication.model.SessionIdHolder;
import com.videoChat.webApplication.services.LoginService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.security.auth.login.LoginContext;
import java.util.Map;

@Controller
public class LoginController {
    @Autowired
    LoginConstraints loginConstraints;
    @Autowired
    LoginService loginService;
    @Autowired
     HttpServletRequest session;

    @Autowired
    SessionIdHolder sessionIdHolder;

//    @GetMapping("/login")
    @GetMapping("/login")
    public String login( Model model) {
        model.addAttribute("errorMessage", " ");
        return "login";
    }

    @GetMapping("/")
    public String loginNew( Model model) {
        model.addAttribute("errorMessage", " ");
        return "login";
    }

    @PostMapping("/login")
    public ModelAndView loginPage(@RequestParam Map<String, Object> map , Model model )   {
        String email = map.get("email").toString();
        String password = map.get("password").toString();
        loginService.validLogin(email, password);
        if (loginConstraints.getLOGIN_SUCCESS()) {
            session.getSession().setAttribute("email", email);
            System.out.println((String) session.getSession().getAttribute("email"));
            String sessionId = session.getSession().getId();

            // Store the session ID in the SessionIdHolder
            sessionIdHolder.storeSessionId(email, sessionId);
            model.addAttribute("email", (String) session.getAttribute("email"));

            return  new ModelAndView("redirect:/home");
        } else {
            model.addAttribute("errorMessage", loginConstraints.INVALID_CREDENTIAL);
            return  new ModelAndView("redirect:/login");
        }
    }

}
