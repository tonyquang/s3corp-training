package com.cronemail.demo.repository;

import com.cronemail.demo.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IUsersRepository extends JpaRepository<Users, Long> {
    Users findByUserId(String userID);
}
