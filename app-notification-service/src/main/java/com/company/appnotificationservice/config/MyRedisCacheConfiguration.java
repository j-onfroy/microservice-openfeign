package com.company.appnotificationservice.config;

import org.springframework.cache.annotation.CachingConfigurationSelector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MyRedisCacheConfiguration extends CachingConfigurationSelector {
    private final CacheConfigurationProperties cacheConfigurationProperties;

    public MyRedisCacheConfiguration(@Lazy CacheConfigurationProperties cacheConfigurationProperties) {
        this.cacheConfigurationProperties = cacheConfigurationProperties;
    }

    private RedisCacheConfiguration createCacheConfiguration(long timeoutInSeconds) {
        return org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(timeoutInSeconds));
    }

    @Bean
    public RedisCacheManager cacheManager(LettuceConnectionFactory redisConnectionFactory) {
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        if (Objects.nonNull(cacheConfigurationProperties.getCaches())) {
            for (Map.Entry<String, String> cacheNameAndTimeout : cacheConfigurationProperties.getCaches().entrySet()) {
                cacheConfigurations.put(cacheNameAndTimeout.getKey(), createCacheConfiguration(Long.parseLong(cacheNameAndTimeout.getValue())));
            }
        }

        final RedisCacheWriter redisCacheWriter = RedisCacheWriter.lockingRedisCacheWriter(redisConnectionFactory);
        final RedisSerializationContext.SerializationPair<Object> valueSerializationPair = RedisSerializationContext.SerializationPair.fromSerializer(new UserRedisSerializer());
        final RedisSerializationContext.SerializationPair<String> keySerializationPair = RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string());

        return RedisCacheManager
                .builder(redisConnectionFactory)
                .cacheDefaults(createCacheConfiguration(60000))
                .withInitialCacheConfigurations(cacheConfigurations).build();
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(cacheConfigurationProperties.getHost());
        redisStandaloneConfiguration.setPort(Integer.parseInt(cacheConfigurationProperties.getPort()));
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }


}
