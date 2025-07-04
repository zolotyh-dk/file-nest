package ru.zolotyh.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.zolotyh.controller.TelegramBot;

@Component
public class BotInitializer {
    private final TelegramBot bot;

    public BotInitializer(TelegramBot bot) {
        this.bot = bot;
    }

    @PostConstruct
    public void registerBot() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(bot);
    }
}