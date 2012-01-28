package org.bff.squeezeserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Command represents a command along with optional command paramaters to be
 * sent to a {@link SqueezeServer} server.
 *
 * @author Bill Findeisen
 * @version 1.0
 */
public class Command {
    private String command;
    private List<String> params;

    /**
     * Constructor for SqueezeServer command for a command requiring no parameters.
     *
     * @param command the parameterless command to send.
     */
    public Command(String command) {
        this(command, new String[]{null});
    }

    /**
     * Constructor for SqueezeServer command for a command requiring a single
     * parameter.
     *
     * @param command the command to send
     * @param param   the parameter for the command
     */
    public Command(String command, String param) {
        this(command, new String[]{param});

    }

    /**
     * Constructor for SqueezeServer command for a command requiring more
     * than 1 parameter.
     *
     * @param command the command to send
     * @param params  the parameters to send
     */
    public Command(String command, String[] params) {
        this.command = command;
        this.params = new ArrayList<String>();
        this.params.addAll(Arrays.asList(params));
    }

    /**
     * Returns the command of this object.
     *
     * @return the command
     */
    public String getCommand() {
        return command;
    }

    /**
     * Returns the parameter(s) of this command as a {@link List} of {@link String}s.
     * Returns null of there is no parameter for the command.
     *
     * @return the parameters for the command
     */
    public List<String> getParams() {
        return params;
    }
}
