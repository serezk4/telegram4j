package com.serezka.configuration.telegram4j.bot;

import com.serezka.telegram4j.bot.Bot;
import com.serezka.telegram4j.bot.ExecutorRouter;
import com.serezka.telegram4j.bot.Handler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

@Configuration
@PropertySource("classpath:telegram.properties")
public class BotConfiguration {
    @Bean
    @Scope("singleton")
    public Bot bot(@Value("${telegram.bot.username}") String botUsername,
                   @Value("${telegram.bot.token}") String botToken,
                   Handler handler, ExecutorRouter executor) {
        return new Bot(botUsername, botToken, handler, executor);
    }
}
