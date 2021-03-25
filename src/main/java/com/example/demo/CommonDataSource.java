package com.example.demo;


import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.sql.SQLException;

public class CommonDataSource {

    public static DataSource getDataSource() throws SQLException {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://199.30.30.57:5432/postgres")
                .password("s3training")
                .username("postgres")
                .driverClassName("org.postgresql.Driver")
                .build();
    }

}
