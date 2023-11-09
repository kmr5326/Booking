package com.booking.chat.redis.configuration;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class ReactiveRedisConfiguration {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;


    @Bean
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
        RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration(host, port);
        //standaloneConfiguration.setPassword("tmp");
        return new LettuceConnectionFactory(standaloneConfiguration);
    }

    @Bean
    @Qualifier
    public ReactiveRedisTemplate<String, List<Long>> reactiveRedisTemplate(){
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();

        ObjectMapper objectMapper = new ObjectMapper();
        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class, Long.class);
        Jackson2JsonRedisSerializer<List<Long>> listJsonSerializer = new Jackson2JsonRedisSerializer<>(type);

        RedisSerializationContext<String, List<Long>> serializationContext = RedisSerializationContext
            .<String, List<Long>>newSerializationContext(stringSerializer)
            .key(stringSerializer)
            .value(listJsonSerializer)
            .hashKey(stringSerializer)
            .hashValue(listJsonSerializer)
            .build();
        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory(), serializationContext);
    }
}
