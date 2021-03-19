package com.quan12yt.trackingcronjob.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user_activity")
@IdClass(UserActivityKey.class)
public class UserActivity implements Serializable {
    @Id
    @Column(name = "user_id")
    private String userId;
    @Id
    @Column(name = "url")
    private String url;
    @Id
    @Column(name = "date")
    private String date;
    @Column(name = "total_time")
    private Long totalTime;
    @Column(name = "access_count")
    private Integer accessCount;
}
