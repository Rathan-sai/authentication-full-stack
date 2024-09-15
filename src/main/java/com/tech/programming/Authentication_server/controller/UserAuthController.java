package com.tech.programming.Authentication_server.controller;

import com.tech.programming.Authentication_server.dto.LoginRequest;
import com.tech.programming.Authentication_server.dto.UserResponse;
import com.tech.programming.Authentication_server.entity.User;
import com.tech.programming.Authentication_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UserAuthController {

    @Autowired
    UserService userService;

    @GetMapping
    public String login(){
//        return userService.login(loginRequest);
        return "welcome to the programming world";
    }
}
