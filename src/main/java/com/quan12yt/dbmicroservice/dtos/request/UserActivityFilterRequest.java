package com.quan12yt.dbmicroservice.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActivityFilterRequest {

    @NotEmpty(message = "Host name must not be null/empty")
    private String hostName;
    @NotEmpty(message = "timestamp must not be null/empty")
    private String timestamp;
    @NotNull(message = "Count must not be null")
    private Integer count;
}
