package com.serezka.telegram4j.session.notify;

import com.serezka.database.authorization.model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;

@Log4j2
@Component
@Scope("singleton")
public class NotifySessionManager {
    private final List<NotifySession> notifySessions = new LinkedList<>();

    public boolean checkRequirements(Update update) {
        return update.hasCallbackQuery();
    }

    public NotifySession add(NotifySession notifySession) {
        synchronized (notifySessions) {
            notifySessions.add(notifySession);
            log.info("new notify session: {}", notifySession);
            return notifySession;
        }
    }

    public boolean contains(User user, long messageId) {
        synchronized (notifySessions) {
            return notifySessions.stream().anyMatch(notifySession -> notifySession.getMessageId() == messageId && notifySession.getUser().equals(user));
        }
    }

    public NotifySession get(User user, int messageId) {
        synchronized (notifySessions) {
            return notifySessions.stream()
                    .filter(notifySession -> notifySession.getUser().equals(user) && notifySession.getMessageId() == messageId)
                    .findAny().orElse(null);
        }
    }

    public void remove(NotifySession notifySession) {
        synchronized (notifySessions) {
            notifySessions.remove(notifySession);
            log.info("removed notify session: {}", notifySession);
        }
    }


}
