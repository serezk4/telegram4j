package com.serezka.telegram4j.broker;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * @author serezk4
 * @version 1.0
 * @since 1.0.1
 * <p>
 * Class for sending messages
 */

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MessageBroker {
    TelegramClient client;

}
