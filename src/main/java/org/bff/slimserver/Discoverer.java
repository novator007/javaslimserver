/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;

/**
 * Discoverer will attempt to discover squeezebox servers that exist on the network.
 * A UDP broadcast is used to see who responds.  To start the discovery call {@link #startDiscovery}.
 * Discovery will continue until {@link #stopDiscovery()} is called.  Servers are added to a hash map
 * retrieved with {@link #getDiscoveredServers()}.
 *
 * @author Bill
 */
public class Discoverer {

    private HashMap<String, InetAddress> discoveredServers;
    private byte[] macAddress;
    private UdpSocket udpSocket;
    private boolean listenStarted;
    private final Object waitObject;
    private DatagramPacket discoveryPacket;
    private static Logger logger = Logger.getLogger("DiscoverLogger");

    /**
     * Default constructor
     */
    public Discoverer() {
        waitObject = new Object();
        discoveredServers = new HashMap<String, InetAddress>();
    }

    /**
     * Stops the discovery of servers
     */
    public void stopDiscovery() {
        udpSocket.setStopped(true);
    }

    /**
     * Starts the discovery of servers.  This will continue until {@link #stopDiscovery()} is called.
     *
     * @throws SocketException
     * @throws UnknownHostException
     * @throws IOException
     */
    public void startDiscovery() throws SocketException, UnknownHostException, IOException {
        logger.debug("Starting discovery");

        udpSocket = new UdpSocket();
        udpSocket.start();

        listenStarted = false;

        synchronized (waitObject) {
            while (!listenStarted) {
                try {
                    waitObject.wait();
                } catch (InterruptedException ex) {
                    logger.warn(null, ex);
                }
            }
        }
        logger.debug("Starting send...");
        //eIPAD\0NAME\0JSON\0
//        byte args[] = new byte[18];
//        args[0] = 'd';
//        args[2] = (byte) 2;
//        args[3] = (byte) 23;
        byte[] data = new byte[18];
        data[0] = (byte) 'd';//as in D
        data[6] = (byte) 13;//as in port 3483 (together with byte 7)
        data[7] = (byte) 155;

        System.arraycopy(getMacAddress(), 0, data, 12, 6);

        logger.debug("Mac");
        for (byte b : getMacAddress()) {
            logger.debug(Byte.toString(b));
        }

        logger.debug("Sending");
        for (byte b : data) {
            logger.debug(Byte.toString(b));
        }

        try {
            InetAddress broadcast = InetAddress.getByName("255.255.255.255");
            discoveryPacket = new DatagramPacket(data, 18, broadcast, 3483);
            logger.debug("Sending packet...");
            if (udpSocket != null) {
                udpSocket.send(discoveryPacket);
            } else {
                logger.warn("UDP Socket is null.  Not sending packet.");
            }
        } catch (IOException e) {
            logger.warn("Error discovering", e);
        }
    }

    /**
     * Parse a string mac address into a byte array.
     */
    private static byte[] parseMacAddress(String mac) {
        byte macAddress[] = new byte[6];

        String hex[] = mac.split(":-");
        for (int i = 0; i < Math.min(hex.length, macAddress.length); i++) {
            macAddress[i] = (byte) Integer.parseInt(hex[i], 16);
        }
        return macAddress;
    }

    private byte[] getMacAddress() {
        if (macAddress != null) {
            return macAddress;
        }

        logger.debug("Getting mac address");

        try {
            InetAddress addr = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(addr);
            macAddress = ni.getHardwareAddress();
        } catch (UnknownHostException ex) {
            logger.warn(null, ex);
        } catch (SocketException ex) {
            logger.warn(null, ex);
        } catch (NullPointerException npe) {
            logger.warn(null, npe);
        }

        if (macAddress == null) {
            logger.debug("Mac address not found.  Generating one.");
            /*
             * new random mac address. if this clashes with a real mac, then we are
             * really unlucky!
             */
            macAddress = new byte[6];
            for (int i = 0; i < macAddress.length; i++) {
                macAddress[i] = (byte) (Math.random() * 255);
            }
        }
        return macAddress;
    }

    /**
     * @return the discoveredServers
     */
    public HashMap<String, InetAddress> getDiscoveredServers() {
        return discoveredServers;
    }

    /**
     * Thread listening for Slim UDP commands.
     */
    private class UdpSocket extends Thread {

        private DatagramSocket socket;
        private boolean stopped;

        public UdpSocket() {
            super("SlimUDP Socket");
            try {
                socket = new DatagramSocket(3483);
            } catch (BindException e) {
                try {
                    logger.warn("Could not bind to port", e);
                    logger.warn("Will assume server is running locally");
                    getDiscoveredServers().put("127.0.0.1", InetAddress.getLocalHost());
                } catch (UnknownHostException ex) {
                    logger.fatal(null, ex);
                }
            } catch (IOException e) {
                logger.fatal(null, e);
            }
        }

        private void send(DatagramPacket p) throws IOException {
            if (socket != null) {
                socket.send(p);
            }
        }

        @Override
        public void run() {
            logger.debug("Starting discovery listen udp socket");
            try {
                byte[] buf = new byte[1024];
                DatagramPacket dp = new DatagramPacket(buf, buf.length);

                setStopped(false);

                listenStarted = true;
                synchronized (waitObject) {
                    waitObject.notify();
                }

                if (socket != null) {
                    socket.setSoTimeout(30000);

                    while (!isStopped()) {
                        boolean timeout = false;
                        try {
                            socket.receive(dp);
                        } catch (SocketTimeoutException ste) {
                            logger.warn("Socket timeout reached", ste);
                            timeout = true;
                        }

                        if (!timeout) {
                            StringBuffer str = new StringBuffer();

                            for (int i = 0; i < dp.getLength(); i++) {
                                String s = Integer.toString((buf[i] & 0xFF), 16);
                                str.append(s);
                                str.append(" ");
                            }

                            logger.debug("Received:" + str);

                            if (buf[0] == (byte) 'e') {
                                logger.info("Discovered server name:"
                                        + dp.getAddress() + " - "
                                        + dp.getAddress().getHostAddress());

                                getDiscoveredServers().put(dp.getAddress().getHostAddress(), dp.getAddress());

                                logger.debug("Server count is now " + getDiscoveredServers().size());
                                logger.info("Sending discovery packet.");
                                send(discoveryPacket);
                            }
                        } else {
                            logger.info("Sending discovery packet after timeout.");
                            send(discoveryPacket);
                        }
                    }
                    logger.info("UDP Socket has stopped listening");
                } else {
                    logger.warn("UDP Socket was null.  Not starting discovery thread.");
                }
            } catch (IOException e) {
                logger.warn("Error in udp socket", e);
            } finally {
                if (socket != null) {
                    socket.close();
                }
                setStopped(true);
            }
        }

        /**
         * @return the stopped
         */
        public boolean isStopped() {
            return stopped;
        }

        /**
         * @param stopped the stopped to set
         */
        public void setStopped(boolean stopped) {
            this.stopped = stopped;
            if (socket != null) {
                socket.disconnect();
            }
        }
    }
}
