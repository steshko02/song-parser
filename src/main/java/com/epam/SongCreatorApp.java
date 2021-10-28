package com.epam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class SongCreatorApp {
        public static void main(String[] args) {
            SpringApplication.run(SongCreatorApp.class, args);
        }
}
