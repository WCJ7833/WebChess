package com.webchess.appl;


import com.webchess.model.Player;

import java.util.*;
import java.util.regex.*;

/**
 * The object to coordinate the state of the Web Application.
 *
 * This class is an example of the GRASP Controller principle.
 */

public class PlayerServices {

    //
    // Constants
    //

    final static String NO_WINS_MESSAGE = "You have not won a game, yet. But I *feel* your luck changing.";
    final static String GAMES_PLAYED_FORMAT = "You have won an average of %.1f%% of this session's %d game.";

    //
    // Attributes
    //
    private String Username = null;
    // This player's game. There is only one game at a time allowed.
    // The gameCenter provides sitewide features for all the games and players.
    private final com.webchess.appl.GameCenter gameCenter;

    /**
     * Construct a new {@Linkplain PlayerServices} but wait for the player to want to start a game.
     *
     * @param gameCenter
     *    the {@Link GameCenter} that has sitewide responsibilities
     */
    PlayerServices(com.webchess.appl.GameCenter gameCenter) {
        this.gameCenter = gameCenter;
    }

    public synchronized int SignIn(String username){
        List<Player> playerServicesList = gameCenter.getPlayerLobby().getPlayerList();
        for (Player p: playerServicesList){
            if (p.getName() == null){
                continue;
            }
            else if (p.getName().equals(username)){
                return 1;
            }
        }
        Pattern p = Pattern.compile("[^a-zA-Z0-9\\s]");
        boolean check = p.matcher(username).find();
        if (!check){
            gameCenter.getPlayerLobby().addPlayerList(username);
            this.Username = username;
            return 0;
        }
        return 2;
    }
    public String getUsername() {
        return Username;
    }

    public void endSession() {

        //game = null;
    }

}
