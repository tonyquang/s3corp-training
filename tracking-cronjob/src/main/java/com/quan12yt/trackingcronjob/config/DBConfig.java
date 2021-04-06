package com.quan12yt.trackingcronjob.config;

import com.quan12yt.demo.dbconfig.CommonDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@EnableAutoConfiguration
public class DBConfig {

    @Bean
    @Primary
    public DataSource dataSource() throws SQLException {
        return CommonDataSource.getHikariDataSource();
    }

}
