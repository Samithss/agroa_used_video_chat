package com.videoChat.webApplication.Controller;

import com.videoChat.webApplication.model.SignUpConstraints;
import com.videoChat.webApplication.services.SignUpService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class SignUpController {

    @Autowired
    SignUpConstraints signUpConstraints;

    @Autowired
    SignUpService signUpService;

    @GetMapping("/signup")
    public String signup(Model model,HttpSession session) {
        model.addAttribute("errorUser"," ");
        return "signup";
    }
    @PostMapping("/signup")
    public String singUpform(@RequestParam Map<String, Object> map, Model model){
        String email = map.get("email").toString();
        String password = map.get("password").toString();
        String firstname=map.get("firstName").toString();
        String lastName=map.get("lastName").toString();
        Long contactNo= Long.valueOf(map.get("contactNo").toString());
        signUpService.validate(email,password,firstname,lastName,contactNo);
        if(signUpConstraints.isSIGNUP_SUCCESS())
        {
            signUpService.saveUser(email,password,firstname,lastName,contactNo);
            return "login";
        }
        else {
            model.addAttribute("errorUser",signUpConstraints.INVALID);
        }
        return "signup";
    }
}
