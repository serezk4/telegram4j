package com.serezka.telegram4j.command.system;

import com.serezka.database.authorization.model.User;
import com.serezka.telegram4j.broker.MessageBroker;
import com.serezka.telegram4j.broker.SessionBroker;
import com.serezka.telegram4j.session.step.Step;
import com.serezka.telegram4j.session.step.StepSession;
import com.serezka.telegram4j.session.step.StepSessionConfiguration;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Step2 extends SystemCommand {
    SessionBroker sessionBroker;
    MessageBroker messageBroker;

    public Step2(SessionBroker sessionBroker, MessageBroker messageBroker) {
        super(List.of("/step"), "step");
        this.sessionBroker = sessionBroker;
        this.messageBroker = messageBroker;
    }

    @Override
    public void execute(User user, Update update) {
        sessionBroker.register(new StepSession(StepSessionConfiguration.create()
                .execute((session, user1, update1) -> new Step("123"))
                .execute((session, user1, update1) -> new Step("root"))
                , user, messageBroker), update);
    }
}
