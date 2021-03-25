package com.server.trafficweb.configuration;

import java.sql.SQLException;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.quan12yt.demo.CommonDataSource;

@Configuration
public class TrafficWebConfiguration {

	@Bean
	public DataSource dataSource() throws SQLException {
		return CommonDataSource.getDataSource();
	}
}
