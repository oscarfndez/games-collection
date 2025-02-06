package com.gamescollection.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamescollection.core.port.GameRepository;
import com.gamescollection.core.port.GameService;
import com.gamescollection.core.service.GameServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreConfiguration {

    @Bean
    public GameService GameService(GameRepository gameRepository) {
        return new GameServiceImpl(gameRepository);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
