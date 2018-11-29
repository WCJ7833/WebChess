package com.webchess.ui;

import com.webchess.appl.*;

import javax.servlet.http.*;
import java.util.*;
import java.util.logging.*;

/**
 * This class handles players joining and leaving the server, cleaning up after they leaves
 *
 * @author <a href='wcj7833@rit.edu'>William Johnson</a>
 */
public class SessionTimeoutWatchdog implements HttpSessionBindingListener{

    private static final Logger LOG = Logger.getLogger(SessionTimeoutWatchdog.class.getName());

        private final PlayerServices playerServices;

    /**
     * Creates a watchdog to keep track of player activity
     *
     * @param playerServices manages game resources
     */
    public SessionTimeoutWatchdog(final PlayerServices playerServices) {
            LOG.fine("Watch dog created.");
            this.playerServices = Objects.requireNonNull(playerServices);
        }

        @Override
        /**
         *Logs the a session is starting
         */
        public void valueBound(HttpSessionBindingEvent event) {
            // ignore this event
            LOG.fine("Player session started.");
        }

        @Override
        /**
         * Terminates an idle session and logs the session ending.
         */
        public void valueUnbound(HttpSessionBindingEvent event) {
            // the session is being terminated do some cleanup
            playerServices.endSession();
            //
            LOG.fine("Player session ended.");
        }
    }


