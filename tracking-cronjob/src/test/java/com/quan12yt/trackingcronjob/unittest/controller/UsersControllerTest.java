package com.quan12yt.trackingcronjob.unittest.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.quan12yt.trackingcronjob.controller.UsersController;
import com.quan12yt.trackingcronjob.exception.GlobalExceptionHandler;
import com.quan12yt.trackingcronjob.model.Users;
import com.quan12yt.trackingcronjob.repository.UserRepository;
import com.quan12yt.trackingcronjob.service.UserService;
import com.quan12yt.trackingcronjob.service.imp.UserServiceImp;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {UserServiceImp.class, UserRepository.class, UsersController.class, GlobalExceptionHandler.class})
@AutoConfigureMockMvc
@EnableWebMvc
public class UsersControllerTest {

    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final List<Users> usersList = new ArrayList<>();
    private final List<String> listUserId = new ArrayList<>();


    @Before
    public void setup() {
        Users users1 = new Users("PC-QUANPHAM$", "quan12yt@gmail.com");
        Users users2 = new Users("PC-LENHO$", "lenho@gmail.com");
        Users users3 = new Users("PC-QUANGBUI$", "quangbui@gmail.com");
        usersList.add(users1);
        usersList.add(users2);
        usersList.add(users3);
        listUserId.add(users1.getUserId());
        listUserId.add(users2.getUserId());
        listUserId.add(users3.getUserId());
    }

    @Test
    public void getAllUsersSucceed() throws Exception {
        when(userService.getUsers()).thenReturn(usersList);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/users")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getAllUsersEmpty() throws Exception {
        when(userService.getUsers()).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/users")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void getUsersByIdEmpty() throws Exception {
     //   when(userService.getUsersById(anyList())).thenReturn(Collections.emptyList());
        String json = objectMapper.writeValueAsString(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath
                        ("$.message", CoreMatchers.is("List user id cant be null or empty !!")));
    }

    @Test
    public void getUsersByIdSucceed() throws Exception {
        when(userService.getUsersById(listUserId)).thenReturn(usersList);
        String json = objectMapper.writeValueAsString(listUserId);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getUsersByIdNoContent() throws Exception {
        when(userService.getUsersById(listUserId)).thenReturn(Collections.emptyList());
        String json = objectMapper.writeValueAsString(listUserId);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isNoContent());


    }

}
