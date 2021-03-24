package com.quan12yt.trackingcronjob.unittest;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.quan12yt.trackingcronjob.TrackingCronjobApplication;
import com.quan12yt.trackingcronjob.job.SendEmailJob;
import com.quan12yt.trackingcronjob.model.JobMapData;
import com.quan12yt.trackingcronjob.service.ScheduleService;
import com.quan12yt.trackingcronjob.util.JobUtils;
import org.assertj.core.api.BDDAssumptions;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.quartz.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@SpringBootTest(classes = TrackingCronjobApplication.class)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class ScheduleServiceTest {

    @Autowired
    ScheduleService scheduleService;
    @MockBean
    private SendEmailJob sendEmailJob;
    @Mock
    SchedulerFactoryBean schedulerFactoryBean;
    @Mock
    Scheduler scheduler;

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
        scheduleService.schedule(ExampleJob.class, info);
        Thread.sleep(10000);
        List<ILoggingEvent> logsList = listAppender.list;

        Assert.assertEquals( "asdasd", logsList.get(0).getMessage());
    }
}

