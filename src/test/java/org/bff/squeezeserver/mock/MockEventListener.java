package org.bff.squeezeserver.mock;

import org.bff.squeezeserver.Player;
import org.bff.squeezeserver.monitor.EventListener;

/**
 * Created by IntelliJ IDEA.
 * User: bill
 * Date: 1/22/12
 * Time: 2:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class MockEventListener extends EventListener {
    public MockEventListener(Player player) {
        super(player);
    }

    @Override
    protected void initialize() {
    }
}
