package com.quan12yt.dbmicroservice.config;

import com.quan12yt.demo.dbconfig.CommonDataSource;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration
public class DBConfig {

    @Bean
    @Primary
    public DataSource dataSource(){
        return CommonDataSource.getHikariDataSource();
    }

}
