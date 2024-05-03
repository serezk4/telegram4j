package com.serezka.telegram4j.util;

import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.objects.Update;

@Log4j2
public class UpdateUtil {
    public static long getChatId(Update update) {
        if (update.hasCallbackQuery()) return update.getCallbackQuery().getMessage().getChatId();
        if (update.hasMessage()) return update.getMessage().getChatId();

        log.warn( "can't get chatId for update: {}",update.getMessage());
        return -1;
    }

    public static String getText(Update update) {
        if (update.hasCallbackQuery()) return update.getCallbackQuery().getData();
        if (update.hasMessage()) return update.getMessage().getText();

        log.warn( "can't get text for update: {}",update.getMessage());
        return "";
    }

    public static int getMessageId(Update update) {
        if (update.hasCallbackQuery()) return update.getCallbackQuery().getMessage().getMessageId();
        if (update.hasMessage()) return update.getMessage().getMessageId();

        log.warn( "can't get messageId for update: {}",update.getMessage());
        return -1;
    }
}
