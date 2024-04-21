package com.serezka.telegram4j.bot;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author serezk4
 * @version 1.0.1
 * @see Handler
 * @since 1.0
 * <p>
 * Main class for bot
 * Handles updates and transfer to handler
 */

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@PropertySource("classpath:telegram.properties")
public class Bot extends TelegramLongPollingBot {
    /* bot data     */ String botUsername, botToken;
    /* handler      */ Handler handler;
    /* executor     */ ExecutorRouter executor;

    public Bot(String botUsername, String botToken,
               Handler handler, ExecutorRouter executor) {
        super(botToken);

        this.botUsername = botUsername;
        this.botToken = botToken;
        this.handler = handler;
        this.executor = executor;
    }

    /**
     * Method for handling updates
     *
     * @param update - received update
     */
    @Override
    public void onUpdateReceived(Update update) {

    }
}
