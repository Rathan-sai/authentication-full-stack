package com.tech.programming.Authentication_server.dto;

import com.tech.programming.Authentication_server.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String passwordHash;
    private String description;

    public UserResponse(User user) {
        if(user == null){
            return;
        }
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.passwordHash = user.getPasswordHash();
        this.description = user.getDescription();
    }
}
