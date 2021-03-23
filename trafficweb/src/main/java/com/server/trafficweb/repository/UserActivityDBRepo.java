/**
 * 
 */

package com.server.trafficweb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.server.trafficweb.models.UserActivityDB;


/**
 * @author nhut.to
 *
 */
@Repository
public interface UserActivityDBRepo extends CrudRepository<UserActivityDB, String> {

}
