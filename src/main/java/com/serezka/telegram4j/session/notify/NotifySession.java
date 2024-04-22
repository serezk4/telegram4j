package com.serezka.telegram4j.session.notify;

import com.serezka.telegram4j.session.Session;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author serezk4
 * @version 1.0
 * @since 1.12
 * <p>
 * Class for notify session
 * e.g. for sending notifications
 */
public class NotifySession implements Session {
    @Override
    public void next(Update update) {

    }

    @Override
    public void close() throws Exception {

    }
}
