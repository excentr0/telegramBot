package com.example.excentrobot.controllers;

import com.example.excentrobot.service.TelegramBotService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class MainController {
    private final TelegramBotService telegramBotService;

    public MainController(TelegramBotService telegramBotService) {this.telegramBotService = telegramBotService;}

    @ResponseBody
    @PostMapping(value = "/callback")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return telegramBotService.onWebhookUpdateReceived(update);
    }
}
