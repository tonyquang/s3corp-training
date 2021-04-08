package com.quan12yt.dbmicroservice.services;

import com.quan12yt.dbmicroservice.models.Users;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<Users> getUsers();

    List<Users> getUsersByListId(List<String> userId);

    public Optional<Users> getUserByUserId(String userId);
}
