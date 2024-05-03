package com.serezka.telegram4j.session.step;

import com.serezka.database.authorization.model.User;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class StepSessionManager {
    private static StepSessionManager instance = null;

    private final Map<User, Deque<StepSession>> stepSessions = new HashMap<>();

    private StepSessionManager() {
    }

    public static synchronized StepSessionManager getInstance() {
        if (instance == null) instance = new StepSessionManager();
        return instance;
    }

    public StepSession add(StepSession stepSession) {
        synchronized (stepSessions) {
            stepSessions.computeIfAbsent(stepSession.getUser(), k -> new ArrayDeque<>()).add(stepSession);
            return stepSession;
        }
    }

    public boolean contains(User user) {
        synchronized (stepSessions) {
            return stepSessions.containsKey(user) && !stepSessions.get(user).isEmpty();
        }
    }

    public void remove(StepSession stepSession) {
        synchronized (stepSessions) {
            stepSessions.get(stepSession.getUser()).remove(stepSession);
        }
    }

    public StepSession get(User user) {
        synchronized (stepSessions) {
            return stepSessions.get(user).peekLast();
        }
    }
}
