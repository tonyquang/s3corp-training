package com.cronemail.demo.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user_activity")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActivity {

    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "count")
    private int count;

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
