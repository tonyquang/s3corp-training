package com.quan12yt.trackingcronjob.repository;

import com.quan12yt.trackingcronjob.model.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long> {
    Optional<Accounts> findByUserName(String userName);
}
