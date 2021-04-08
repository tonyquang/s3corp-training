package com.cronemail.demo.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_activity")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IdClass(IDUserActivity.class)
public class UserActivity implements Serializable {

    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Id
    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "count")
    private int count;

    @Id
    @Column(name = "date", nullable = false)
    private String date;

    @Column(name = "total_time")
    private double totalTime;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Users users;
}
