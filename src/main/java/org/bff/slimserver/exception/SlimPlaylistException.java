package org.bff.slimserver.exception;

/**
 * Represents an error with the {@link org.bff.slimserver.SlimPlaylist}.
 * @author Bill Findeisen
 * @version 1.0
 */
public class SlimPlaylistException extends SlimResponseException {
    
    /**
     * Constructor.
     */
    public SlimPlaylistException() {
        super();
    }
    
    /**
     * Class constructor specifying the message.
     * @param message the exception message
     */
    public SlimPlaylistException(String message) {
        super(message);
    }
            
    /**
     * Class constructor specifying the message and command generating the
     * error.
     * @param message the exception message
     * @param command the command generating the exception
     */
    public SlimPlaylistException(String message, String command) {
        super(message, command);
    }
    
    /**
     * Class contructor specifying the cause.
     * @param cause the cause of this exception
     */
    public SlimPlaylistException(Throwable cause) {
        super(cause);
    }
    
    /**
     * Class constructor specifying the message and cause.
     * @param message the exception message
     * @param cause the cause of this exception
     */
    public SlimPlaylistException(String message, Throwable cause) {
        super(message, cause);
    }
}
