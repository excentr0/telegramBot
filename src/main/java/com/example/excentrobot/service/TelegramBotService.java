package com.example.excentrobot.service;

import com.example.excentrobot.config.TelegramBotConfig;
import com.example.excentrobot.entity.Command;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.ApiResponse;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.net.Proxy;
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

    @Override
    public void setWebhook(SetWebhook setWebhook) throws TelegramApiException {
        try {
            final RestTemplate rest = newRestTemplate();
            final HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("Accept", "application/json");

            final String setWebhookUrl =
                    String.format("https://api.telegram.org/bot%s/%s", getBotToken(), SetWebhook.PATH);
            rest.exchange(setWebhookUrl, HttpMethod.POST, new HttpEntity<>(setWebhook, headers), ApiResponse.class);
        } catch (Exception e) {
            throw new TelegramApiRequestException("Error executing setWebHook method", e);
        }
    }

    @PostConstruct
    private void init() {
        registerMyCommands();
    }

    private void registerMyCommands() {
        try {
            List<BotCommand> commands =
                    Arrays.stream(Command.values())
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
    public String getBotToken() {
        return config.getToken();
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
        sendMessage.setText(message.getText());
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

    private RestTemplate newRestTemplate() {
        final RestTemplate rest = new RestTemplate();
        final DefaultBotOptions options = getOptions();
        switch (options.getProxyType()) {
            case NO_PROXY:
                break;
            case HTTP: {
                final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
                requestFactory.setProxy(new Proxy(Proxy.Type.HTTP,
                        new InetSocketAddress(options.getProxyHost(), options.getProxyPort())));
                rest.setRequestFactory(requestFactory);
                break;
            }
            case SOCKS4:
            case SOCKS5: {
                final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
                requestFactory.setProxy(new Proxy(Proxy.Type.SOCKS,
                        new InetSocketAddress(options.getProxyHost(), options.getProxyPort())));
                rest.setRequestFactory(requestFactory);
                break;
            }
        }
        return rest;
    }
}
