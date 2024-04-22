package com.serezka.telegram4j.broker;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.Serializable;

/**
 * @author serezk4
 * @version 1.0
 * @since 1.1
 * <p>
 * Class for sending messages
 */

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class MessageBroker {
    TelegramClient client;

    /**
     * Method for sending message
     *
     * @param method   - method to execute
     * @param <T>      - response type
     * @param <Method> - method type
     * @return response
     */
    public <T extends Serializable, Method extends BotApiMethod<T>> T execute(Method method) {
        try {
            return client.execute(method);
        } catch (TelegramApiException e) {
            log.warn("Failed to send message: ", e);
            return null;
        }
    }

    /**
     * Method for sending message
     *
     * @param chatId - chat id
     * @param text   - message text
     */
    public Message sendMessage(Long chatId, String text) {
        // todo add here default reply markup
        return sendMessage(chatId, text, null, ParseMode.HTML);
    }

    /**
     * Method for sending message
     *
     * @param chatId    - chat id
     * @param text      - message text
     * @param parseMode - message parse mode
     */
    public Message sendMessage(Long chatId, String text, String parseMode) {
        // todo add here default reply markup
        return sendMessage(chatId, text, null, parseMode);
    }

    /**
     * Method for sending message
     *
     * @param chatId        - chat id
     * @param text          - message text
     * @param replyKeyboard - keyboard
     */
    public Message sendMessage(Long chatId, String text, ReplyKeyboard replyKeyboard) {
        return sendMessage(chatId, text, replyKeyboard, ParseMode.HTML);
    }

    /**
     * Method for sending message
     *
     * @param chatId        - chat id
     * @param text          - message text
     * @param replyKeyboard - keyboard
     * @param parseMode     - message parse mode
     */
    public Message sendMessage(Long chatId, String text, ReplyKeyboard replyKeyboard, String parseMode) {
        return execute(SendMessage.builder()
                .chatId(chatId.toString())
                .text(text).parseMode(parseMode)
                .replyMarkup(replyKeyboard)
                .build());

    }
}
