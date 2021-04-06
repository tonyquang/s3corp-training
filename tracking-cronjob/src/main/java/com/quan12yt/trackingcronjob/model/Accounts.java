package com.quan12yt.trackingcronjob.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "accounts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Accounts {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "account_role", joinColumns = @JoinColumn(name = "account_id")
            , inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnoreProperties("accounts")
    private Set<Roles> roles;
}
