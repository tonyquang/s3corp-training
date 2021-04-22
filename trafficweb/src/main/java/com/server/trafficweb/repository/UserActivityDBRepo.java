/**
 * 
 */

package com.server.trafficweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.server.trafficweb.models.MyKey;
import com.server.trafficweb.models.UserActivityDB;

/**
 * @author nhut.to
 *
 */
@Repository
public interface UserActivityDBRepo extends JpaRepository<UserActivityDB, MyKey> {
}
