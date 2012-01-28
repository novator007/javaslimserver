package org.bff.squeezeserver.exception;

/**
 * Abstract base class for SqueezeServer Exceptions.
 *
 * @author Bill Findeisen
 * @version 1.0
 */
public abstract class SqueezeException extends Exception {

    /**
     * Constructor.
     */
    public SqueezeException() {
        super();
    }

    /**
     * Class constructor specifying the message.
     *
     * @param message the exception message
     */
    public SqueezeException(String message) {
        super(message);
    }

    /**
     * Class contructor specifying the cause.
     *
     * @param cause the cause of this exception
     */
    public SqueezeException(Throwable cause) {
        super(cause);
    }

    /**
     * Class constructor specifying the message and cause.
     *
     * @param message the exception message
     * @param cause   the cause of this exception
     */
    public SqueezeException(String message, Throwable cause) {
        super(message, cause);
    }
}
