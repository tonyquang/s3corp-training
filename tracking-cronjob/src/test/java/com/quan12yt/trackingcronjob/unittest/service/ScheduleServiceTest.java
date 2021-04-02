package com.quan12yt.trackingcronjob.unittest.service;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.easyquartz.scheduler.ScheduleService;
import com.quan12yt.trackingcronjob.job.SendEmailJob;
import com.quan12yt.trackingcronjob.model.JobMapData;
import com.quan12yt.trackingcronjob.unittest.ExampleJob;
import com.quan12yt.trackingcronjob.util.ValueConstant;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.*;

import static org.mockito.Mockito.*;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
public class ScheduleServiceTest {

    @Autowired
    ScheduleService scheduleService;
    @MockBean
    private SendEmailJob sendEmailJob;
    @MockBean
    SchedulerFactoryBean schedulerFactoryBean;
    @MockBean
    Scheduler scheduler;

    @TestConfiguration
    public static class EmailServiceConfiguration{
        
//        @Bean
//        public ScheduleService scheduleService(){
//            return  new ScheduleService(scheduler);
//        }
    }

    private Trigger trigger;
    private JobDetail jobDetail;

    private JobMapData info;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(schedulerFactoryBean.getScheduler()).thenReturn(scheduler);

        info = new JobMapData();
        info.setInitialOffsetMs(1000L);
        info.setAccessCount(0);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testRunSchedule() throws SchedulerException, InterruptedException {
        Logger logger =(Logger) LoggerFactory.getLogger(ExampleJob.class);

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();

        logger.addAppender(listAppender);
        scheduleService.schedule(ExampleJob.class, "test", ValueConstant.CRON_EXPRESSION);
        Thread.sleep(10000);
        List<ILoggingEvent> logsList = listAppender.list;

        Assert.assertEquals( "asdasd", logsList.get(0).getMessage());
    }
}

