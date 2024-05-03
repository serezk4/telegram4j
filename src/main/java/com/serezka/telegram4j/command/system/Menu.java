package com.serezka.telegram4j.command.system;

import com.serezka.database.authorization.model.User;
import com.serezka.telegram4j.broker.MessageBroker;
import com.serezka.telegram4j.broker.SessionBroker;
import com.serezka.telegram4j.keyboard.Button;
import com.serezka.telegram4j.keyboard.inline.Callback;
import com.serezka.telegram4j.keyboard.inline.Inline;
import com.serezka.telegram4j.session.menu.MenuSession;
import com.serezka.telegram4j.session.menu.MenuSessionConfiguration;
import com.serezka.telegram4j.session.menu.page.Page;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Menu extends SystemCommand {
    SessionBroker sessionBroker;
    MessageBroker messageBroker;

    public Menu(SessionBroker sessionBroker, MessageBroker messageBroker) {
        super(List.of("/menu"), "Show menu");
        this.sessionBroker = sessionBroker;
        this.messageBroker = messageBroker;
    }

    @Override
    public void execute(User user, Update update) {
        sessionBroker.register(new MenuSession(MenuSessionConfiguration.create(
                new Page.GenerateByFunction((session, user1, update1) -> new Page("Menu", new Inline.DynamicKeyboard(
                        2, Button.Inline.fromLink("test", "123"), Button.Inline.fromLink("test2", "root")
                )))),
                user, messageBroker), update);
    }
}
