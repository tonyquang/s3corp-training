package com.server.trafficweb;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.server.trafficweb.constants.IConfigConstants;
import com.server.trafficweb.service.UserActivityService;

@SpringBootTest
public class TestSaveDocument {

	private String USER_ID = "user_id";
	private String URL = "url";
	private String TIME_STAMP = "@timestamp";

	@Autowired
	UserActivityService service;

	RestHighLevelClient client;

	@BeforeEach
	void setup() {
		client = service.createRestHighLevelClient(IConfigConstants.HOSTNAME, IConfigConstants.PORT,
				IConfigConstants.PROTOCOL);
	}

	@Test
	void testSaveDocument1() {
		String indexName = "proxyinfo";
		List<String> fieldNames = Stream.of(USER_ID, URL, TIME_STAMP).collect(Collectors.toList());

		try {
			boolean isSave = service.saveDocument(client, indexName, fieldNames);
			assertFalse(isSave);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void testSaveDocument2() {
		String indexName = "proxyinfo";
		try {
			boolean isSave = service.saveDocument(client, indexName, null);
			assertFalse(isSave);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void testSaveDocument3() {
		String indexName = "proxyinfo";
		List<String> fieldNames = Stream.of(USER_ID, URL, TIME_STAMP).collect(Collectors.toList());

		try {
			boolean isSave = service.saveDocument(null, indexName, fieldNames);
			assertFalse(isSave);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void testSaveDocument4() {
		String indexName = null;
		List<String> fieldNames = Stream.of(USER_ID, URL, TIME_STAMP).collect(Collectors.toList());

		try {
			boolean isSave = service.saveDocument(client, indexName, fieldNames);
			assertFalse(isSave);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void testSaveDocument5() {
		String indexName = "";
		List<String> fieldNames = Stream.of(USER_ID, URL, TIME_STAMP).collect(Collectors.toList());
		try {
			boolean isSave = service.saveDocument(client, indexName, fieldNames);
			assertFalse(isSave);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void testSaveDocument6() {
		String indexName = "proxyinfo";
		List<String> fieldNames = Stream.of(USER_ID, URL, "aaa").collect(Collectors.toList());

		try {
			boolean isSave = service.saveDocument(client, indexName, fieldNames);
			assertFalse(isSave);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void testSaveDocument7() {
		String indexName = "proxyinfo";
		List<String> fieldNames = Stream.of(USER_ID, "aaa", TIME_STAMP).collect(Collectors.toList());

		try {
			boolean isSave = service.saveDocument(client, indexName, fieldNames);
			assertFalse(isSave);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void testSaveDocument8() {
		String indexName = "proxyinfo";
		List<String> fieldNames = Stream.of("aaa", URL, TIME_STAMP).collect(Collectors.toList());

		try {
			boolean isSave = service.saveDocument(client, indexName, fieldNames);
			assertFalse(isSave);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
