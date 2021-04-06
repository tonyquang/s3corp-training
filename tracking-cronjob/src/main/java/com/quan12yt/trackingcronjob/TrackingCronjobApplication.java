package com.quan12yt.trackingcronjob;

import ch.qos.logback.core.spi.ContextAware;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
@ComponentScan({"com.quan12yt.*", "com.easyquartz"})

public class TrackingCronjobApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(TrackingCronjobApplication.class, args);
        context = (ConfigurableApplicationContext) context;
        ((ConfigurableApplicationContext) context).registerShutdownHook();
    }

}
