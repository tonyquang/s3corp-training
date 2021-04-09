package com.quan12yt.s3trainingeurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class S3trainingEurekaserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(S3trainingEurekaserverApplication.class, args);
	}

}
