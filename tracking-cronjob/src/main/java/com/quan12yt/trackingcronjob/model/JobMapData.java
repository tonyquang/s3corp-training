package com.quan12yt.trackingcronjob.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobMapData implements Serializable {
    private Long initialOffsetMs;
    private UserActivity userActivity;
    private Integer accessCount;
    private Integer time;

   }
