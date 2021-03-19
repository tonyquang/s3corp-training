package com.quan12yt.trackingcronjob.repository;

import com.quan12yt.trackingcronjob.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, String> {

}
