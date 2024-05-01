package com.serezka.telegram4j.session.step;

import com.serezka.database.authorization.model.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.function.TriFunction;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

/**
 * @author serezk4
 * @version 1.0
 * @since 1.13
 * <p>
 * Class for step
 * @see StepSession
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class Step {
    String text;
    ReplyKeyboard keyboard;

    // generators

    public interface Generator {
        Step apply(StepSession session, User user, Update update);
    }

    /**
     * Class for step generation by function
     *
     * @param func - function for step generation
     */
    public record GenerateByFunction(TriFunction<StepSession, User, Update, Step> func) implements Step.Generator {
        @Override
        public Step apply(StepSession session, User user, Update update) {
            return func.apply(session, user, update);
        }
    }

    /**
     * Class for static step generation
     *
     * @param step - static step
     */
    public record StaticGenerator(Step step) implements Step.Generator {
        @Override
        public Step apply(StepSession session, User user, Update update) {
            return step;
        }
    }
}
