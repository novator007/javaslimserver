package org.bff.slimserver.exception;

/**
 * Represents an error with the SlimServer connection.
 * @author Bill Findeisen
 * @version 1.0
 */
public class SlimConnectionException extends SlimException {
    
    /**
     * Constructor.
     */
    public SlimConnectionException() {
        super();
    }
    
    /**
     * Class constructor specifying the message.
     * @param message the exception message
     */
    public SlimConnectionException(String message) {
        super(message);
    }
    
    /**
     * Class contructor specifying the cause.
     * @param cause the cause of this exception
     */
    public SlimConnectionException(Throwable cause) {
        super(cause);
    }
    
    /**
     * Class constructor specifying the message and cause.
     * @param message the exception message
     * @param cause the cause of this exception
     */
    public SlimConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
