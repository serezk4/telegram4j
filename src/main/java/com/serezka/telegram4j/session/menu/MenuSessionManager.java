package com.serezka.telegram4j.session.menu;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

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
}
