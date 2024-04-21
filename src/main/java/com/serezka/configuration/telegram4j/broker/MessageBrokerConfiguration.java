package com.serezka.configuration.telegram4j.broker;

import com.serezka.telegram4j.broker.MessageBroker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
public class MessageBrokerConfiguration {
    @Bean
    public MessageBroker messageBroker(TelegramClient telegramClient) {
        return new MessageBroker(telegramClient);
    }
}
