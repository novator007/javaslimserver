/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver.comm;

import org.bff.slimserver.Command;
import org.bff.slimserver.Constants;
import org.bff.slimserver.SqueezeServer;
import org.bff.slimserver.Utils;
import org.bff.slimserver.exception.ConnectionException;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author bfindeisen
 */
public class CommunicatorCLI implements Communicator {

    private Socket socket;
    private InetAddress serverAddress;
    private int serverPort;
    private String user;
    private String password;
    private static String encoding;
    private static String CMD_LOGIN;
    private static String CMD_VERSION;
    private static final int DEFAULT_CLI_PORT = Constants.DEFAULT_CLI_PORT;
    private static final String PARAM_MARKER = Constants.CMD_PARAM_MARKER;
    private static final String PREFIX_RESCAN = Constants.RESPONSE_PREFIX_RESCAN;

    public CommunicatorCLI(String server) throws UnknownHostException, ConnectionException {
        this(server, DEFAULT_CLI_PORT);
    }

    public CommunicatorCLI(String server, int cliPort) throws UnknownHostException, ConnectionException {
        this(server, cliPort, null, null);
    }

    public CommunicatorCLI(String server, int cliPort, String user, String password) throws UnknownHostException, ConnectionException {
        this.serverAddress = InetAddress.getByName(server);
        this.serverPort = cliPort;
        loadProperties();
        connect();
    }

    private synchronized void connect() throws ConnectionException {
        try {
            this.socket = new Socket(getServerAddress(), getServerPort());

            if (getUser() != null && getPassword() != null) {
                sendCommand(new Command(CMD_LOGIN,
                        new String[]{getUser(), getPassword()}));
            }

            //I do this twice to check the connection.  If the user and password are incorrect
            //this will bomb
            sendCommand(new Command(CMD_VERSION));

        } catch (Exception e) {
            throw new ConnectionException(e.getMessage());
        }
        if (!this.socket.isConnected()) {
            throw new ConnectionException();
        }
    }

    @Override
    public synchronized String[] sendCommand(String slimCommand) throws ConnectionException {
        return sendCommand(slimCommand, null);
    }

    @Override
    public synchronized String[] sendCommand(String slimCommand, String param) throws ConnectionException {
        if (param == null) {
            return sendCommand(new Command(slimCommand));
        } else {
            return sendCommand(new Command(slimCommand, param));
        }
    }

    @Override
    public synchronized String[] sendCommand(Command command) throws ConnectionException {

        byte[] bytesToSend = null;
        OutputStream outStream = null;
        BufferedReader in = null;

        if (!socket.isConnected()) {
            try {
                connect();
            } catch (Exception e) {
                throw new ConnectionException(e.getMessage());
            }
        }

        try {
            String cmd = convertCommand(command.getCommand(), command.getParams());

            bytesToSend = cmd.getBytes(getEncoding());
            outStream = getSocket().getOutputStream();
            outStream.write(bytesToSend);
            in = new BufferedReader(new InputStreamReader(getSocket().getInputStream(), getEncoding()));

            String inLine = in.readLine();

            if (cmd.contains("?")) {
                cmd = cmd.replaceFirst("\\?", "");
            }

            int commandLength = cmd.trim().split(" ").length;

            String[] splitString = null;

            if (inLine != null && commandLength < inLine.split(" ").length) {

                boolean scanning = false;
                //for now just trim the rescan tag
                //@TODO handle rescanning
                if (inLine.split(" ")[commandLength].startsWith(Utils.encode(PREFIX_RESCAN, getEncoding()))) {
                    ++commandLength;
                    scanning = true;
                }

                //trim the newline
                cmd = cmd.trim();

                splitString =
                        new String[inLine.split(" ").length - commandLength];

                System.arraycopy(inLine.split(" "),
                        scanning ? cmd.split(" ").length + 1 : cmd.split(" ").length,
                        splitString,
                        0,
                        splitString.length);

                for (int i = 0; i < splitString.length; i++) {
                    splitString[i] = Utils.decode(splitString[i], getEncoding());
                }
            }

            return (splitString);

        } catch (Exception e) {
            Logger.getLogger(SqueezeServer.class.getName()).log(Level.SEVERE, null, e);

            int count = 0;

            while (count < 2) {
                try {
                    wait(5000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SqueezeServer.class.getName()).log(Level.SEVERE, null, ex);
                }

                try {
                    connect();
                    break;
                } catch (Exception ex) {
                    Logger.getLogger(SqueezeServer.class.getName()).log(Level.SEVERE, null, ex);
                    ++count;
                }
            }
            throw new ConnectionException(e.getMessage());
        }
    }

    private String convertCommand(String command, List<String> params) {
        StringBuilder sb = new StringBuilder(command);
        if (params != null && params.size() > 0) {
            for (String param : params) {
                if (param != null) {
                    sb.replace(sb.indexOf(PARAM_MARKER),
                            sb.indexOf(PARAM_MARKER) + PARAM_MARKER.length(),
                            param);
                }
            }
        }
        sb.append("\n");

        return (sb.toString());
    }

    /**
     * @return the socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * @return the serverAddress
     */
    public InetAddress getServerAddress() {
        return serverAddress;
    }

    /**
     * @return the serverPort
     */
    public int getServerPort() {
        return serverPort;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the encoding
     */
    @Override
    public String getEncoding() {
        return encoding;
    }

    /**
     * @param serverEncoding the encoding to set
     */
    public static void setEncoding(String serverEncoding) {
        encoding = serverEncoding;
    }

    private static void loadProperties() {
        Properties prop = new Properties();

        InputStream is = SqueezeServer.class.getResourceAsStream(Constants.PROP_FILE);

        try {
            prop.load(is);
            setEncoding(SqueezeServer.getEncoding());
            CMD_VERSION = prop.getProperty(Constants.PROP_CMD_VERSION);
            CMD_LOGIN = prop.getProperty(Constants.PROP_CMD_LOGIN);
        } catch (Exception e) {
            Logger.getLogger(SqueezeServer.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(CommunicatorCLI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public boolean isConnected() {
        if (socket.isConnected()) {
            try {
                sendCommand(new Command(CMD_VERSION));
            } catch (Exception e) {
                Logger.getLogger(SqueezeServer.class.getName()).log(Level.SEVERE, null, e);
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
}
