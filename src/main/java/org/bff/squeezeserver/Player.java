package org.bff.squeezeserver;

import org.bff.squeezeserver.events.PlayerChangeEvent;
import org.bff.squeezeserver.events.PlayerChangeListener;
import org.bff.squeezeserver.events.VolumeChangeEvent;
import org.bff.squeezeserver.events.VolumeChangeListener;
import org.bff.squeezeserver.exception.ConnectionException;
import org.bff.squeezeserver.exception.PlayerException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a player
 *
 * @author Bill Findeisen
 * @version 1.0
 */
public class Player {

    private String id;
    private SqueezeServer squeezeServer;
    private static Properties prop = SqueezeServer.getProperties();
    public static final String SLIM_PROP_PLAYER_IP;
    public static final String SLIM_PROP_PLAYER_MODEL;
    public static final String SLIM_PROP_PLAYER_DISPLAY_TYPE;
    public static final String SLIM_PROP_PLAYER_SIG_STRENGTH;
    public static final String SLIM_PROP_PLAYER_NAME;
    public static final String SLIM_PROP_PLAYER_CONNECTED;
    public static final String SLIM_PROP_PLAYER_SLEEP;
    public static final String SLIM_PROP_PLAYER_SLEEP_QUERY;
    public static final String SLIM_PROP_PLAYER_POWER;
    public static final String SLIM_PROP_PLAYER_POWER_QUERY;
    public static final String SLIM_PROP_PLAYER_VOLUME;
    public static final String SLIM_PROP_PLAYER_VOLUME_QUERY;
    public static final String SLIM_PROP_PLAYER_MUTE_QUERY;
    public static final String SLIM_PROP_PLAYER_MUTE;
    public static final String SLIM_PROP_PLAYER_PLAY;
    public static final String SLIM_PROP_PLAYER_STOP;
    public static final String SLIM_PROP_PLAYER_PAUSE;
    public static final String SLIM_PROP_PLAYER_MODE;
    public static final String SLIM_PROP_PLAYER_TIME;
    public static final String SLIM_PROP_PLAYER_TIME_QUERY;
    public static final String SLIM_PROP_SYNC;
    public static final String SLIM_PROP_SYNC_QUERY;
    private static final String MODE_PLAYING = "play";
    private static final String MODE_STOPPED = "stop";
    private static final String PARAM_PLAYER_ID = Constants.CMD_PARAM_PLAYER_ID;
    private static final String PARAM_MARKER = Constants.CMD_PARAM_MARKER;
    private List<PlayerChangeListener> playerListeners;
    private List<VolumeChangeListener> volumeListeners;

    /**
     * The status of the player.
     */
    public enum PlayerStatus {

        /**
         * player stopped status
         */
        STATUS_STOPPED,
        /**
         * player playing status
         */
        STATUS_PLAYING,
        /**
         * player paused status
         */
        STATUS_PAUSED,
    }

    ;

