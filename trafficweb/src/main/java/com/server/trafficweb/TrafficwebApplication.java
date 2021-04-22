package com.server.trafficweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@EnableEurekaClient
@ComponentScan({ "com.server.trafficweb", "com.easyquartz" })
public class TrafficwebApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrafficwebApplication.class, args);
	}

}
