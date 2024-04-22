package com.serezka.telegram4j.command.system;

import com.serezka.database.authorization.model.User;
import com.serezka.telegram4j.broker.MessageBroker;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class Hello extends SystemCommand {
    MessageBroker broker;

    public Hello(MessageBroker broker) {
        super(List.of("/hello"), "print hello");
        this.broker = broker;
    }

    @Override
    public void execute(User user, Update update) {
        broker.sendMessage(update.getMessage().getChatId(), "Hello, " + user.getUsername() + "!");
    }
}
