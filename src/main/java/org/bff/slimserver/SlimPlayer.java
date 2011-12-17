package org.bff.slimserver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bff.slimserver.events.PlayerChangeEvent;
import org.bff.slimserver.events.PlayerChangeListener;
import org.bff.slimserver.events.VolumeChangeEvent;
import org.bff.slimserver.events.VolumeChangeListener;
import org.bff.slimserver.exception.SlimConnectionException;
import org.bff.slimserver.exception.SlimPlayerException;

/**
 * Represents a player
 * @author Bill Findeisen
 * @version 1.0
 */
public class SlimPlayer {

    private String id;
    private SlimServer slimServer;
    private static Properties prop = SlimServer.getSlimProperties();
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
    private static final String PARAM_PLAYER_ID = SlimConstants.CMD_PARAM_PLAYER_ID;
    private static final String PARAM_MARKER = SlimConstants.CMD_PARAM_MARKER;
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
    };

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
     * @param id player id
     * @param ss the {@link SlimServer} for this player
     */
    SlimPlayer(String id, SlimServer ss) {
        this.setId(id);
        this.slimServer = ss;

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

    public String getName() throws SlimConnectionException {
        if (name == null) {
            name = getSlimServer().sendCommand(
                    SLIM_PROP_PLAYER_NAME.replaceAll(PARAM_PLAYER_ID, getId()))[0];
        }

        return name;
    }

    public String getIp() throws SlimConnectionException {
        if (ip == null) {
            ip = getSlimServer().sendCommand(
                    SLIM_PROP_PLAYER_IP.replaceAll(PARAM_PLAYER_ID, getId()))[0].split(":")[0];
        }
        return ip;
    }

    public String getModel() throws SlimConnectionException {
        if (model == null) {
            model = getSlimServer().sendCommand(
                    SLIM_PROP_PLAYER_MODEL.replaceAll(PARAM_PLAYER_ID, getId()))[0];
        }
        return model;

    }

    public String getDisplayType() throws SlimConnectionException {
        if (displayType == null) {
            displayType = getSlimServer().sendCommand(
                    SLIM_PROP_PLAYER_DISPLAY_TYPE.replaceAll(PARAM_PLAYER_ID, getId()))[0];
        }
        return displayType;
    }

    public int getSignalStrength() throws SlimConnectionException {
        return Integer.parseInt(getSlimServer().sendCommand(
                SLIM_PROP_PLAYER_SIG_STRENGTH.replaceAll(PARAM_PLAYER_ID, getId()))[0]);
    }

    public boolean isConnected() throws SlimConnectionException {
        return Integer.parseInt(getSlimServer().sendCommand(
                SLIM_PROP_PLAYER_CONNECTED.replaceAll(PARAM_PLAYER_ID, getId()))[0]) == 1 ? true : false;
    }

    public int getPort() throws SlimConnectionException {
        return Integer.parseInt(getSlimServer().sendCommand(
                SLIM_PROP_PLAYER_IP.replaceAll(PARAM_PLAYER_ID, getId()))[0].split(":")[1]);
    }

    /**
     * Convenience method
     * @return true if sleep time is set
     * @throws SlimConnectionException
     */
    public boolean isWillSleep() throws SlimConnectionException {
        return getSleepTime() > 0;
    }

    public int getSleepTime() throws SlimConnectionException {
        String s = getSlimServer().sendCommand(
                SLIM_PROP_PLAYER_SLEEP_QUERY.replaceAll(PARAM_PLAYER_ID, getId()))[0];

        if (s.contains(".")) {
            s = s.split("\\.")[0];
        }
        return Integer.parseInt(s);
    }

    /**
     * Sets the amount of time before the player goes to sleep.  Pass a 0 to stop the player from sleeping.
     * @param seconds before sleep
     * @throws SlimConnectionException
     */
    public void setSleepTime(int seconds) throws SlimConnectionException {
        String command = SLIM_PROP_PLAYER_SLEEP.replaceAll(PARAM_PLAYER_ID, getId());
        command = command.replaceAll(PARAM_MARKER, Integer.toString(seconds));

        getSlimServer().sendCommand(command);
    }

    public boolean isPowered() throws SlimConnectionException {
        return (getSlimServer().sendCommand(
                SLIM_PROP_PLAYER_POWER_QUERY.replaceAll(PARAM_PLAYER_ID, getId()))[0]).equals("1");
    }

    public void powerOn() throws SlimConnectionException {
        getSlimServer().sendCommand(SLIM_PROP_PLAYER_POWER.replaceAll(PARAM_PLAYER_ID, getId()), "1");
        firePlayerEvent(new PlayerChangeEvent(this, PlayerChangeEvent.PLAYER_ON));
    }

    public void powerOff() throws SlimConnectionException {
        getSlimServer().sendCommand(SLIM_PROP_PLAYER_POWER.replaceAll(PARAM_PLAYER_ID, getId()), "0");
        firePlayerEvent(new PlayerChangeEvent(this, PlayerChangeEvent.PLAYER_OFF));
    }

    public void play() throws SlimConnectionException {
        getSlimServer().sendCommand(SLIM_PROP_PLAYER_PLAY.replaceAll(PARAM_PLAYER_ID, getId()));
        firePlayerEvent(new PlayerChangeEvent(this, PlayerChangeEvent.PLAYER_STARTED));
    }

    public void stop() throws SlimConnectionException {
        getSlimServer().sendCommand(SLIM_PROP_PLAYER_STOP.replaceAll(PARAM_PLAYER_ID, getId()));
        firePlayerEvent(new PlayerChangeEvent(this, PlayerChangeEvent.PLAYER_STOPPED));
    }

    public void pause() throws SlimConnectionException {
        getSlimServer().sendCommand(SLIM_PROP_PLAYER_PAUSE.replaceAll(PARAM_PLAYER_ID, getId()));
        firePlayerEvent(new PlayerChangeEvent(this, PlayerChangeEvent.PLAYER_PAUSED));
    }

    public boolean isPlaying() throws SlimConnectionException {
        return getMode().equals(PlayerStatus.STATUS_PLAYING);
    }

    public boolean isStopped() throws SlimConnectionException {
        return getMode().equals(PlayerStatus.STATUS_STOPPED);
    }

    public boolean isPaused() throws SlimConnectionException {
        return getMode().equals(PlayerStatus.STATUS_PAUSED);
    }

    public PlayerStatus getMode() throws SlimConnectionException {
        String response =
                getSlimServer().sendCommand(SLIM_PROP_PLAYER_MODE.replaceAll(PARAM_PLAYER_ID, getId()))[0];

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
     * @param volume if less that 0 sets to 0, if greater than 100 sets to 100
     */
    public void setVolume(int volume) throws SlimConnectionException {
        if (volume > 100) {
            volume = 100;
        }
        if (volume < 0) {
            volume = 0;
        }

        getSlimServer().sendCommand(SLIM_PROP_PLAYER_VOLUME.replaceAll(PARAM_PLAYER_ID, getId()), Integer.toString(volume));

        fireVolumeEvent(new VolumeChangeEvent(this, volume));
    }

    /**
     * Returns the volume of the player.  If muted, a negative number is returned
     * @return the volume of the player
     */
    public int getVolume() throws SlimConnectionException {
        return Integer.parseInt(getSlimServer().sendCommand(
                SLIM_PROP_PLAYER_VOLUME_QUERY.replaceAll(PARAM_PLAYER_ID, getId()))[0]);
    }

    /**
     * Returns the time of the currently playing song
     * @return the elapsed time
     */
    public int getElapsedTime() throws SlimConnectionException {
        return (int) Double.parseDouble(getSlimServer().sendCommand(
                SLIM_PROP_PLAYER_TIME_QUERY.replaceAll(PARAM_PLAYER_ID, getId()))[0].split("\\\\.")[0]);
    }

    /**
     * Seeks to a particular point in the currently playing song.
     * @param time in seconds
     */
    public void seek(int time) throws SlimConnectionException {
        if (time < 0) {
            time = 0;
        }

        String cmd = SLIM_PROP_PLAYER_TIME.replaceAll(PARAM_PLAYER_ID, getId());

        getSlimServer().sendCommand(cmd, Integer.toString(time));

        firePlayerEvent(new PlayerChangeEvent(this, PlayerChangeEvent.PLAYER_SEEKING));
    }

    public void advance(int numSeconds) throws SlimConnectionException {
        if (numSeconds < 0) {
            numSeconds = 0;
        }

        String cmd = SLIM_PROP_PLAYER_TIME.replaceAll(PARAM_PLAYER_ID, getId());

        getSlimServer().sendCommand(cmd, "+" + Integer.toString(numSeconds));

        firePlayerEvent(new PlayerChangeEvent(this, PlayerChangeEvent.PLAYER_SEEKING));
    }

    public void backup(int numSeconds) throws SlimConnectionException {
        if (numSeconds < 0) {
            numSeconds = 0;
        }

        String cmd = SLIM_PROP_PLAYER_TIME.replaceAll(PARAM_PLAYER_ID, getId());

        getSlimServer().sendCommand(cmd, "-" + Integer.toString(numSeconds));

        firePlayerEvent(new PlayerChangeEvent(this, PlayerChangeEvent.PLAYER_SEEKING));
    }

    /**
     * Toggles mute
     */
    public void mute() throws SlimConnectionException {
        getSlimServer().sendCommand(SLIM_PROP_PLAYER_MUTE.replaceAll(PARAM_PLAYER_ID, getId()), "");

        if (isMuted()) {
            firePlayerEvent(new PlayerChangeEvent(this, PlayerChangeEvent.PLAYER_MUTED));
        } else {
            firePlayerEvent(new PlayerChangeEvent(this, PlayerChangeEvent.PLAYER_UNMUTED));
        }
    }

    public boolean isMuted() throws SlimConnectionException {
        return (getSlimServer().sendCommand(
                SLIM_PROP_PLAYER_MUTE_QUERY.replaceAll(PARAM_PLAYER_ID, getId()))[0]).equals("1");
    }

    public void syncPlayer(SlimPlayer player) throws SlimConnectionException, SlimPlayerException {
        if (player == null) {
            throw new SlimPlayerException("To player cannot be null.");
        }

        String command = SLIM_PROP_SYNC.replaceFirst(PARAM_PLAYER_ID, player.getId());
        command = command.replaceFirst(PARAM_PLAYER_ID, getId());

        getSlimServer().sendCommand(command);

        firePlayerEvent(new PlayerChangeEvent(this, PlayerChangeEvent.PLAYER_SYNCED));
    }

    public void unsyncPlayer() throws SlimConnectionException {
        String command = SLIM_PROP_SYNC.replaceAll(PARAM_MARKER, "-");
        command = command.replaceFirst(PARAM_PLAYER_ID, getId());
        command = command.replaceFirst(PARAM_PLAYER_ID, "-");

        getSlimServer().sendCommand(command);

        firePlayerEvent(new PlayerChangeEvent(this, PlayerChangeEvent.PLAYER_UNSYNCED));
    }

    public boolean isSynced() throws SlimConnectionException {
        String command = SLIM_PROP_SYNC_QUERY.replaceAll(PARAM_PLAYER_ID, getId());

        String[] response = getSlimServer().sendCommand(command);

        return !"-".equalsIgnoreCase(response[0]);

    }

    /**
     * Returns a Collection of player ids that are synced with this player.
     *
     * @return
     * @throws SlimConnectionException
     */
    public Collection<SlimPlayer> getSyncedPlayerIds() throws SlimConnectionException {
        String command = SLIM_PROP_SYNC_QUERY.replaceAll(PARAM_PLAYER_ID, getId());
        String[] response = getSlimServer().sendCommand(command);

        List<SlimPlayer> ids = new ArrayList<SlimPlayer>();

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
                        ids.add(new SlimPlayer(strId, slimServer));
                    }
                }
            }
        }
        return ids;
    }

    public SlimServer getSlimServer() {
        return slimServer;
    }

    public void setSlimServer(SlimServer slimServer) {
        this.slimServer = slimServer;
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

        SlimPlayer player = (SlimPlayer) object;
        if (this.getId().equals(player.getId())) {
            return (true);
        } else {
            return (false);
        }

    }

    /**
     * Overrides hashcode method
     * @return the hash code
     */
    public int hashCode() {
        int hash = 7;
        try {
            hash = 31 * hash + (int) getPort();
            hash = 31 * hash + (null == getName() ? 0 : getName().hashCode());
        } catch (SlimConnectionException ex) {
            Logger.getLogger(SlimPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return (hash);
    }

    @Override
    public String toString() {
        try {
            return getName();
        } catch (SlimConnectionException ex) {
            Logger.getLogger(SlimPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return getId();
    }
}
