package com.serezka.telegram4j.session;

import com.serezka.database.authorization.model.User;
import com.serezka.telegram4j.session.menu.MenuSession;
import com.serezka.telegram4j.session.menu.MenuSessionManager;
import com.serezka.telegram4j.session.notify.NotifySessionManager;
import com.serezka.telegram4j.session.step.Step;
import com.serezka.telegram4j.session.step.StepSessionManager;
import com.serezka.telegram4j.util.UpdateUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * @author serezk4
 * @version 1.0
 * @since 1.13
 * <p>
 * Class for combining all sessions
 */

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MasterSessionManager {
    NotifySessionManager notifySessionManager;
    MenuSessionManager menuSessionManager;
    StepSessionManager stepSessionManager = StepSessionManager.getInstance();

    /**
     * Handle update by user and update
     * Check if exists any session for this user
     * @param user - user
     * @param update - received update
     * @return - true if session was handled | false if not (no session for this user)
     */
    public boolean handle(User user, Update update) {
        if (notifySessionManager.checkRequirements(update) && notifySessionManager.contains(user, UpdateUtil.getChatId(update))) {
            notifySessionManager.get(user, update.getCallbackQuery().getMessage().getMessageId()).next(update);
            return true;
        }

        if (menuSessionManager.checkRequirements(update) && menuSessionManager.contains(update)) {
            menuSessionManager.get(update).next(update);
            return true;
        }

        if (stepSessionManager.contains(user)) {
            stepSessionManager.get(user).next(update);
            return true;
        }

        return false;
    }
}
