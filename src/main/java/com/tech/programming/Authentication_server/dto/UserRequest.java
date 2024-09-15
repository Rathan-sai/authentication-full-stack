package com.tech.programming.Authentication_server.dto;

import com.tech.programming.Authentication_server.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    private String name;
    private String email;
    private String passwordHash;
    private String description;

    public User toEntity() {
        return toEntity(null);
    }

    public User toEntity(User user){
        if(user == null){
            user = new User();
        }
        user.setName(this.getName());
        user.setEmail(this.getEmail());
        user.setPasswordHash(this.getPasswordHash());
        user.setDescription(this.getDescription());

        return user;
    }
}
