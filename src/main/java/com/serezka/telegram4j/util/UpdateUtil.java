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
}
