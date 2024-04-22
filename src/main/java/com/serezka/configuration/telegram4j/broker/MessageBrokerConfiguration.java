package com.serezka.configuration.telegram4j.broker;

import com.serezka.configuration.telegram4j.bot.BotConfiguration;
import com.serezka.telegram4j.broker.MessageBroker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * @author serezk4
 * @version 1.0
 * @since 1.0
 * <p>
 * Configuration class for message broker
 * @see MessageBroker
 */

@Configuration
public class MessageBrokerConfiguration {

    /**
     * Bean for message broker for executing requests to Telegram API, uses Telegram client
     * @see TelegramClient
     * @see BotConfiguration#telegramClient(String)
     * @param telegramClient Telegram client
     * @return Message broker
     */
    @Bean
    public MessageBroker messageBroker(TelegramClient telegramClient) {
        return new MessageBroker(telegramClient);
    }
}
