
package com.server.trafficweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.server.trafficweb.models.Users;

/**
 * @author nhut.to
 *
 */
@Repository
public interface UserRepository extends JpaRepository<Users, String> {
}