package com.cronemail.demo.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class Users {

    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "users")
    @EqualsAndHashCode.Exclude // useless this field for equals and hashcode
    @ToString.Exclude // useless for toString()
    List<UserActivity> listUserActivity;

}
