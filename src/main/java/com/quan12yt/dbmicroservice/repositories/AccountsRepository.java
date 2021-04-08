package com.quan12yt.dbmicroservice.repositories;


import com.quan12yt.dbmicroservice.models.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long> {
    Optional<Accounts> findByUserName(String userName);
}
