package com.serezka.telegram4j.command.system;

import com.serezka.database.authorization.model.User;
import com.serezka.telegram4j.broker.MessageBroker;
import com.serezka.telegram4j.broker.SessionBroker;
import com.serezka.telegram4j.session.notify.Notification;
import com.serezka.telegram4j.session.notify.NotifySession;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class Hello extends SystemCommand {
    MessageBroker broker;
    SessionBroker sessionBroker;

    public Hello(MessageBroker broker, SessionBroker sessionBroker) {
        super(List.of("/hello"), "print hello");
        this.broker = broker;
        this.sessionBroker = sessionBroker;
    }

    @Override
    public void execute(User user, Update update) {
        broker.sendMessage(update.getMessage().getChatId(), "Hello, " + user.getUsername() + "!");

        sessionBroker.register(new NotifySession(user, new Notification("test", Notification.Type.INFO), broker), update);
    }
}
