package com.serezka.telegram4j.session.step;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Getter
public class StepSessionConfiguration {
    boolean saveUsersMessages = true, saveBotsMessages = true;
    boolean canEditMessages = true;
    final Deque<Step.Generator> steps = new ArrayDeque<>();

    public StepSessionConfiguration executeAll(Step.Generator... steps) {
        this.steps.addAll(Arrays.stream(steps).toList());
        return this;
    }

    public StepSessionConfiguration saveUsersMessages(boolean val) {
        this.saveUsersMessages = val;
        return this;
    }

    public StepSessionConfiguration saveBotsMessages(boolean val) {
        this.saveBotsMessages = val;
        return this;
    }

    public StepSessionConfiguration canEditMessages(boolean val) {
        this.canEditMessages = val;
        return this;
    }

    public StepSessionConfiguration get(String text, ReplyKeyboard replyKeyboard) {
        steps.add((session, user, update) -> new Step(String.join("","*", text, "*:"), replyKeyboard));
        return this;
    }

    public static StepSessionConfiguration create() {
        return new StepSessionConfiguration();
    }
}
