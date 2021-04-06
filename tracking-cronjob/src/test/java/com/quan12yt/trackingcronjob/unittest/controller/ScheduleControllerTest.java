package com.quan12yt.trackingcronjob.unittest.controller;

import com.easyquartz.scheduler.ScheduleService;
import com.quan12yt.trackingcronjob.controller.ScheduleController;
import com.quan12yt.trackingcronjob.service.JobService;
import com.quan12yt.trackingcronjob.service.imp.JobServiceImp;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.LocalDateTime;
import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {JobServiceImp.class, ScheduleController.class})
@AutoConfigureMockMvc
@EnableWebMvc
public class ScheduleControllerTest {

    @MockBean
    JobServiceImp jobService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void runSendingEmailJobSucceed() throws Exception {
        Mockito.when(jobService.runSendEmailJoh()).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/schedule/send")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath
                        ("$.message", CoreMatchers.is("Run Send Email job successfully")));

    }

    @Test
    public void runSendingEmailJobFailed() throws Exception {
        Mockito.when(jobService.runSendEmailJoh()).thenReturn(false);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/schedule/send")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath
                        ("$.message", CoreMatchers.is("Some thing gone wrong !!")));

    }
}
