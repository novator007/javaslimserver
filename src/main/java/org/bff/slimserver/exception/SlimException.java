package org.bff.slimserver.exception;

/**
 * Abstract base class for SlimServer Exceptions.
 * @author Bill Findeisen
 * @version 1.0
 */
public abstract class SlimException extends Exception {
    
    /**
     * Constructor.
     */
    public SlimException() {
        super();
    }
    
    /**
     * Class constructor specifying the message.
     * @param message the exception message
     */
    public SlimException(String message) {
        super(message);
    }
    
    /**
     * Class contructor specifying the cause.
     * @param cause the cause of this exception
     */
    public SlimException(Throwable cause) {
        super(cause);
    }
    
    /**
     * Class constructor specifying the message and cause.
     * @param message the exception message
     * @param cause the cause of this exception
     */
    public SlimException(String message, Throwable cause) {
        super(message, cause);
    }    
}
