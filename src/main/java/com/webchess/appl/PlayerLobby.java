package com.webchess.appl;

import com.webchess.model.*;
import spark.Session;

import java.util.*;

/**
 * Object that acts as a lobby for the Web Checkers application. It contains a list of players who
 * are signed in the application
 *
 * @author Gabriel Jusino
 * @author William Johnson
 */
public class PlayerLobby {

    public List<Player> PlayerList;
    private Session session;
    public PlayerLobby(){
        this.PlayerList = new ArrayList<>();
    }

    /**
     * Adds new player into the lobby
     * @param username name of the new player
     */
    public synchronized void addPlayerList(String username){
        PlayerList.add(new Player(username));
    }

    /**
     * Removes a player from the lobby
     * @param user the user to be removed
     */
    public synchronized void removePlayerList(Player user){
        PlayerList.remove(user);
    }

    /**
     * Sets the session of the game for players in a game
     * @param session session of the current game
     */
    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * Gets the session of the game
     * @return session of the game
     */
    public Session getSession(){
        return this.session;
    }

    /**
     * Gets a player from the lobby
     * @param userName the requested player to get
     * @return the requested player
     */
    public synchronized Player getPlayer(String userName){
        for(Player p: PlayerList){
            if (p.getName().equals(userName)){
                return p;
            }
        }
        return null;
    }

    /**
     * Gets the list of players in the lobby
     * @return list of players
     */
    public synchronized List<Player> getPlayerList() {
        return PlayerList;
    }

    /**
     * Prints the amount of players in the lobby
     * @return the amount of players
     */
    public synchronized String PlayerCountString() {
        int playerCount = PlayerList.size();
        if (playerCount == 1){
            return String.format("There is %s player currently in the lobby.", playerCount);
        }
        else {
            return String.format("There are %s players currently in the lobby.", playerCount);
        }
    }

    /**
     * Returns a string list of the players, besides the user who is signed in, of the lobby
     * @param currentUser user who is signed in
     * @return string list of players in the application
     */
    public synchronized List<String> PlayerList(String currentUser) {
        List<String> players = new ArrayList<>();
        for (Player p: PlayerList) {
            if (!p.getName().equals(currentUser)){
                players.add(p.getName());
            }
        }
        return players;
    }

}
