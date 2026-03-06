package org.kanelucky.api.java.eventbus.event;

/**
 * An interface to represent a cancellable event.
 * <p>
 * Event that implements this interface is cancelled.
 *
 * Originally developed in AllayMC (https://github.com/AllayMC/Allay)
 * Ported and adapted to this project by Kanelucky
 *
 * Original author: daoge_cmd (AllayMC)
 * Port author: Kanelucky
 */
public interface CancellableEvent {
    /**
     * Check whether the event is cancelled.
     *
     * @return {@code true} if the event is cancelled, otherwise {@code false}.
     */
    boolean isCancelled();

    /**
     * Set the event to be cancelled or not.
     *
     * @param value {@code true} if the event is cancelled, otherwise {@code false}
     */
    void setCancelled(boolean value);

    /**
     * Cancel the event.
     */
    default void cancel() {
        setCancelled(true);
    }
}
