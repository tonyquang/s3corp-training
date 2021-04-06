package com.quan12yt.trackingcronjob.repository;

import com.quan12yt.trackingcronjob.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByRoleName(String roleName);
}
