package com.serezka.telegram4j.bot;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.PropertySource;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

/**
 * @author serezk4
 * @version 1.0.1
 * @since 1.0
 * <p>
 * Main class for bot
 * Handles updates and transfer to handler
 */

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@RequiredArgsConstructor
public class Bot implements LongPollingUpdateConsumer {
    /* handler  */ Handler handler;
    /* executor */ ExecutorRouter executor;

    /**
     * Method for handling updates
     *
     * @param list - received update's
     */
    @Override
    public void consume(List<Update> list) {

    }
}
