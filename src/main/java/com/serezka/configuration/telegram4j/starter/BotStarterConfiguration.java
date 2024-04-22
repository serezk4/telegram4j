package com.serezka.configuration.telegram4j.starter;

import com.serezka.configuration.telegram4j.bot.BotConfiguration;
import com.serezka.telegram4j.bot.Bot;
import com.serezka.telegram4j.bot.ExecutorRouter;
import com.serezka.telegram4j.bot.Handler;
import com.serezka.telegram4j.starter.BotStarter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author serezk4
 * @version 1.0
 * @since 1.0
 * <p>
 * Configuration class for bot starter
 * @see BotStarter
 */

@Configuration
@PropertySource("classpath:telegram.properties")
public class BotStarterConfiguration {

    /**
     * Bean for bot starter
     * @see Bot
     * @see BotConfiguration#bot(Handler, ExecutorRouter)
     * @param token Telegram bot token
     * @param bot Bot bean
     * @return Bot starter
     */
    @Bean(initMethod = "start")
    public BotStarter botStarter(@Value("${telegram.bot.token}") String token, Bot bot) {
        return new BotStarter(token, bot);
    }
}
