package com.serezka.telegram4j.session.menu;

import com.serezka.database.authorization.model.User;
import com.serezka.telegram4j.broker.MessageBroker;
import com.serezka.telegram4j.keyboard.Button;
import com.serezka.telegram4j.session.Session;
import com.serezka.telegram4j.session.menu.page.Page;
import com.serezka.telegram4j.session.menu.page.PageTree;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * @author serezk4
 * @version 1.0
 * @since 1.12
 * <p>
 * Class for menu session
 * e.g. for handling menu
 */

@Log4j2
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class MenuSession implements Session {
    static long idCounter = 0;
    final long id = idCounter++;

    final PageTree pageTree;
    final User user;
    final MessageBroker broker;

    final List<Button.Inline> top;
    final List<Button.Inline> bottom;

    @Override
    public void init(Update update) {
        log.info("Initiating menu session for user {} | menu id: {}", user, id);
        Page rootPage = pageTree.getMaster().getSelf().apply(this, user, update);
    }

    @Override
    public void next(Update update) {
        log.info("Handling menu session for user {} | menu id: {}", user, id);
    }

    private void back(Update update) {

    }

    @Override
    public void close() throws Exception {

    }
}
