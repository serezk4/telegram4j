package com.serezka.telegram4j.broker;

import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.*;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
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
 * todo insert local resolver for messages and keyboards here
 */

@Log4j2
public record MessageBroker(TelegramClient client) {
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
     * @param sendPhoto - method to execute
     * @return response
     */
    public Message execute(SendPhoto sendPhoto) {
        try {
            return client.execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.warn("Failed to send message: ", e);
            return null;
        }
    }

    /**
     * Method for sending photo
     *
     * @param chatId   - chat id
     * @param caption  - photo caption
     * @param keyboard - keyboard
     * @param media    - photo
     * @return response
     */
    public Message sendPhoto(long chatId, String caption, ReplyKeyboard keyboard, InputMedia media) {
        return execute(SendPhoto.builder()
                .chatId(chatId)
                .photo(new InputFile(media.getMediaName(), media.getMediaName(), media.getNewMediaFile(), media.getNewMediaStream(), media.isNewMedia()))
                .caption(caption).replyMarkup(keyboard)
                .build());
    }

    /**
     * Method for sending message
     *
     * @param editMessageMedia - method to execute
     * @return response
     */
    public Serializable execute(EditMessageMedia editMessageMedia) {
        try {
            return client.execute(editMessageMedia);
        } catch (TelegramApiException e) {
            log.warn("Failed to send message: ", e);
            return null;
        }
    }

    /**
     * Method for editing message caption
     *
     * @param chatId    - chat id
     * @param messageId - message id
     * @param caption   - new caption
     * @return edit response
     */
    public Serializable editCaption(long chatId, int messageId, String caption) {
        return execute(EditMessageCaption.builder()
                .chatId(chatId).messageId(messageId)
                .caption(caption)
                .build()
        );
    }

    /**
     * Method for editing message media
     *
     * @param chatId    - chat id
     * @param messageId - message id
     * @param media     - new media
     * @return edit response
     */
    public Serializable editMedia(long chatId, int messageId, InputMedia media) {
        return execute(EditMessageMedia.builder()
                .chatId(chatId).messageId(messageId)
                .media(media)
                .build()
        );
    }

    /**
     * Method for editing message reply markup
     *
     * @param chatId    - chat id
     * @param messageId - message id
     * @param keyboard  - new keyboard
     * @return edit response
     */
    public Serializable editKeyboard(long chatId, int messageId, InlineKeyboardMarkup keyboard) {
        return execute(EditMessageReplyMarkup.builder()
                .chatId(chatId).messageId(messageId)
                .replyMarkup(keyboard)
                .build()
        );
    }

    /**
     * Method for editing message text
     *
     * @param chatId    - chat id
     * @param messageId - message id
     * @param text      - new text
     * @return edit response
     */
    public Serializable editText(long chatId, int messageId, String text) {
        return execute(EditMessageText.builder()
                .chatId(chatId).messageId(messageId)
                .text(text)
                .build()
        );
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

    public void deleteMessage(long chatId, int messageId) {
        execute(DeleteMessage.builder()
                .chatId(chatId).messageId(messageId)
                .build());
    }
}
