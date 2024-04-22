package com.serezka.configuration.telegram4j.bot;

import com.serezka.database.authorization.service.UserService;
import com.serezka.telegram4j.bot.Handler;
import com.serezka.telegram4j.command.Command;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ComponentScan("com.serezka.telegram4j.command")
public class HandlerConfiguration {
    @Bean
    public Handler handler(List<Command> commands, UserService userService) {
        return new Handler(commands, userService);
    }
}
