package com.tech.programming.Authentication_server.controller;

import com.tech.programming.Authentication_server.dto.UserRequest;
import com.tech.programming.Authentication_server.dto.UserResponse;
import com.tech.programming.Authentication_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public UserResponse createUser(@RequestBody UserRequest userRequest){
        return userService.addUser(userRequest);
    }

    @GetMapping("{id}")
    public UserResponse getUser(@PathVariable Long id){
        return userService.getUser(id);
    }
}
