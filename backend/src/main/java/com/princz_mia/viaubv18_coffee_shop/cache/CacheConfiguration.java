package com.princz_mia.viaubv18_coffee_shop.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfiguration {

    @Bean
    public CacheStorage<String, Integer> userCache() {
        return new CacheStorage<>(900, TimeUnit.SECONDS);
    }

    // Note: you can implement other cache functions with @Bean annotation. With the 'name' tag you are able to specify for what you want to use it.
    // For example: @Bean(name = { "loginCache" })
}
