package com.cronemail.demo.service.impl;

import com.cronemail.demo.model.UserActivity;
import com.cronemail.demo.repository.IUserActivityRepository;
import com.cronemail.demo.service.IUserActivityService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserActivityServices implements IUserActivityService {

    @Autowired
    IUserActivityRepository userActivityRepository;

    @Override
    public List<UserActivity> selectUserActivityList(String hostName,int count, String timeStamp) {
        List<UserActivity> userActivityList = userActivityRepository.selectUserActivityBelongToFilter(hostName, count, timeStamp);
        Map<String, List<UserActivity>> userActivityGroupByName = userActivityList
                .stream()
                .collect(
                        Collectors.groupingBy(ua ->{
                            return ua.getUserId();
                        })
                );
        List<UserActivity> finalUserActivityList = new ArrayList<>();
        for (Map.Entry<String, List<UserActivity>> entry : userActivityGroupByName.entrySet()) {
            finalUserActivityList.add(entry.getValue().stream().max(Comparator.comparing(UserActivity::getCount)).orElseThrow(NoSuchElementException::new));
        }
        return finalUserActivityList;
    }
}
