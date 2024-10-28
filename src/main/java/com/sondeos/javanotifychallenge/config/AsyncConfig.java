package com.sondeos.javanotifychallenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {

    @Bean(name = "notificationExecutor")
    public Executor notificationTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // Elijo 3 porque pasa el test y no crea demasiados hilos
        executor.setCorePoolSize(3);
        executor.setThreadNamePrefix("Notification-");
        executor.initialize();
        return executor;
    }
}
