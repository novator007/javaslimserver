package org.bff.squeezeserver.monitor;

import org.bff.squeezeserver.Player;
import org.bff.squeezeserver.events.ErrorEvent;
import org.bff.squeezeserver.events.ErrorListener;
import org.bff.squeezeserver.exception.ConnectionException;
import org.bff.squeezeserver.exception.SqueezeException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * StandAloneMonitor monitors a SqueezeServer connection by querying the status and
 * statistics of the SqueezeServer server at given delay intervals.  As statistics change
 * appropriate events are fired indicating these changes.  If more detailed
 * events are desired attach listeners to the different slim objects.
 * <p/>
 * This class doesn't know any details about what caused events to occur so null
 * will be passed for specific event details such as a item being added to the
 * playlist.
 *
 * @author Bill Findeisen
 * @version 1.0
 */
public class StandAloneMonitor
        extends EventListener
        implements Runnable {

    private final int delay;
    private boolean stopped;
    private int oldPos;
    private static final SimpleDateFormat SDF_DEBUG = new SimpleDateFormat("MM/dd/yy HH:mm:ss.SSS");

    /**
     * Sets the {@link org.bff.squeezeserver.Player} for the monitor
     *
     * @param player the {@link org.bff.squeezeserver.Player}
     * @throws org.bff.squeezeserver.exception.SqueezeException
     *          if there as a connection
     *          problem
     */
    public void setPlayer(Player player) throws SqueezeException, IOException {
        super.setPlayer(player);
    }

    private static final int DEFAULT_DELAY = 1000;

    private List<ErrorListener> errorListeners =
            new ArrayList<ErrorListener>();


    /**
     * Creates a new instance of StandAloneMonitor using the default delay
     * of 1 second.
     *
     * @param player the slim player to monitor
     * @throws org.bff.squeezeserver.exception.SqueezeException
     *          if a connection can't be made witht the player
     */
    public StandAloneMonitor(Player player) throws SqueezeException {
        this(player, DEFAULT_DELAY);
    }

    /**
     * Creates a new instance of StandAloneMonitor using the given delay interval
     * for queries.
     *
     * @param player the Player to monitor
     * @param delay  the delay interval
     * @throws org.bff.squeezeserver.exception.SqueezeException
     *          if there is a problem initializing the player
     */
    public StandAloneMonitor(Player player, int delay) throws SqueezeException {
        super(player);

        this.delay = delay;
    }

    /**
     * Adds a {@link org.bff.squeezeserver.events.ErrorListener} to this object to receive
     * {@link org.bff.squeezeserver.events.ErrorEvent}s.
     *
     * @param el the ErrorListener to add
     */
    public synchronized void addSlimErrorListener(ErrorListener el) {
        errorListeners.add(el);
    }

    /**
     * Removes a {@link org.bff.squeezeserver.events.ErrorListener} from this object.
     *
     * @param el the ErrorListener to remove
     */
    public synchronized void removeSlimErrorListener(ErrorListener el) {
        errorListeners.remove(el);
    }

    /**
     * Sends the appropriate {@link org.bff.squeezeserver.events.ErrorListener} to all registered
     * {@link org.bff.squeezeserver.events.ErrorListener}s.
     *
     * @param msg the event message
     */
    protected synchronized void fireSlimErrorEvent(String msg) {
        ErrorEvent ee = new ErrorEvent(this, msg);

        for (ErrorListener el : errorListeners) {
            el.errorEventReceived(ee);
        }
    }

    private static int count = 0;

    /**
     * Implements the Runnable run method to monitor the SqueezeServer connection.
     */
    @Override
    public void run() {

        while (!isStopped()) {

            try {
                //checkTrackPosition();
            } catch (Exception e) {

                if (e instanceof ConnectionException) {
                    setConnectedState(false);
                    fireConnectionChangeEvent(false);
                    boolean retry = true;

                    while (retry) {

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(StandAloneMonitor.class.getName()).log(Level.SEVERE, null, ex);
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
     *
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
    protected final void checkTrackPosition() throws ConnectionException {
        int newPos = getPlayer().getElapsedTime();
        if (oldPos != newPos) {
            oldPos = newPos;
            fireTrackPositionChangeEvent(newPos);
        }
    }
}
