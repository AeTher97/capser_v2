package com.mwozniak.capser_v2.ytremote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@SpringBootApplication
public class YouTubeRemoteApplication {

    public static void main(String[] args) {
        SpringApplication.run(YouTubeRemoteApplication.class, args);
    }

    @Bean
    public TaskScheduler taskScheduler() {

        return new ThreadPoolTaskScheduler();
    }
}
