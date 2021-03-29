package com.server.trafficweb;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.server.trafficweb.models.UserActivityDB;
import com.server.trafficweb.service.UserActivityService;

@SpringBootTest
public class TestSaveUserActivity {

	@Autowired
	UserActivityService service;

	@Test
	void testSaveUserActivity1() {
		String date = service.getCurrentTime();
		String url = "stackoverflow.com";
		String userid = "PC-NHUTTO$";
		int count = 9;
		double time = 10.0;
		UserActivityDB user = service.saveUserActivity(date, userid, url, count, time);
		assertEquals("PC-NHUTTO$", user.getUser_id());
	}

//	@Test
//	void testSaveUserActivity2() {
//		String date = service.getCurrentTime();
//		String url = null;
//		String userid = "ttmnhut";
//		int count = 9;
//		double time = 10.0;
//		UserActivityDB user = service.saveUserActivity(date, userid, url, count, time);
//		assertNull(user);
//	}
//
//	@Test
//	void testSaveUserActivity3() {
//		String date = service.getCurrentTime();
//		String url = "stackoverflow.com";
//		String userid = null;
//		int count = 9;
//		double time = 10.0;
//		UserActivityDB user = service.saveUserActivity(date, userid, url, count, time);
//		assertNull(user);
//	}
//
//	@Test
//	void testSaveUserActivity4() {
//		String date = null;
//		String url = "stackoverflow.com";
//		String userid = "nhuttm";
//		int count = 9;
//		double time = 10.0;
//		UserActivityDB user = service.saveUserActivity(date, userid, url, count, time);
//		assertNull(user);
//	}

}
