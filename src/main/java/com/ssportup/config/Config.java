package com.ssportup.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.ssportup.repo.AuthRepository;
import com.ssportup.repo.AuthRepositoryImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс конфигурации микросервиса.
 *
 * @author habatoo
 */
@Configuration
public class Config {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public AuthRepository userRepositoryBean() throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        return new AuthRepositoryImpl(connection);
    }
}
