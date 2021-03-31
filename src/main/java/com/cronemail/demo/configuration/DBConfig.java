package com.cronemail.demo.configuration;

import com.quan12yt.demo.CommonDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class DBConfig {

    @Bean
    public DataSource dataSource(){
        return CommonDataSource.getHikariDataSource();
    }

}
