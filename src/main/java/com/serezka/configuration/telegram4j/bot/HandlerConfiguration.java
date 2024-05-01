package com.serezka.configuration.telegram4j.bot;

import com.serezka.database.authorization.service.UserService;
import com.serezka.telegram4j.bot.Handler;
import com.serezka.telegram4j.broker.MessageBroker;
import com.serezka.telegram4j.command.Command;
import com.serezka.telegram4j.session.MasterSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author serezk4
 * @version 1.0
 * @since 1.0
 * <p>
 * Configuration class for handler
 * @see Handler
 */

@Configuration
@ComponentScan("com.serezka.telegram4j.command")
public class HandlerConfiguration {

    /**
     * Bean for handler
     * Creates handler with specified list of commands
     * @param commands List of commands
     * @param userService User service
     * @param broker Message broker
     * @return Handler
     */
    @Bean
    public Handler handler(List<Command> commands, UserService userService, MessageBroker broker, MasterSessionManager sessionManager) {
        return new Handler(commands, userService, broker, sessionManager);
    }
}
