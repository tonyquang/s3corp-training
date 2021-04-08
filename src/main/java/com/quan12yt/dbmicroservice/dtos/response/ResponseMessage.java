package com.quan12yt.dbmicroservice.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ResponseMessage {

    private String message;
    private String timestamp;

}