    static {
        SLIM_PROP_PLAYER_IP = prop.getProperty("SS_PLAYER_IP").trim();
        SLIM_PROP_PLAYER_MODEL = prop.getProperty("SS_PLAYER_MODEL").trim();
        SLIM_PROP_PLAYER_DISPLAY_TYPE = prop.getProperty("SS_PLAYER_DSP_TYPE").trim();
        SLIM_PROP_PLAYER_SIG_STRENGTH = prop.getProperty("SS_PLAYER_SIG_STRENGTH").trim();
        SLIM_PROP_PLAYER_NAME = prop.getProperty("SS_PLAYER_NAME").trim();
        SLIM_PROP_PLAYER_CONNECTED = prop.getProperty("SS_PLAYER_CONNECTED").trim();
        SLIM_PROP_PLAYER_SLEEP_QUERY = prop.getProperty("SS_PLAYER_SLEEP_QUERY").trim();
        SLIM_PROP_PLAYER_SLEEP = prop.getProperty("SS_PLAYER_SLEEP").trim();
        SLIM_PROP_PLAYER_POWER = prop.getProperty("SS_PLAYER_POWER").trim();
        SLIM_PROP_PLAYER_POWER_QUERY = prop.getProperty("SS_PLAYER_POWER_QUERY").trim();
        SLIM_PROP_PLAYER_VOLUME = prop.getProperty("SS_PLAYER_VOLUME").trim();
        SLIM_PROP_PLAYER_VOLUME_QUERY = prop.getProperty("SS_PLAYER_VOLUME_QUERY").trim();
        SLIM_PROP_PLAYER_MUTE = prop.getProperty("SS_PLAYER_MUTE").trim();
        SLIM_PROP_PLAYER_MUTE_QUERY = prop.getProperty("SS_PLAYER_MUTE_QUERY").trim();
        SLIM_PROP_PLAYER_PLAY = prop.getProperty("SS_PLAYER_PLAY").trim();
        SLIM_PROP_PLAYER_STOP = prop.getProperty("SS_PLAYER_STOP").trim();
        SLIM_PROP_PLAYER_PAUSE = prop.getProperty("SS_PLAYER_PAUSE").trim();
        SLIM_PROP_PLAYER_MODE = prop.getProperty("SS_PLAYER_MODE").trim();
        SLIM_PROP_PLAYER_TIME = prop.getProperty("SS_PLAYER_TIME_SEEK");
        SLIM_PROP_PLAYER_TIME_QUERY = prop.getProperty("SS_PLAYER_TIME_QUERY");
        SLIM_PROP_SYNC = prop.getProperty("SS_PLAYER_SYNC");
        SLIM_PROP_SYNC_QUERY = prop.getProperty("SS_PLAYER_SYNC_QUERY");
    }

    /**
     * Constructor
     *
     * @param id player id
     * @param ss the {@link SqueezeServer} for this player
     */
    Player(String id, SqueezeServer ss) {
        this.setId(id);
        this.squeezeServer = ss;

        playerListeners = new ArrayList<PlayerChangeListener>();
        volumeListeners = new ArrayList<VolumeChangeListener>();
    }

    public void addPlayerChangeListener(PlayerChangeListener playerListener) {
        playerListeners.add(playerListener);
    }

    public void removePlayerChangeListener(PlayerChangeListener playerListener) {
        playerListeners.remove(playerListener);
    }

    public void addVolumeChangeListener(VolumeChangeListener volumeListener) {
        volumeListeners.add(volumeListener);
    }

