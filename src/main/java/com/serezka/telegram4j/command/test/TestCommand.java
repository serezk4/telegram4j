package com.serezka.telegram4j.command.test;

import com.serezka.database.authorization.model.User;
import com.serezka.telegram4j.command.Command;

import java.util.List;

/**
 * @author serezk4
 * @version 1.0
 * @since 1.1
 * <p>
 * Abstract class for test command
 * Default role: TEST (test / max role), only test users can use this command
 * @see User.Role#TEST
 */

public abstract class TestCommand extends Command {
    public TestCommand(List<String> usage, String help) {
        super(usage, help, User.Role.TEST);
    }
}
