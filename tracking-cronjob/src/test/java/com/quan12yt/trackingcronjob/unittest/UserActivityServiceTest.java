package com.quan12yt.trackingcronjob.unittest;

import com.quan12yt.trackingcronjob.TrackingCronjobApplication;
import com.quan12yt.trackingcronjob.exception.DataNotFoundException;
import com.quan12yt.trackingcronjob.exception.VariablesUnacceptedException;
import com.quan12yt.trackingcronjob.model.UserActivity;
import com.quan12yt.trackingcronjob.repository.UserActivityRepository;
import com.quan12yt.trackingcronjob.service.UserActivityService;
import com.quan12yt.trackingcronjob.service.imp.EmailService;
import com.quan12yt.trackingcronjob.service.imp.UserActivityServiceImp;
import com.quan12yt.trackingcronjob.util.ValidateUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest(classes = TrackingCronjobApplication.class)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class UserActivityServiceTest {

    @Autowired
    private UserActivityServiceImp activityService;
    @MockBean
    private UserActivityRepository activityRepository;

    private List<UserActivity> activityList = new ArrayList<>();
    private List<String> listUserId = new ArrayList<>();

    private UserActivity activity1;
    private UserActivity activity2;
    private UserActivity activity3;

    @Before
    public void setup() {
        activity1 = new UserActivity("quan.pham", "facebook.com", "03-23-2021", 123L, 11);
        activity2 = new UserActivity("len.ho", "facebook.com", "03-23-2021", 123L, 11);
        activity3 = new UserActivity("quang.bui", "facebook.com", "03-23-2021", 123L, 11);

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


}
