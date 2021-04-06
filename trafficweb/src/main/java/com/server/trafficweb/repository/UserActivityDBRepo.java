/**
 * 
 */

package com.server.trafficweb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.server.trafficweb.models.UserActivityDB;

/**
 * @author nhut.to
 *
 */
@Repository
public interface UserActivityDBRepo extends JpaRepository<UserActivityDB, String> {

	Optional<UserActivityDB> findByUserIdAndUrlAndDate(String userId, String url, String date);
}
