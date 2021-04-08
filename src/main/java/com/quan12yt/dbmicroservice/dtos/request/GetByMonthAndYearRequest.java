package com.quan12yt.dbmicroservice.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetByMonthAndYearRequest {

    @NotEmpty(message = "Host name must not be null/empty")
    private String hostName;
    @NotEmpty(message = "UserName must not be null/empty")
    private String userName;
    @NotEmpty(message = "Year must not be null/empty")
    private String year;
    @NotNull(message = "Month must not be null")
    private String month;
}
