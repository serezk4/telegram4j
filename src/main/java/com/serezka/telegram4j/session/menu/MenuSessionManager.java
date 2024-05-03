package com.serezka.telegram4j.session.menu;

import com.serezka.database.authorization.model.User;
import com.serezka.telegram4j.util.UpdateUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

/**
 * @author serezk4
 * @version 1.0
 * @since 1.12
 * <p>
 * Class for menu session manager
 *
 * todo: make gc for destroyed menu sessions
 */

@Component
@Scope("singleton")
public class MenuSessionManager {
    private final List<MenuSession> menuSessions = new ArrayList<>();

    public boolean checkRequirements(Update update) {
        return update.hasCallbackQuery();
    }

    public MenuSession add(MenuSession menuSession) {
        synchronized (menuSessions) {
            menuSessions.add(menuSession);
            return menuSession;
        }
    }

    public void remove(MenuSession menuSession) {
        synchronized (menuSessions) {
            menuSessions.remove(menuSession);
        }
    }

    public MenuSession get(Update update) {
        if (!checkRequirements(update)) return null;
        return get(update.getCallbackQuery().getMessage().getMessageId());
    }

    public MenuSession get(int messageId) {
        synchronized (menuSessions) {
            return menuSessions.stream()
                    .filter(menuSession -> menuSession.containsMessage(messageId))
                    .findFirst()
                    .orElse(null);
        }
    }

    public boolean contains(Update update) {
        return contains(UpdateUtil.getMessageId(update));
    }

    public boolean contains(int messageId) {
        return menuSessions.stream().anyMatch(menuSession -> menuSession.containsMessage(messageId));
    }
}
