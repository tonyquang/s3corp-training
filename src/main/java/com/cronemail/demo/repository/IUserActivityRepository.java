package com.cronemail.demo.repository;

import com.cronemail.demo.model.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IUserActivityRepository extends JpaRepository<UserActivity, Long> {
    /*
    Select User activity belong to filter host, count and time stamp
    @param hostName: facebook, youtube,...
    @param count: 1, 10, 20,...
    @param timeStamp: yy.MM.dd
     */
    @Query(value = "Select * " +
            "from user_activity " +
            "where url like %?1% " +
            "and count >= ?2 " +
            "and \"date\" like ?3%",
            nativeQuery = true)
    List<UserActivity> selectUserActivityBelongToFilter(String hostName, int count, String timeStamp);
}
