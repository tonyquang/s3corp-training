package com.cronemail.demo;

import com.cronemail.demo.model.Users;
import com.cronemail.demo.repository.IUsersRepository;
import com.cronemail.demo.service.IUserService;
import com.cronemail.demo.service.impl.UsersServices;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

@SpringBootTest(classes = UsersServices.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class UserServicesTest {

    @Autowired
    IUserService usersServices;

    @MockBean
    IUsersRepository usersRepository;

    private String userID;
    private String expectedEmail;
    private Users user;

    @Before
    public void setup(){
        userID = System.getProperty("user.name");
        expectedEmail = "quangbui14041999@gmail.com";
        user = Users.builder().userId(userID).email("quangbui14041999@gmail.com").build();
    }

    @Test
    public void selectEmailFromUserIDSuccessTest(){
        when(usersRepository.findByUserId(userID)).thenReturn(user);
        String actualEmail = usersServices.selectEmailFromUserID(userID);
        assertSame(actualEmail, expectedEmail);
    }

    @Test
    public void selectEmailFromUserIDNotFound(){
        when(usersRepository.findByUserId(userID)).thenReturn(null);
        String actualEmail = usersServices.selectEmailFromUserID(userID);
        assertEquals(actualEmail, "");
    }
}
