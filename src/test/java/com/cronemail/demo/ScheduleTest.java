package com.cronemail.demo;

import com.cronemail.demo.utils.Constant;
import com.easyquartz.scheduler.ScheduleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest(classes = DemoApplication.class)
@RunWith(SpringRunner.class)
@TestPropertySource("/TestSchedule.properties")
@AutoConfigureMockMvc
public class ScheduleTest {

    @Autowired
    ScheduleService scheduleService;

    private JobTest jobTest;
    // Run every 5s
    private static final String CRON_EXPRESSION = "0/10 * * * * ? *";
    @Before
    public void setup(){
        jobTest = new JobTest();
    }

    @Test
    public void scheduleQuartzTest(){
        scheduleService.schedule(jobTest.getClass(), Constant.GROUP_ID ,CRON_EXPRESSION);
        try {
            Thread.currentThread().sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(jobTest.count > 0);
    }

}
