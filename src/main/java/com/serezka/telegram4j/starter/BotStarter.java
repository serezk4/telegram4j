package com.serezka.telegram4j.starter;

import com.serezka.telegram4j.bot.Bot;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class BotStarter {
    String token;
    Bot bot;

    
    public void start() {
        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(token, bot);
        } catch (Exception e) {
            log.error("Failed to start bot: ", e);
        }
    }
}
