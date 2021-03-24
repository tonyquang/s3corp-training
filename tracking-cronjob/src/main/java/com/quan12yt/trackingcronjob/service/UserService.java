package com.quan12yt.trackingcronjob.service;


import com.quan12yt.trackingcronjob.model.Users;

import java.util.List;

public interface UserService {

    List<Users> getUsers();

    List<Users> getUsersById(List<String> userId);

}
