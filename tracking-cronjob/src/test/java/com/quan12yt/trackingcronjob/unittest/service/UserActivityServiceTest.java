package com.quan12yt.trackingcronjob.unittest.service;

import com.quan12yt.trackingcronjob.exception.DataNotFoundException;
import com.quan12yt.trackingcronjob.exception.VariablesUnacceptedException;
import com.quan12yt.trackingcronjob.model.UserActivity;
import com.quan12yt.trackingcronjob.repository.UserActivityRepository;
import com.quan12yt.trackingcronjob.service.UserActivityService;
import com.quan12yt.trackingcronjob.service.imp.UserActivityServiceImp;
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

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@RunWith(SpringRunner.class)
public class UserActivityServiceTest {

    @Autowired
    private UserActivityService activityService;
    @MockBean
    private UserActivityRepository activityRepository;

    private List<UserActivity> activityList = new ArrayList<>();
    private List<String> listUserId = new ArrayList<>();

    private UserActivity activity1;
    private UserActivity activity2;
    private UserActivity activity3;

    @TestConfiguration
    public static class EmailServiceConfiguration{
        @Bean
        UserActivityServiceImp activityServiceImp(){
            return new UserActivityServiceImp();
        }

    }

    @Before
    public void setup() {
        activity1 = new UserActivity("quan.pham", "facebook.com", "2021-04-05", 123L, 11);
        activity2 = new UserActivity("len.ho", "facebook.com", "2021-04-05", 123L, 11);
        activity3 = new UserActivity("quang.bui", "facebook.com", "2021-04-05", 123L, 11);

        activityList.add(activity1);
        activityList.add(activity3);
        activityList.add(activity2);

        listUserId.add(activity1.getUserId());
        listUserId.add(activity2.getUserId());
        listUserId.add(activity3.getUserId());
    }

    @Test
    public void getViolatedSuccess() {
        when(activityRepository.getViolatedUserByDateAndUrl("facebook.com", "03-23-2021")).thenReturn(listUserId);
        List<String> result = activityService.getViolatedUserByDateAndUrl("facebook.com", "03-23-2021");

        Assert.assertEquals(result.size(), listUserId.size());
        Assert.assertSame(result.get(0), listUserId.get(0));
    }

    @Test
    public void getEmptyViolated() {
        when(activityRepository.getViolatedUserByDateAndUrl("facebook.com", "03-23-2021")).thenReturn(Collections.emptyList());

        Throwable ex = Assert.assertThrows(DataNotFoundException.class, () -> activityService.getViolatedUserByDateAndUrl("facebook.com", "03-23-2021"));
        Assert.assertSame("Can't find any violated user", ex.getMessage());
    }

    @Test
    public void getViolatedUnacceptedVariables() {
        Throwable ex = Assert.assertThrows(VariablesUnacceptedException.class, () -> activityService.getViolatedUserByDateAndUrl("", ""));
        Assert.assertSame("Url or date is null or empty", ex.getMessage());
    }

    @Test
    public void findByMonthSuccess() {
        Object[][] objects = new Object[1][3];
        objects[0][0] = "05";
        objects[0][1] = 11;
        objects[0][2] = 123L;
        when(activityRepository.findByUserIdAndUrlAndMonth("facebook.com","PC-QUANPHAM$", "04")).thenReturn(Collections.singletonList(activityList.get(0)));
        Object[][] result = activityService.getViolatedUserByDateAndUrlAndMonth("facebook.com", "PC-QUANPHAM$", "04");

        Assert.assertEquals(objects[0][0], result[0][0]);
        Assert.assertEquals(objects[0][1], result[0][1]);
        Assert.assertEquals(objects[0][2], result[0][2]);
    }
    @Test
    public void findByMonthEmptyData() {
        when(activityRepository.findByUserIdAndUrlAndMonth("facebook.com","PC-QUANPHAM$", "04")).thenReturn(Collections.emptyList());
        Throwable ex = Assert.assertThrows(DataNotFoundException.class, () -> activityService.getViolatedUserByDateAndUrlAndMonth("facebook.com", "PC-QUANPHAM$", "04"));
        Assert.assertSame("Can't find any user activities for the given data", ex.getMessage());

    }

    @Test
    public void findByMonthInvalidMonth() {
        Throwable ex = Assert.assertThrows(VariablesUnacceptedException.class, () -> activityService.getViolatedUserByDateAndUrlAndMonth("facebook.com","PC-QUANPHAM$", "034"));
        Assert.assertSame("Month value is invalid", ex.getMessage());
    }

    @Test
    public void findByMonthUnacceptedVariables() {
        Throwable ex = Assert.assertThrows(VariablesUnacceptedException.class, () -> activityService.getViolatedUserByDateAndUrlAndMonth("facebook.com",null, "03"));
        Assert.assertSame("Url or date is null or empty", ex.getMessage());
    }


}
