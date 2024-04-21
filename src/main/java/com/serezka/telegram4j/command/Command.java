package com.serezka.telegram4j.command;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * @author serezk4
 * @version 1.0
 * @since 1.0
 * <p>
 * Abstract class for command
 * Create your own command by extending this class
 */

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
@RequiredArgsConstructor
public abstract class Command {
    List<String> usage; // in regex
    String help;
    // todo requiredRole


}
