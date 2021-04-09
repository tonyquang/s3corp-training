package com.quan12yt.s3trainingcloudgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class S3trainingCloudGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(S3trainingCloudGatewayApplication.class, args);
	}

}
