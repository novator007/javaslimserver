package org.bff.squeezeserver.exception;

/**
 * Represents an error with the SqueezeServer connection.
 *
 * @author Bill Findeisen
 * @version 1.0
 */
public class ConnectionException extends SqueezeException {

    /**
     * Constructor.
     */
    public ConnectionException() {
        super();
    }

    /**
     * Class constructor specifying the message.
     *
     * @param message the exception message
     */
    public ConnectionException(String message) {
        super(message);
    }

    /**
     * Class contructor specifying the cause.
     *
     * @param cause the cause of this exception
     */
    public ConnectionException(Throwable cause) {
        super(cause);
    }

    /**
     * Class constructor specifying the message and cause.
     *
     * @param message the exception message
     * @param cause   the cause of this exception
     */
    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
