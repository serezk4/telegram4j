package com.serezka.telegram4j.command.system;

import com.serezka.database.authorization.model.User;
import com.serezka.telegram4j.command.Command;

import java.util.List;

/**
 * @author serezk4
 * @version 1.0
 * @since 1.1
 * <p>
 * Abstract class for system command
 * Default role: MIN (minimum), all users can use this command
 * @see User.Role#MIN
 */

public abstract class SystemCommand extends Command {
    public SystemCommand(List<String> usage, String help) {
        super(usage, help, User.Role.MIN);
    }
}
