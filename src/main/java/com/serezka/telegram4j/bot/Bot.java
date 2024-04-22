package com.serezka.telegram4j.bot;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

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
     * @param updates - received update's
     */
    @Override
    public void consume(List<Update> updates) {
        if (executor.isShutdown()) return;
        updates.forEach(this::consume);
    }

    /**
     * Method for handling single update
     *
     * @param update - received update
     */
    private void consume(Update update) {
        executor.route(update.getUpdateId(), () -> handler.handle(update));
    }
}
