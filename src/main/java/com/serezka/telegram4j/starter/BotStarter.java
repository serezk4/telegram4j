package com.serezka.telegram4j.starter;

import com.serezka.telegram4j.bot.Bot;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author serezk4
 * @version 1.1
 * @since 1.12
 * <p>
 * Class for starting telegram bot with token
 */

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Log4j2
public class BotStarter {
    String token;
    Bot bot;
    TelegramBotsLongPollingApplication botsApplication;

    /**
     * Method for starting bot
     * Registers bot with token
     *
     * @see Bot
     */
    public void start() {
        try {
            log.info("Starting bot with token: {}***", token.substring(0, 4));
            botsApplication.registerBot(token, bot);
            log.info("Successfully started bot {}***", token.substring(0, 4));
        } catch (TelegramApiException e) {
            log.warn("Failed to start bot with token: {}***; Exception: {}", token.substring(0, 4), e.getMessage());
        }
    }

    /**
     * Method for stopping bot
     * Unregisters bot with token
     * @see Bot
     * @see TelegramBotsLongPollingApplication#stop()
     */
    public void stop() {
        try {
            log.info("Stopping bot with token: {}***", token.substring(0, 4));
            botsApplication.stop();
            log.info("Successfully stopped bot {}***", token.substring(0, 4));
        } catch (TelegramApiException e) {
            log.warn("Failed to stop bot with token: {}***; Exception: {}", token.substring(0, 4), e.getMessage());
        }
    }
}
