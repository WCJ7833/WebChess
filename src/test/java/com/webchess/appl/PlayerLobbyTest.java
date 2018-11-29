package com.webchess.appl;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.webchess.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import java.util.*;


@Tag("Application-tier")
/**
 * This class is a tester for the PlayerLobby class
 */

public class PlayerLobbyTest {
    private Session session;
    private PlayerLobby playerLobby;
    @BeforeEach
    public void setup(){
        // create a unique CuT for each test
        playerLobby = new PlayerLobby();
    }

    /**
     * Tests if the player lobby is empty
     */
    @Test
    public void emptyPlayerLobby(){
        assertTrue(playerLobby.PlayerList.isEmpty());
    }

    /**
     * Tests if the player lobby is not empty
     */
    @Test
    public void nonemptyPlayerLobby(){
        playerLobby.addPlayerList("Tom");
        assertFalse(playerLobby.PlayerList.isEmpty());
    }
    /**
     * Tests if the player lobby is not empty
     */
    @Test
    public void multipleuserPlayerLobby(){
        playerLobby.addPlayerList("Tom");
        assertEquals(playerLobby.PlayerList.size(),1);
        playerLobby.addPlayerList("Max");
        assertEquals(playerLobby.PlayerList.size(),2);
        Player p = playerLobby.getPlayer("Max");
        playerLobby.removePlayerList(p);
        assertEquals(playerLobby.PlayerList.size(),1);
    }

    @Test
    public void signedInPlayerLobby(){
        playerLobby.addPlayerList("Tom");
        List<String> players = playerLobby.PlayerList("Tom");
        List<String> emptyList = new ArrayList<>();
        assertEquals(players,emptyList);
        playerLobby.addPlayerList("Max");
        players = playerLobby.PlayerList("Tom");
        assertEquals(players.get(0),"Max");
        players = playerLobby.PlayerList("Max");
        assertEquals(players.get(0),"Tom");
    }
    @Test
    public void notsignedInPlayer(){
        String players0 = playerLobby.PlayerCountString();
        assertEquals(players0,"There are 0 players currently in the lobby.");
        playerLobby.addPlayerList("Tom");
        String players1 = playerLobby.PlayerCountString();
        assertEquals(players1,"There is 1 player currently in the lobby.");
        playerLobby.addPlayerList("Max");
        String players2 = playerLobby.PlayerCountString();
        assertEquals(players2,"There are 2 players currently in the lobby.");
    }

    @Test
    public void accessNonExistentPlayer(){
        assertEquals(playerLobby.getPlayer("Tom"),null);
    }

    @Test
    public void accessPlayer(){
        playerLobby.addPlayerList("Tom");
        Player newPlayer = new Player("Tom");
        assertEquals(playerLobby.getPlayer("Tom"),newPlayer);
    }

    @Test void playerLobbySession(){
        session = mock(Session.class);
        assertEquals(playerLobby.getSession(),null);
        playerLobby.setSession(session);
        assertEquals(playerLobby.getSession(),session);
    }

}
