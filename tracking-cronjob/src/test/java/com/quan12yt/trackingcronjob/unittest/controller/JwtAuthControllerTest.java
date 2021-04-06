package com.quan12yt.trackingcronjob.unittest.controller;

import com.quan12yt.trackingcronjob.service.AccountsService;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootTest
@RunWith(SpringRunner.class)
@EnableWebMvc
@AutoConfigureMockMvc
public class JwtAuthControllerTest {

    @MockBean
    AccountsService accountsService;
}
