package com.quan12yt.dbmicroservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserActivityKey implements Serializable {
    private String userId;
    private String url;
    private String date;
}
