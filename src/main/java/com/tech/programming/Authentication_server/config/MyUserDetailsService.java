package com.tech.programming.Authentication_server.config;

import com.tech.programming.Authentication_server.entity.User;
import com.tech.programming.Authentication_server.entity.UserPrincipal;
import com.tech.programming.Authentication_server.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByName(username);
        if(user == null){
            System.out.println("user not found");
            throw new UsernameNotFoundException("user not found: "+username);
        }

        return new UserPrincipal(user);
    }
}
