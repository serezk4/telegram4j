package com.serezka.telegram4j.command.system;

import com.serezka.database.authorization.model.User;
import com.serezka.telegram4j.command.Command;

import java.util.List;

public abstract class SystemCommand extends Command {
    public SystemCommand(List<String> usage, String help) {
        super(usage, help, User.Role.MIN);
    }
}
