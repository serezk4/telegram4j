package com.serezka.telegram4j.session.notify;

import com.serezka.database.authorization.model.User;
import com.serezka.telegram4j.broker.MessageBroker;
import com.serezka.telegram4j.keyboard.Button;
import com.serezka.telegram4j.keyboard.inline.Callback;
import com.serezka.telegram4j.keyboard.inline.Inline;
import com.serezka.telegram4j.session.Session;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * @author serezk4
 * @version 1.0
 * @since 1.12
 * <p>
 * Class for notify session
 * e.g. for sending notifications
 */

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Getter
@ToString
public class NotifySession implements Session {
    final User user;
    final Notification notification;
    final MessageBroker broker;

    int messageId;

    @Override
    public void init(Update update) {
        messageId = broker.sendMessage(user.getChatId(),
                        String.format("%s %s", notification.type().getEmoji(), notification.message()),
                        Inline.getStaticKeyboard(new Button.Inline[][]{{
                                new Button.Inline("ok", Callback.builder()
                                        .data(List.of("close"))
                                        .build())
                        }}))
                .getMessageId();
    }

    @Override
    public void next(Update update) {
        if (messageId == 0) return;
        close();
    }

    @Override
    public void close() {
        broker.deleteMessage(user.getChatId(), messageId);
    }
}
