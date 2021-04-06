package com.quan12yt.trackingcronjob.unittest.service;

import com.quan12yt.trackingcronjob.exception.DataNotFoundException;
import com.quan12yt.trackingcronjob.exception.VariablesUnacceptedException;
import com.quan12yt.trackingcronjob.model.Users;
import com.quan12yt.trackingcronjob.repository.UserRepository;
import com.quan12yt.trackingcronjob.service.UserService;
import com.quan12yt.trackingcronjob.service.imp.UserServiceImp;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;

    private List<Users> usersList = new ArrayList<>();
    private List<String> listUserId = new ArrayList<>();

    private Users user1;
    private Users user2;
    private Users user3;

    @TestConfiguration
    public static class EmailServiceConfiguration{
        @Bean
        UserServiceImp userServiceImp(){
            return new UserServiceImp();
        }
    }

    @Before
    public void setup() {
        user1 = new Users("PC-QUANPHAM$", "quan12yt@gmail.com");
        user2 = new Users("PC-QUANGBUI$", "quang@gmail.com");
        user3 = new Users("PC-LENHO$", "lenho@gmail.com");

        usersList.add(user1);
        usersList.add(user2);
        usersList.add(user3);

        listUserId.add(user1.getUserId());
        listUserId.add(user2.getUserId());
        listUserId.add(user3.getUserId());
    }

    @Test
    public void getAllUsersSucceed(){
        when(userRepository.findAll()).thenReturn(usersList);
        List<Users> resultsUsers = userService.getUsers();
        Assert.assertEquals(usersList.size(), resultsUsers.size());
    }


    @Test
    public void getUserByIdSucceed(){
        when(userRepository.findAllById(listUserId)).thenReturn(usersList);
        List<Users> resultsUsers = userService.getUsersById(listUserId);
        Assert.assertEquals(usersList.size(), resultsUsers.size());
    }

}
