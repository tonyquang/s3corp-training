package com.quan12yt.dbmicroservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user_activity")
@IdClass(UserActivityKey.class)
public class UserActivity implements Serializable {
    @Id
    @Column(name = "user_id")
    @NotEmpty(message = "userId must not be Null/Empty")
    private String userId;

    @Id
    @Column(name = "url")
    @NotEmpty(message = "url must not be Null/Empty")
    private String url;

    @Id
    @Column(name = "date")
    @NotEmpty(message = "date must not be Null/Empty")
    private String date;

    @NotNull(message = "Total time must not be Null/Empty")
    @Column(name = "total_time")
    @Positive(message = "Total time must be positive")
    private Long totalTime;

    @NotNull(message = "Count must not be Null/Empty")
    @Column(name = "count")
    @Positive(message = "Total time must be positive")
    private Integer count;
}

