package com.wilddev.image.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;

import org.springframework.context.annotation.*;

@ConditionalOnProperty(name = "lock.enabled", havingValue = "true")
@Configuration
public class RedisConf {

    private final String appName;

    public RedisConf(@Value("${spring.application.name}") String appName) {
        this.appName = appName;
    }

    @Bean(destroyMethod = "destroy")
    public RedisLockRegistry lockRegistry(RedisConnectionFactory factory) {
        return new RedisLockRegistry(factory, appName);
    }
}
