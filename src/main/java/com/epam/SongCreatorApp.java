package com.epam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
@EnableEurekaClient
public class SongCreatorApp {
        public static void main(String[] args) {
            SpringApplication.run(SongCreatorApp.class, args);
        }
}
