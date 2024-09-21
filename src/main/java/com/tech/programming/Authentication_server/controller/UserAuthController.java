package com.tech.programming.Authentication_server.controller;

import com.tech.programming.Authentication_server.dto.LoginRequest;
import com.tech.programming.Authentication_server.dto.LoginResponse;
import com.tech.programming.Authentication_server.dto.UserRequest;
import com.tech.programming.Authentication_server.entity.User;
import com.tech.programming.Authentication_server.service.JWTService;
import com.tech.programming.Authentication_server.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class UserAuthController {

    @Autowired
    UserService userService;

    @Autowired
    JWTService jwtService;

    @GetMapping
    public String getSessionId(HttpServletRequest httpServletRequest){
        return "welcome to the programming world " + httpServletRequest.getSession().getId();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> verifyLogin(@RequestBody LoginRequest request){
        String token = userService.verifyLogin(request);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/googleLogin")
    public ResponseEntity<LoginResponse> verifyGoogleLogin(@RequestParam String token, HttpServletRequest httpServletRequest){
        User user = userService.verifyGoogleToken(token);
        if(user == null){
            throw new AuthException("Could not verify google account");
        }

        String authToken = jwtService.generateToken(user.getEmail());
        return ResponseEntity.ok(new LoginResponse(authToken));
    }

    @GetMapping("/auth/github/callback")
    public ResponseEntity<LoginResponse> verifyGitHubLogin(@RequestParam("code") String code){
        User user = userService.verifyGitHubLogin(code);
        if(user == null){
            throw new AuthException("Could not verify google account");
        }

        String authToken = jwtService.generateToken(user.getEmail());
        return ResponseEntity.ok(new LoginResponse(authToken));
    }

    @GetMapping("api/names")
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
