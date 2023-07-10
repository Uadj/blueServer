package com.example.demo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;

@Configuration
public class ApplicationConfig {

    @Bean
    public RedisAtomicLong rankCounter(RedisConnectionFactory connectionFactory) {
        return new RedisAtomicLong("rankCounter", connectionFactory);
    }
}