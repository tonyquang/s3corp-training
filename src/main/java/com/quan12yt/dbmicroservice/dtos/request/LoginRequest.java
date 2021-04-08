package com.quan12yt.dbmicroservice.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotEmpty(message = "User name must not be null/empty")
    private String userName;
    @NotEmpty(message = "Password must not be null/empty")
    private String password;

}