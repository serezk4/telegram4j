package com.serezka.telegram4j.bot;

import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * @param handler  handler
 * @param executor executor
 * @author serezk4
 * @version 1.2
 * @since 1.0
 * <p>
 * Main class for bot
 * Handles updates and transfer to handler
 */

public record Bot(Handler handler, ExecutorRouter executor) implements LongPollingUpdateConsumer {
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
