package com.serezka.telegram4j.broker;

import com.serezka.telegram4j.session.menu.MenuSession;
import com.serezka.telegram4j.session.menu.MenuSessionManager;
import com.serezka.telegram4j.session.notify.NotifySession;
import com.serezka.telegram4j.session.notify.NotifySessionManager;
import com.serezka.telegram4j.session.step.StepSession;
import com.serezka.telegram4j.session.step.StepSessionManager;
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
 * Class for registering sessions
 */

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SessionBroker {
    MenuSessionManager menuSessionManager;
    StepSessionManager stepSessionManager;
    NotifySessionManager notifySessionManager;

    /**
     * Register notify session
     * @param notifySession - notify session
     * @param update - received update
     */
    public void register(NotifySession notifySession, Update update) {
        notifySessionManager.add(notifySession).init(update);
    }

    /**
     * Register menu session
     * @param menuSession - menu session
     * @param update - received update
     */
    public void register(MenuSession menuSession, Update update) {

    }

    /**
     * Register step session
     * @param stepSession - step session
     * @param update - received update
     */
    public void register(StepSession stepSession, Update update) {

    }


}
