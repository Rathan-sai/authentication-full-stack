package com.tech.programming.Authentication_server.jpa;

import com.tech.programming.Authentication_server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByName(String name);

    User findByEmail(String email);
}
