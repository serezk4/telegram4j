package com.serezka.telegram4j.session.step;

import com.serezka.telegram4j.keyboard.Keyboard;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Getter
public class StepSessionConfiguration {
    boolean saveUsersMessages = false, saveBotsMessages = false;
    boolean canEditMessages = true;
    final Deque<Step.Generator> steps = new ArrayDeque<>();

    public StepSessionConfiguration execute(Step.Generator... steps) {
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

    public StepSessionConfiguration get(String text, Keyboard keyboard) {
        steps.add((session, user, update) -> new Step(String.join("","*", text, "*:"), keyboard));
        return this;
    }

    public static StepSessionConfiguration create() {
        return new StepSessionConfiguration();
    }
}
