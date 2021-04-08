package com.quan12yt.dbmicroservice.services.imp;

import com.quan12yt.dbmicroservice.models.Users;
import com.quan12yt.dbmicroservice.repositories.UserRepository;
import com.quan12yt.dbmicroservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Users> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<Users> getUsersByListId(List<String> userIds) {
        List<Users> usersList;
        usersList = userRepository.findAllById(userIds);
        return usersList;
    }

    @Override
    public Optional<Users> getUserByUserId(String userId){
        return userRepository.findById(userId);
    }


}
