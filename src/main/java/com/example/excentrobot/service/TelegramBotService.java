package com.example.excentrobot.service;

import com.example.excentrobot.config.TelegramBotConfig;
import com.example.excentrobot.entity.Command;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TelegramBotService extends TelegramWebhookBot {
    private final TelegramBotConfig config;

    public TelegramBotService(TelegramBotConfig config, DefaultBotOptions defaultBotOptions) {
        super(defaultBotOptions);
        this.config = config;
        init();
    }

    @Override
    public String getBotUsername() {
        return config.getUsername();
    }

    @PostConstruct
    private void init() {
        registerMyCommands();
    }

    private void registerMyCommands() {
        try {
            List<BotCommand> commands = Arrays.stream(Command.values())
                    .map(command -> BotCommand.builder()
                            .command(command.getName())
                            .description(command.getDesc())
                            .build())
                    .collect(Collectors.toList());
            execute(SetMyCommands.builder().commands(commands).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        System.out.println(update.getMessage());

        if (update.hasCallbackQuery()) {
            return handleCallback(update.getCallbackQuery());
        } else if (update.hasMessage()) {
            return handleMessage(update.getMessage());
        }
        return null;
    }

    private BotApiMethod<?> handleMessage(Message message) {
        SendMessage sendMessage;
        sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(message.getText() != null ? message.getText() : "No text");
        return sendMessage;
    }

    private BotApiMethod<?> handleCallback(CallbackQuery callbackQuery) {
        SendMessage message = new SendMessage();
        message.setChatId(message.getChatId());
        message.setText(callbackQuery.toString());
        return message;
    }

    @Override
    public String getBotPath() {
        return null;
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }
}
