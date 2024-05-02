package com.serezka.telegram4j.session.step;

import com.serezka.database.authorization.model.User;
import com.serezka.telegram4j.broker.MessageBroker;
import com.serezka.telegram4j.session.Session;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author serezk4
 * @version 1.0
 * @since 1.12
 * <p>
 * Class for step session
 * e.g. for handling step-by-step actions in chat
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Getter
public class StepSession implements Session {
    StepSessionConfiguration configuration;
    final long chatId;
    final User user;
    final MessageBroker broker;

    // todo

    @Override
    public void next(Update update) {}

    @Override
    public void close() throws Exception {}
}
