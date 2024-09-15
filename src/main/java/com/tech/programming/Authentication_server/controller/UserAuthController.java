package com.tech.programming.Authentication_server.controller;

import com.tech.programming.Authentication_server.dto.LoginRequest;
import com.tech.programming.Authentication_server.dto.UserRequest;
import com.tech.programming.Authentication_server.dto.UserResponse;
import com.tech.programming.Authentication_server.entity.User;
import com.tech.programming.Authentication_server.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UserAuthController {

    @Autowired
    UserService userService;

    @GetMapping
    public String login(HttpServletRequest httpServletRequest){
//        return userService.login(loginRequest);
        return "welcome to the programming world " + httpServletRequest.getSession().getId();
    }

    @GetMapping("names")
    public String get(){
        return "rathan, jeevan";
    }

    @GetMapping("csrf-token")
    public CsrfToken getToken(HttpServletRequest request){
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @PostMapping("students")
    public String add(@RequestBody UserRequest request){
        return "added students";
    }
}
