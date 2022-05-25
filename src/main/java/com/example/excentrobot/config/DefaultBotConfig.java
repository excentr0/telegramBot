package com.example.excentrobot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;

@Configuration
public class DefaultBotConfig {
    @Bean
    public DefaultBotOptions defaultBotOptionConfig() {
        DefaultBotOptions botOptions = new DefaultBotOptions();

        return botOptions;
    }
}
