//package com.quan12yt.trackingcronjob.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
//import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.quartz.SchedulerFactoryBean;
//
//import javax.sql.DataSource;
//import java.sql.SQLException;
//import java.util.Properties;
//
//@Configuration
//@EnableAutoConfiguration
//public class QuartConfig {
//
//    @Autowired
//    DataSource dataSource;
//
//    @Bean
//    @QuartzDataSource
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public DataSource quartzDataSource() throws SQLException {
//        return dataSource;
//    }
//
//    @Bean
//    public SchedulerFactoryBeanCustomizer schedulerFactoryBeanCustomizer()
//    {
//        return new SchedulerFactoryBeanCustomizer()
//        {
//            @Override
//            public void customize(SchedulerFactoryBean bean)
//            {
//                bean.setQuartzProperties(createQuartzProperties());
//            }
//        };
//    }
//    private Properties createQuartzProperties()
//    {
//        Properties props = new Properties();
//        props.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate");
//        return props;
//    }
//}
