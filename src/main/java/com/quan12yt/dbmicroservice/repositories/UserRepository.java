package com.quan12yt.dbmicroservice.repositories;


import com.quan12yt.dbmicroservice.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, String> {

}
