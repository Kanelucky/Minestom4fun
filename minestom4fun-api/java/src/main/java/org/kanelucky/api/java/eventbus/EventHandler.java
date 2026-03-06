package org.kanelucky.api.java.eventbus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to mark a method as an event handler
 *
 * Originally developed in AllayMC (https://github.com/AllayMC/Allay)
 * Ported and adapted to this project by Kanelucky
 *
 * Original author: daoge_cmd (AllayMC)
 * Port author: Kanelucky
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventHandler {
    /**
     * Whether the event handler should be called asynchronously.
     *
     * @return {@code true} if the event handler should be called asynchronously, otherwise {@code false}
     */
    boolean async() default false;

    /**
     * The priority of the event handler. Event handler with the bigger priority will be called first.
     *
     * @return the priority of the event handler
     */
    int priority() default 0;
}
