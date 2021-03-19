package com.quan12yt.trackingcronjob.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobInfo implements Serializable {
    private Integer totalFireCount;
    private Integer remainingFireCount;
    private Boolean runForever;
    private Long initialOffsetMs;
    private String callbackData;
    private UserActivity userActivity;
    private Integer accessCount;
    private Integer time;

   }
