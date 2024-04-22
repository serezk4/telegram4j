package com.serezka.telegram4j.command.test;

import com.serezka.database.authorization.model.User;
import com.serezka.telegram4j.command.Command;

import java.util.List;

public abstract class TestCommand extends Command {
    public TestCommand(List<String> usage, String help) {
        super(usage, help, User.Role.TEST);
    }
}
