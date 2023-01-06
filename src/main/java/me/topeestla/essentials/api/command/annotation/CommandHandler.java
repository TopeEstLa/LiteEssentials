package me.topeestla.essentials.api.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author TopeEstLa
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandHandler {

    /**
     * The command name, for example "test", if you need to use a subcommand, you have to
     * separate the subcommand with a ".". For example "test.subcommand"
     */
    String name();

    /**
     * The permission required to execute this command.
     */
    String permission() default "";

    /**
     * A list of alternative names for this command.
     */
    String[] aliases() default {};

    /**
     * Help message for this command, [0] The use of the command in (/help), [1] is the descriptions of the command in (/help).
     */
    String[] help() default {"", ""};

    /**
     * If the command is only for player.
     */
    boolean inGameOnly() default false;
}
