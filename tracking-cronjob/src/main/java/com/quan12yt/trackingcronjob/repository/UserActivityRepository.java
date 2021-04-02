package com.quan12yt.trackingcronjob.repository;

import com.quan12yt.trackingcronjob.model.UserActivity;
import com.quan12yt.trackingcronjob.model.UserActivityKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, UserActivityKey> {

    @Query(value = "select u.user_id \n" +
            "from user_activity u\n" +
            "where (u.url = :url and u.date = :date) and u.count >10 ", nativeQuery = true)
    List<String> getViolatedUserByDateAndUrl(@Param("url") String url, @Param("date") String date);

    @Query(value = "select *   \n" +
            "from user_activity u\n" +
            "where (u.url like %:url% and u.user_id = :userid) and u.date like %:month%", nativeQuery = true)
    List<UserActivity> findByUserIdAndUrlAndMonth(@Param("url") String url, @Param("userid") String userId, @Param("month") String month);
}
