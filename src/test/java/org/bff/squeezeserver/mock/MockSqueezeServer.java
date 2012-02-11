package org.bff.squeezeserver.mock;

import org.bff.squeezeserver.Command;
import org.bff.squeezeserver.SqueezeServer;
import org.bff.squeezeserver.exception.ConnectionException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bill
 * Date: 1/20/12
 * Time: 4:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class MockSqueezeServer extends SqueezeServer {
    public static final String TEST_FILE = "src/test/resources/testdata.txt";
    private List<TestData> testDataList;
    private String server;
    private int webPort;
    private int serverPort;

    public MockSqueezeServer(String server, int port, int webPort) throws ConnectionException {
        super();
        setServer(server);
        setServerPort(port);
        setWebPort(webPort);
        loadTestFile();
    }

    public String[] sendCommand(String command) throws ConnectionException {
        return lookupResponse(command);
    }

    public String[] sendCommand(Command command) throws ConnectionException {
        return lookupResponse(command);
    }

    public void sendCommand(String command, String param) throws ConnectionException {
        super.sendCommand(command, param);
    }

    private void loadTestFile() {
        testDataList = new ArrayList<TestData>();
        List<String> list = new ArrayList<String>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(TEST_FILE));
            String str;
            while ((str = in.readLine()) != null) {
                list.add(str);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] array = new String[list.size()];
        int count = 0;
        for (String s : list) {
            array[count++] = s;
        }

        int i = 0;
        while (i < array.length) {
            if (array[i].startsWith("Class:")) {
                TestData testData = new TestData();
                testData.setClassName(array[i].replaceAll("Class:", "").trim());
                ++i;

                while (array[i].trim().startsWith("Command:")) {
                    String command = array[i++].replaceAll("Command:", "").trim();
                    testData.setCommand(command);
                }
                while (array[i].trim().startsWith("Param:")) {
                    String param = array[i++].replaceAll("Param:", "").trim();
                    testData.getParams().add(param.equals("null") ? null : param);
                }
                while (array[i].trim().startsWith("Response:")) {
                    String response = array[i++].replaceAll("Response:", "").trim();
                    testData.getResult().add(response.equals("null") ? null : response);
                    if (i > array.length - 1) {
                        break;
                    }
                }
                testDataList.add(testData);
            } else {
                ++i;
            }
        }
    }

    /**
     * This may look inefficient but it's not as the first in the list should always
     * get hit :)
     *
     * @param command
     * @return
     */
    private String[] lookupResponse(String command) {
        for (TestData data : testDataList) {
            if (data.command.equals(command)) {
                if (data.getParams().size() == 0) {
                    testDataList.remove(data);
                    return (String[]) data.getResult().toArray(new String[0]);
                }
            }
        }
        return null;
    }

    /**
     * This may look inefficient but it's not as the first in the list should always
     * get hit :)
     *
     * @param command
     * @return
     */
    private String[] lookupResponse(Command command) {
        for (TestData data : testDataList) {
            if (data.getCommand().equals(command.getCommand().trim())) {
                if (data.getParams().containsAll(command.getParams())) {
                    testDataList.remove(data);
                    return (String[]) data.getResult().toArray(new String[0]);
                }

            }
        }
        return null;
    }

    public String getServer() {
        return server;
    }

    public int getWebPort() {
        return webPort;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public void setWebPort(int webPort) {
        this.webPort = webPort;
    }

    private class TestData {
        private String command;
        private List<String> params;
        private List<String> result;
        private String className;

        public TestData() {
            params = new ArrayList<String>();
            result = new ArrayList<String>();
        }

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }

        public List<String> getParams() {
            return params;
        }

        public void setParams(List<String> params) {
            this.params = params;
        }

        public List<String> getResult() {
            return result;
        }

        public void setResult(List<String> result) {
            this.result = result;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }
    }
}
