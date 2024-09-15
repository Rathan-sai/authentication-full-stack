package com.tech.programming.Authentication_server.service;

import com.tech.programming.Authentication_server.dto.LoginRequest;
import com.tech.programming.Authentication_server.dto.UserRequest;
import com.tech.programming.Authentication_server.dto.UserResponse;
import com.tech.programming.Authentication_server.entity.User;
import com.tech.programming.Authentication_server.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public UserResponse addUser(UserRequest request){
        User user = request.toEntity(null);
        userRepository.save(user);
        return new UserResponse(user);
    }

    public UserResponse getUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()){
            throw new IllegalArgumentException("Invalid User ID");
        }
        return new UserResponse(optionalUser.get());
    }

    public UserResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());

        if(user == null) {
            throw new IllegalArgumentException("Invalid Email Id");
        }else if(!Objects.equals(user.getPasswordHash(), loginRequest.getPassword())){
            throw new IllegalArgumentException("Invalid password");
        }

        return new UserResponse(user);
    }
}
