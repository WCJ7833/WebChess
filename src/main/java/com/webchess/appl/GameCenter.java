package com.webchess.appl;

import com.webchess.model.*;

import java.util.*;
import java.util.logging.Logger;


/**
 * The object to coordinate the state of the Web Application and keep sitewide statistics.
 *
 * This class is an example of the Pure Fabrication principle.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 * @author <a href='mailto:jrv@se.rit.edu'>Jim Vallino</a>
 */
public class GameCenter {
    private static final Logger LOG = Logger.getLogger(GameCenter.class.getName());
    private PlayerLobby playerLobby;
    private Replay replayStorage;
    //
    // Constants
    //

    // Output strings made public for unit test access


    //
    // Attributes
    //

    //
    // Constructors
    //
    public GameCenter(){
        this.replayStorage = new Replay();
        this.playerLobby = new PlayerLobby();
    }
    //
    // Public methods
    //

    /**
     * Get a new {@Linkplain PlayerServices} object to provide client-specific services to
     * the client who just connected to this application.
     *
     * @return
     *   A new {@Link PlayerServices}
     */
    public PlayerServices newPlayerServices() {
        LOG.fine("New player services instance created.");
        PlayerServices playerServices = new PlayerServices(this);
        assert playerServices.getUsername() == null  : "PlayerServices not properly initiated";
        return playerServices;
    }

    /**
     * Getter for the PlayerLobby of the GameCenter, which contains data from all players signed in
     * @return PlayerLobby of the GameCenter
     */
    public PlayerLobby getPlayerLobby() {
        return playerLobby;
    }

    /**
     * Getter for the Replay storage class that contains prior saved games
     * @return Replay storage class
     */
    public Replay getReplayStorage(){
        return this.replayStorage;
    }
}
