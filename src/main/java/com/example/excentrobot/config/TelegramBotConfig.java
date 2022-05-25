package com.example.excentrobot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Validated
@Configuration
public class TelegramBotConfig {
    @Value("${TELEGRAM_TOKEN:5340998927:AAFk63hOauVBf3NBfErxOtGtXMJk4_WgEnk}")
    @NotNull
    @NotEmpty
    private String token;

    @Value("${TELEGRAM_USERNAME:ExcentroBot}")
    @NotNull
    @NotEmpty
    private String username;

    @Value("${TELEGRAM_URL}")
    @NotNull
    @NotEmpty
    private String url;

    @Value("${TELEGRAM_MAX_CONNECTIONS:40}")
    @NotNull
    @NotEmpty
    private int maxConnections;

    @Value("${TELEGRAM_LANGUAGE_CODE:en}")
    @NotNull
    @NotEmpty
    private String languageCode;

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getUrl() {
        return url;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public String getLanguageCode() {
        return languageCode;
    }
}
