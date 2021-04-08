package com.cronemail.demo;

import com.cronemail.demo.model.UserActivity;
import com.cronemail.demo.repository.IUserActivityRepository;
import com.cronemail.demo.service.IUserActivityService;
import com.cronemail.demo.service.impl.UserActivityServices;
import com.cronemail.demo.utils.Constant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

@SpringBootTest(classes = UserActivityServices.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class UserActivityServicesTest {

    @Autowired
    private IUserActivityService userActivityServices;

    @MockBean
    private IUserActivityRepository iUserActivityRepository;

    private List<UserActivity> userActivityList;

    private final String STR_DATE = "2021-03-23";

    @Before
    public void setup(){
        userActivityList = new ArrayList<>();
        userActivityList.add(UserActivity.builder().userId("PC-QUANG.BUI").url(Constant.HOST_NAME).count(21).date("2021-03-23").totalTime(128.26).build());
        userActivityList.add(UserActivity.builder().userId("PC-QUAN.PHAM").url(Constant.HOST_NAME).count(22).date("2021-03-23").totalTime(128.26).build());
        userActivityList.add(UserActivity.builder().userId("PC-LEN.HO").url(Constant.HOST_NAME).count(30).date("2021-03-23").totalTime(128.26).build());
        userActivityList.add(UserActivity.builder().userId("PC-QUANG.BUI").url(Constant.HOST_NAME).count(22).date("2021-03-23").totalTime(128.26).build());
        userActivityList.add(UserActivity.builder().userId("PC-QUANG.BUI").url(Constant.HOST_NAME).count(30).date("2021-03-23").totalTime(128.26).build());
        userActivityList.add(UserActivity.builder().userId("PC-LEN.HO").url(Constant.HOST_NAME).count(50).date("2021-03-23").totalTime(128.26).build());
    }

    @Test
    public void selectUserActivityListSuccessTest(){
        when(iUserActivityRepository.selectUserActivityBelongToFilter(Constant.HOST_NAME,
                Constant.MAX_TIME_ACCESS_FB,
                STR_DATE)).thenReturn(userActivityList);
        List<UserActivity> actual = userActivityServices.selectUserActivityList(Constant.HOST_NAME,
                Constant.MAX_TIME_ACCESS_FB,
                STR_DATE);

        List<UserActivity> expectedRs = new ArrayList<>();
        expectedRs.add(UserActivity.builder().userId("PC-LEN.HO").url(Constant.HOST_NAME).count(50).date("2021-03-23").totalTime(128.26).build());
        expectedRs.add(UserActivity.builder().userId("PC-QUAN.PHAM").url(Constant.HOST_NAME).count(22).date("2021-03-23").totalTime(128.26).build());
        expectedRs.add(UserActivity.builder().userId("PC-QUANG.BUI").url(Constant.HOST_NAME).count(30).date("2021-03-23").totalTime(128.26).build());
        assertSame(actual.size(), expectedRs.size());
        assertThat(actual, is(expectedRs));
    }

    @Test
    public void selectUserActivityListEmptyTest(){
        when(iUserActivityRepository.selectUserActivityBelongToFilter(Constant.HOST_NAME,
                Constant.MAX_TIME_ACCESS_FB,
                STR_DATE)).thenReturn(new ArrayList<>());
        List<UserActivity> actual = userActivityServices.selectUserActivityList(Constant.HOST_NAME,
                Constant.MAX_TIME_ACCESS_FB,
                STR_DATE);
        assertSame(actual.size(), 0);
    }
    
}
