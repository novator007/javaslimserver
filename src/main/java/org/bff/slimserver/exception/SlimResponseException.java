package org.bff.slimserver.exception;

/**
 * Represents an error with the SlimServer response.
 * @author Bill Findeisen
 * @version 1.0
 */
public class SlimResponseException extends SlimException {
    private String command  = null;
    
    /**
     * Constructor.
     */
    public SlimResponseException() {
        super();
    }
    
    /**
     * Class constructor specifying the message.
     * @param message the exception message
     */
    public SlimResponseException(String message) {
        super(message);
    }
    
    /**
     * Class constructor specifying the message and command generating the
     * error.
     * @param message the exception message
     * @param command the command generating the exception
     */
    public SlimResponseException(String message, String command) {
        super(message);
        this.command = command;
    }
    
    /**
     * Class contructor specifying the cause.
     * @param cause the cause of this exception
     */
    public SlimResponseException(Throwable cause) {
        super(cause);
    }
    
    /**
     * Class constructor specifying the message and cause.
     * @param message the exception message
     * @param cause the cause of this exception
     */
    public SlimResponseException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public String getCommand() {
        return(command);
    }
}
