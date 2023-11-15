//package com.booking.booking.global.config;
//
//import io.micrometer.common.lang.NonNullApi;
//import io.r2dbc.pool.ConnectionPool;
//import io.r2dbc.pool.ConnectionPoolConfiguration;
//import io.r2dbc.spi.ConnectionFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.r2dbc.ConnectionFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
//import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
//
//import java.time.Duration;
//
//@Configuration
//@EnableR2dbcAuditing
//@NonNullApi
//public class MariaDbConfig extends AbstractR2dbcConfiguration {
//    @Value("${maria.url}")
//    private String url;
//    @Value("${maria.user}")
//    private String user;
//    @Value("${maria.password}")
//    private String password;
//
//    @Bean
//    @Override
//    public ConnectionFactory connectionFactory() {
//        ConnectionFactory connectionFactory = ConnectionFactoryBuilder
//                .withUrl(url)
//                .username(user)
//                .password(password)
//                .build();
//
//        ConnectionPoolConfiguration connectionPoolConfiguration
//                = ConnectionPoolConfiguration.builder(connectionFactory)
//                .initialSize(5)
//                .minIdle(2)
//                .maxSize(5)
//                .maxAcquireTime(Duration.ofSeconds(5))
//                .maxCreateConnectionTime(Duration.ofSeconds(5))
//                .maxLifeTime(Duration.ofSeconds(-1))
//                .maxIdleTime(Duration.ofSeconds(-1))
//                .build();
//
//        return new ConnectionPool(connectionPoolConfiguration);
//    }
//}
