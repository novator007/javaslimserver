package org.bff.squeezeserver.exception;

/**
 * Represents an error with the {@link org.bff.squeezeserver.Player}.
 *
 * @author Bill Findeisen
 * @version 1.0
 */
public class PlayerException extends ResponseException {

    /**
     * Constructor.
     */
    public PlayerException() {
        super();
    }

    /**
     * Class constructor specifying the message.
     *
     * @param message the exception message
     */
    public PlayerException(String message) {
        super(message);
    }

    /**
     * Class constructor specifying the message and command generating the
     * error.
     *
     * @param message the exception message
     * @param command the command generating the exception
     */
    public PlayerException(String message, String command) {
        super(message, command);
    }

    /**
     * Class contructor specifying the cause.
     *
     * @param cause the cause of this exception
     */
    public PlayerException(Throwable cause) {
        super(cause);
    }

    /**
     * Class constructor specifying the message and cause.
     *
     * @param message the exception message
     * @param cause   the cause of this exception
     */
    public PlayerException(String message, Throwable cause) {
        super(message, cause);
    }

}
