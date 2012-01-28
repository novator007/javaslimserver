package org.bff.slimserver.capture;

import org.bff.slimserver.Command;
import org.bff.slimserver.SqueezeServer;
import org.bff.slimserver.exception.ConnectionException;
import org.bff.slimserver.mock.MockSqueezeServer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CaptureSqueezeCenter extends SqueezeServer {
    private static final String TEST_FILE = MockSqueezeServer.TEST_FILE;

    public CaptureSqueezeCenter(String server, int port, int webPort) throws ConnectionException {
        super(server, port, webPort);
    }

    public String[] sendCommand(String command) throws ConnectionException {
        writeToFile("Command: " + command);
        String[] response = null;
        response = super.sendCommand(command);
        if (response == null) {
            writeToFile("\tResponse:null");
        } else {
            for (int i = 0; i < response.length; i++) {
                writeToFile("\tResponse:" + response[i]);
            }
        }
        return response;
    }

    public String[] sendCommand(Command command) throws ConnectionException {
        writeToFile("Command: " + command.getCommand());
        String[] response = null;
        response = super.sendCommand(command);

        for (String s : command.getParams()) {
            writeToFile("\tParam:" + s);
        }
        if (response == null) {
            writeToFile("\t\tResponse:null");
        } else {
            for (int i = 0; i < response.length; i++) {
                writeToFile("\t\tResponse:" + response[i]);
            }
        }
        return response;
    }

    public void sendCommand(String command, String param) throws ConnectionException {
        writeToFile("Command: " + command);
        writeToFile("\tParam: " + param);
        String[] response = null;
        super.sendCommand(command, param);
    }

    private void writeToFile(String str) {
        File file = new File(TEST_FILE);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileWriter fw = null;
        try {
            fw = new FileWriter(file.getPath(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(str);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}