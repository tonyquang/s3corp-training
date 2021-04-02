package com.quan12yt.trackingcronjob.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActivityResponse {

    private String userId;
    private Integer count;
    private Long totalTime;

}
