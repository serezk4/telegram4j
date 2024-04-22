package com.serezka.configuration.telegram4j.bot;

import com.serezka.telegram4j.bot.Bot;
import com.serezka.telegram4j.bot.ExecutorRouter;
import com.serezka.telegram4j.bot.Handler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * @author serezk4
 * @version 1.0
 * @since 1.0
 * <p>
 * Configuration class for bot
 * @see Bot
 */

@Configuration
@PropertySource("classpath:telegram.properties")
public class BotConfiguration {

    /**
     * Bean for telegram client
     * Uses OkHttpTelegramClient to execute requests to Telegram API
     * @param botToken Telegram bot token
     * @return Telegram client
     */
    @Bean
    public TelegramClient telegramClient(@Value("${telegram.bot.token}") String botToken) {
        return new OkHttpTelegramClient(botToken);
    }


    /**
     * Bean for bot
     * @param handler Handler bean
     * @param executor ExecutorRouter bean
     * @return Bot
     */
    @Bean
    @Scope("singleton")
    public Bot bot(Handler handler, ExecutorRouter executor) {
        return new Bot(handler, executor);
    }
}
