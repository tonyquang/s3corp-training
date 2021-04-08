package com.quan12yt.dbmicroservice.dtos.response;

import com.quan12yt.dbmicroservice.models.UserActivity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveActivitySucceedResponse {

    private String message;
    private String timestamp;
    private UserActivity userActivity;
}
