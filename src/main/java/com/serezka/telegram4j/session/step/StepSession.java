package com.serezka.telegram4j.session.step;

import com.serezka.telegram4j.session.Session;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author serezk4
 * @version 1.0
 * @since 1.12
 * <p>
 * Class for step session
 * e.g. for handling step-by-step actions in chat
 */
public class StepSession implements Session {
    @Override
    public void next(Update update) {

    }

    @Override
    public void close() throws Exception {

    }
}
