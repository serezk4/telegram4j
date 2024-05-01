package com.serezka.telegram4j.session;

import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author serezk4
 * @version 1.0
 * @since 1.12
 * <p>
 * Abstract class for session
 */

public interface Session extends AutoCloseable {
    /**
     * Method for init session
     * by default calls next method
     *
     * @param update - received update
     * @see #next(Update)
     */
    default void init(Update update) {
        next(update);
    }

    /**
     * Method for handling update
     *
     * @param update - received update
     */
    void next(Update update);
}
