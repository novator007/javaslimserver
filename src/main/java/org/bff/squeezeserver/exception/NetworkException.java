package org.bff.squeezeserver.exception;

/**
 * Represents an error with the SqueezeServer response.
 *
 * @author Bill Findeisen
 * @version 1.0
 */
public class NetworkException extends SqueezeException {
    private String command = null;

    /**
     * Constructor.
     */
    public NetworkException() {
        super();
    }

    /**
     * Class constructor specifying the message.
     *
     * @param message the exception message
     */
    public NetworkException(String message) {
        super(message);
    }

    /**
     * Class constructor specifying the message and command generating the
     * error.
     *
     * @param message the exception message
     * @param command the command generating the exception
     */
    public NetworkException(String message, String command) {
        super(message);
        this.command = command;
    }

    /**
     * Class contructor specifying the cause.
     *
     * @param cause the cause of this exception
     */
    public NetworkException(Throwable cause) {
        super(cause);
    }

    /**
     * Class constructor specifying the message and cause.
     *
     * @param message the exception message
     * @param cause   the cause of this exception
     */
    public NetworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getCommand() {
        return (command);
    }
}