    public void removeVolumeChangeListener(VolumeChangeListener volumeListener) {
        volumeListeners.remove(volumeListener);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String name;
    private String ip;
    private String model;
    private String displayType;
    private String port;

    public String getName() throws ConnectionException {
        if (name == null) {
            name = getSqueezeServer().sendCommand(
                    SLIM_PROP_PLAYER_NAME.replaceAll(PARAM_PLAYER_ID, getId()))[0];
        }

        return name;
    }

    public String getIp() throws ConnectionException {
        if (ip == null) {
            ip = getSqueezeServer().sendCommand(
                    SLIM_PROP_PLAYER_IP.replaceAll(PARAM_PLAYER_ID, getId()))[0].split(":")[0];
        }
        return ip;
    }

    public String getModel() throws ConnectionException {
        if (model == null) {
            model = getSqueezeServer().sendCommand(
                    SLIM_PROP_PLAYER_MODEL.replaceAll(PARAM_PLAYER_ID, getId()))[0];
        }
        return model;

    }

    public String getDisplayType() throws ConnectionException {
        if (displayType == null) {
            displayType = getSqueezeServer().sendCommand(
                    SLIM_PROP_PLAYER_DISPLAY_TYPE.replaceAll(PARAM_PLAYER_ID, getId()))[0];
        }
        return displayType;
    }

    public int getSignalStrength() throws ConnectionException {
        return Integer.parseInt(getSqueezeServer().sendCommand(
                SLIM_PROP_PLAYER_SIG_STRENGTH.replaceAll(PARAM_PLAYER_ID, getId()))[0]);
    }

    public boolean isConnected() throws ConnectionException {
        return Integer.parseInt(getSqueezeServer().sendCommand(
                SLIM_PROP_PLAYER_CONNECTED.replaceAll(PARAM_PLAYER_ID, getId()))[0]) == 1 ? true : false;
    }

    public int getPort() throws ConnectionException {
        return Integer.parseInt(getSqueezeServer().sendCommand(
                SLIM_PROP_PLAYER_IP.replaceAll(PARAM_PLAYER_ID, getId()))[0].split(":")[1]);
    }

    /**
     * Convenience method
     *
     * @return true if sleep time is set
     * @throws org.bff.squeezeserver.exception.ConnectionException
     *
     */
    public boolean isWillSleep() throws ConnectionException {
        return getSleepTime() > 0;
    }

    public int getSleepTime() throws ConnectionException {
        String s = getSqueezeServer().sendCommand(
                SLIM_PROP_PLAYER_SLEEP_QUERY.replaceAll(PARAM_PLAYER_ID, getId()))[0];

        if (s.contains(".")) {
            s = s.split("\\.")[0];
        }
        return Integer.parseInt(s);
    }

    /**
     * Sets the amount of time before the player goes to sleep.  Pass a 0 to stop the player from sleeping.
     *
     * @param seconds before sleep
     * @throws org.bff.squeezeserver.exception.ConnectionException
     *
     */
    public void setSleepTime(int seconds) throws ConnectionException {
        String command = SLIM_PROP_PLAYER_SLEEP.replaceAll(PARAM_PLAYER_ID, getId());
        command = command.replaceAll(PARAM_MARKER, Integer.toString(seconds));

        getSqueezeServer().sendCommand(command);
    }

    public boolean isPowered() throws ConnectionException {
        return (getSqueezeServer().sendCommand(
                SLIM_PROP_PLAYER_POWER_QUERY.replaceAll(PARAM_PLAYER_ID, getId()))[0]).equals("1");
    }

    public void powerOn() throws ConnectionException {
        getSqueezeServer().sendCommand(SLIM_PROP_PLAYER_POWER.replaceAll(PARAM_PLAYER_ID, getId()), "1");
        firePlayerEvent(new PlayerChangeEvent(this, PlayerChangeEvent.PLAYER_ON));
    }

    public void powerOff() throws ConnectionException {
        getSqueezeServer().sendCommand(SLIM_PROP_PLAYER_POWER.replaceAll(PARAM_PLAYER_ID, getId()), "0");
        firePlayerEvent(new PlayerChangeEvent(this, PlayerChangeEvent.PLAYER_OFF));
    }

    public void play() throws ConnectionException {
        getSqueezeServer().sendCommand(SLIM_PROP_PLAYER_PLAY.replaceAll(PARAM_PLAYER_ID, getId()));
        firePlayerEvent(new PlayerChangeEvent(this, PlayerChangeEvent.PLAYER_STARTED));
    }

    public void stop() throws ConnectionException {
        getSqueezeServer().sendCommand(SLIM_PROP_PLAYER_STOP.replaceAll(PARAM_PLAYER_ID, getId()));
        firePlayerEvent(new PlayerChangeEvent(this, PlayerChangeEvent.PLAYER_STOPPED));
    }

    public void pause() throws ConnectionException {
        getSqueezeServer().sendCommand(SLIM_PROP_PLAYER_PAUSE.replaceAll(PARAM_PLAYER_ID, getId()));
        firePlayerEvent(new PlayerChangeEvent(this, PlayerChangeEvent.PLAYER_PAUSED));
    }

    public boolean isPlaying() throws ConnectionException {
        return getMode().equals(PlayerStatus.STATUS_PLAYING);
    }

    public boolean isStopped() throws ConnectionException {
        return getMode().equals(PlayerStatus.STATUS_STOPPED);
    }

    public boolean isPaused() throws ConnectionException {
        return getMode().equals(PlayerStatus.STATUS_PAUSED);
    }

    public PlayerStatus getMode() throws ConnectionException {
        String response =
                getSqueezeServer().sendCommand(SLIM_PROP_PLAYER_MODE.replaceAll(PARAM_PLAYER_ID, getId()))[0];

        if (response.equals(MODE_PLAYING)) {
            return PlayerStatus.STATUS_PLAYING;
        } else if (response.equals(MODE_STOPPED)) {
            return PlayerStatus.STATUS_STOPPED;
        } else {
            return PlayerStatus.STATUS_PAUSED;
        }
    }

    /**
     * Sets the volume.  0-100
     *
     * @param volume if less that 0 sets to 0, if greater than 100 sets to 100
     */
    public void setVolume(int volume) throws ConnectionException {
        if (volume > 100) {
            volume = 100;
        }
        if (volume < 0) {
            volume = 0;
        }

        getSqueezeServer().sendCommand(SLIM_PROP_PLAYER_VOLUME.replaceAll(PARAM_PLAYER_ID, getId()), Integer.toString(volume));

        fireVolumeEvent(new VolumeChangeEvent(this, volume));
    }

    /**
     * Returns the volume of the player.  If muted, a negative number is returned
     *
     * @return the volume of the player
     */
    public int getVolume() throws ConnectionException {
        return Integer.parseInt(getSqueezeServer().sendCommand(
                SLIM_PROP_PLAYER_VOLUME_QUERY.replaceAll(PARAM_PLAYER_ID, getId()))[0]);
    }

    /**
     * Returns the time of the currently playing song
     *
     * @return the elapsed time
     */
    public int getElapsedTime() throws ConnectionException {
        return (int) Double.parseDouble(getSqueezeServer().sendCommand(
                SLIM_PROP_PLAYER_TIME_QUERY.replaceAll(PARAM_PLAYER_ID, getId()))[0].split("\\\\.")[0]);
    }

    /**
     * Seeks to a particular point in the currently playing song.
     *
     * @param time in seconds
     */
    public void seek(int time) throws ConnectionException {
        if (time < 0) {
            time = 0;
        }

        String cmd = SLIM_PROP_PLAYER_TIME.replaceAll(PARAM_PLAYER_ID, getId());

        getSqueezeServer().sendCommand(cmd, Integer.toString(time));

        firePlayerEvent(new PlayerChangeEvent(this, PlayerChangeEvent.PLAYER_SEEKING));
    }

    public void advance(int numSeconds) throws ConnectionException {
        if (numSeconds < 0) {
            numSeconds = 0;
        }

        String cmd = SLIM_PROP_PLAYER_TIME.replaceAll(PARAM_PLAYER_ID, getId());

        getSqueezeServer().sendCommand(cmd, "+" + Integer.toString(numSeconds));

        firePlayerEvent(new PlayerChangeEvent(this, PlayerChangeEvent.PLAYER_SEEKING));
    }

    public void backup(int numSeconds) throws ConnectionException {
        if (numSeconds < 0) {
            numSeconds = 0;
        }

        String cmd = SLIM_PROP_PLAYER_TIME.replaceAll(PARAM_PLAYER_ID, getId());

        getSqueezeServer().sendCommand(cmd, "-" + Integer.toString(numSeconds));

        firePlayerEvent(new PlayerChangeEvent(this, PlayerChangeEvent.PLAYER_SEEKING));
    }

    /**
     * Toggles mute
     */
    public void mute() throws ConnectionException {
        getSqueezeServer().sendCommand(SLIM_PROP_PLAYER_MUTE.replaceAll(PARAM_PLAYER_ID, getId()), "");

        if (isMuted()) {
            firePlayerEvent(new PlayerChangeEvent(this, PlayerChangeEvent.PLAYER_MUTED));
        } else {
            firePlayerEvent(new PlayerChangeEvent(this, PlayerChangeEvent.PLAYER_UNMUTED));
        }
    }

    public boolean isMuted() throws ConnectionException {
        return (getSqueezeServer().sendCommand(
                SLIM_PROP_PLAYER_MUTE_QUERY.replaceAll(PARAM_PLAYER_ID, getId()))[0]).equals("1");
    }

    public void syncPlayer(Player player) throws ConnectionException, PlayerException {
        if (player == null) {
            throw new PlayerException("To player cannot be null.");
        }

        String command = SLIM_PROP_SYNC.replaceFirst(PARAM_PLAYER_ID, player.getId());
        command = command.replaceFirst(PARAM_PLAYER_ID, getId());

        getSqueezeServer().sendCommand(command);

        firePlayerEvent(new PlayerChangeEvent(this, PlayerChangeEvent.PLAYER_SYNCED));
    }

    public void unsyncPlayer() throws ConnectionException {
        String command = SLIM_PROP_SYNC.replaceAll(PARAM_MARKER, "-");
        command = command.replaceFirst(PARAM_PLAYER_ID, getId());
        command = command.replaceFirst(PARAM_PLAYER_ID, "-");

        getSqueezeServer().sendCommand(command);

        firePlayerEvent(new PlayerChangeEvent(this, PlayerChangeEvent.PLAYER_UNSYNCED));
    }

    public boolean isSynced() throws ConnectionException {
        String command = SLIM_PROP_SYNC_QUERY.replaceAll(PARAM_PLAYER_ID, getId());

        String[] response = getSqueezeServer().sendCommand(command);

        return !"-".equalsIgnoreCase(response[0]);

    }

    /**
     * Returns a Collection of player ids that are synced with this player.
     *
     * @return
     * @throws org.bff.squeezeserver.exception.ConnectionException
     *
     */
    public Collection<Player> getSyncedPlayerIds() throws ConnectionException {
        String command = SLIM_PROP_SYNC_QUERY.replaceAll(PARAM_PLAYER_ID, getId());
        String[] response = getSqueezeServer().sendCommand(command);

        List<Player> ids = new ArrayList<Player>();

        if (response != null && response.length > 0) {
            String[] playerIds = null;
            for (int i = 0; i < response.length; i++) {
                String s = response[i];
                playerIds = s.split(",");
            }

            if (playerIds != null) {
                for (int i = 0; i < playerIds.length; i++) {
                    String strId = playerIds[i];
                    if (!strId.equals(getId())) {
                        ids.add(new Player(strId, squeezeServer));
                    }
                }
            }
        }
        return ids;
    }

    public SqueezeServer getSqueezeServer() {
        return squeezeServer;
    }

    public void setSqueezeServer(SqueezeServer squeezeServer) {
        this.squeezeServer = squeezeServer;
    }

    private void firePlayerEvent(PlayerChangeEvent event) {
        for (PlayerChangeListener listener : playerListeners) {
            listener.playerChanged(event);
        }

    }

    private void fireVolumeEvent(VolumeChangeEvent event) {
        for (VolumeChangeListener listener : volumeListeners) {
            listener.volumeChanged(event);
        }

    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return (true);
        }

        if ((object == null) || (object.getClass() != this.getClass())) {
            return false;
        }

        Player player = (Player) object;
        if (this.getId().equals(player.getId())) {
            return (true);
        } else {
            return (false);
        }

    }

    /**
     * Overrides hashcode method
     *
     * @return the hash code
     */
    public int hashCode() {
        int hash = 7;
        try {
            hash = 31 * hash + (int) getPort();
            hash = 31 * hash + (null == getName() ? 0 : getName().hashCode());
        } catch (ConnectionException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }

        return (hash);
    }

    @Override
    public String toString() {
        try {
            return getName();
        } catch (ConnectionException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
        }

        return getId();
    }
}
