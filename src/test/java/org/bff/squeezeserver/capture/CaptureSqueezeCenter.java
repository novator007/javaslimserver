package org.bff.squeezeserver.capture;

import org.bff.squeezeserver.Command;
import org.bff.squeezeserver.MockUtils;
import org.bff.squeezeserver.SqueezeServer;
import org.bff.squeezeserver.exception.ConnectionException;
import org.bff.squeezeserver.mock.MockSqueezeServer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CaptureSqueezeCenter extends SqueezeServer {
    private static final String TEST_FILE = MockSqueezeServer.TEST_FILE;

    public CaptureSqueezeCenter(String server, int port, int webPort) throws ConnectionException {
        super(server, port, webPort);
        createTestFile();
    }

    private void createTestFile() {
        File file = new File(TEST_FILE);
        if (!file.exists()) {
            try {
                file.delete();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String[] sendCommand(String command) throws ConnectionException {
        writeToFile("Class: " + MockUtils.getClassName());
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
        writeToFile("Class: " + MockUtils.getClassName());
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
        writeToFile("Class: " + MockUtils.getClassName());
        writeToFile("Command: " + command);
        writeToFile("\tParam: " + param);
        String[] response = null;
        super.sendCommand(command, param);
    }

    private void writeToFile(String str) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(TEST_FILE, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(str + System.getProperty("line.separator"));
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
