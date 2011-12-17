package org.bff.slimserver.monitor;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bff.slimserver.SlimPlayer;
import org.bff.slimserver.events.PlaylistChangeEvent;
import org.bff.slimserver.events.SlimErrorEvent;
import org.bff.slimserver.events.SlimErrorListener;
import org.bff.slimserver.exception.SlimConnectionException;
import org.bff.slimserver.exception.SlimException;
import org.bff.slimserver.musicobjects.SlimPlaylistItem;

/**
 * SlimStandAloneMonitor monitors a SlimServer connection by querying the status and
 * statistics of the SlimServer server at given delay intervals.  As statistics change
 * appropriate events are fired indicating these changes.  If more detailed
 * events are desired attach listeners to the different slim objects.
 *
 * This class doesn't know any details about what caused events to occur so null
 * will be passed for specific event details such as a item being added to the
 * playlist.
 * 
 * @author Bill Findeisen
 * @version 1.0
 */
public class SlimStandAloneMonitor
        extends SlimEventListener
        implements Runnable {

    private final int delay;
    private boolean stopped;
    private int oldPos;
    private static final SimpleDateFormat SDF_DEBUG = new SimpleDateFormat("MM/dd/yy HH:mm:ss.SSS");
    
    /**
     * Sets the {@link SlimPlayer} for the monitor
     * @param player the {@link SlimPlayer}
     * @throws org.bff.slimserver.exception.SlimException if there as a connection
     * problem
     */
    public void setPlayer(SlimPlayer player) throws SlimException, IOException {
        super.setPlayer(player);
    }

    private static final int DEFAULT_DELAY = 1000;
          
    private List<SlimErrorListener> errorListeners =
            new ArrayList<SlimErrorListener>();
    

    /**
     * Creates a new instance of SlimStandAloneMonitor using the default delay
     * of 1 second.
     * 
     * @param player the slim player to monitor
     * @throws SlimException if a connection can't be made witht the player
     */
    public SlimStandAloneMonitor(SlimPlayer player) throws SlimException {
        this(player, DEFAULT_DELAY);
    }

    /**
     * Creates a new instance of SlimStandAloneMonitor using the given delay interval
     * for queries.
     * 
     * @param player the SlimPlayer to monitor
     * @param delay the delay interval
     * @throws SlimException if there is a problem initializing the player
     */
    public SlimStandAloneMonitor(SlimPlayer player, int delay) throws SlimException {
        super(player);

        this.delay = delay;
    }    

    /**
     * Adds a {@link SlimErrorListener} to this object to receive
     * {@link SlimErrorEvent}s.
     * @param el the SlimErrorListener to add
     */
    public synchronized void addSlimErrorListener(SlimErrorListener el) {
        errorListeners.add(el);
    }

    /**
     * Removes a {@link SlimErrorListener} from this object.
     * @param el the SlimErrorListener to remove
     */
    public synchronized void removeSlimErrorListener(SlimErrorListener el) {
        errorListeners.remove(el);
    }

    /**
     * Sends the appropriate {@link SlimErrorListener} to all registered
     * {@link SlimErrorListener}s.
     * @param msg the event message
     */
    protected synchronized void fireSlimErrorEvent(String msg) {
        SlimErrorEvent ee = new SlimErrorEvent(this, msg);

        for (SlimErrorListener el : errorListeners) {
            el.errorEventReceived(ee);
        }
    }
    private static int count = 0;

    /**
     * Implements the Runnable run method to monitor the SlimServer connection.
     */
    @Override
    public void run() {

        while (!isStopped()) {

            try {
                //checkTrackPosition();
            } catch (Exception e) {

                if (e instanceof SlimConnectionException) {
                    setConnectedState(false);
                    fireConnectionChangeEvent(false);
                    boolean retry = true;

                    while (retry) {

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(SlimStandAloneMonitor.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        checkConnection();
                        if (isConnectedState()) {
                            retry = false;
                        }
                    }
                } else {
                    fireSlimErrorEvent(e.getMessage());
                }
                e.printStackTrace();
            }

            try {
                synchronized (this) {
                    this.wait(delay);
                }
            } catch (InterruptedException ie) {
            }
        }
    }

    /**
     * Starts the monitor by creating and starting a thread using this instance
     * as the Runnable interface.
     */
    public void start() {
        super.start();
        Thread th = new Thread(this);
        th.start();
    }

    /**
     * Stops the thread.
     */
    public void stop() {
        super.stop();
        setStopped(true);
    }

    /**
     * Returns true if the monitor is stopped, false if the monitor is still running.
     * @return true if monitor is running, false otherwise false
     */
    public boolean isStopped() {
        return stopped;
    }

    
    private void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    /**
     * Checks the track position and fires a {@link TrackPositionChangeEvent} if
     * there has been a change in track position.
     */
    protected final void checkTrackPosition() throws SlimConnectionException {
        int newPos = getPlayer().getElapsedTime();
        if (oldPos != newPos) {
            oldPos = newPos;
            fireTrackPositionChangeEvent(newPos);
        }
    }
}
