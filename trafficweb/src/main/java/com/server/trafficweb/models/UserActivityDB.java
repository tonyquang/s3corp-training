package com.server.trafficweb.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nhut.to
 *
 */
@Entity
@IdClass(MyKey.class)
@Table(name = "user_activity")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserActivityDB {

	@Id
    @Column(name = "user_id")
	private String userId;
	@Id
	private String url;
	@Id
	private String date;

	private int count;
	private double total_time;
}
