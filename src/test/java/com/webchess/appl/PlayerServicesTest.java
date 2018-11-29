package com.webchess.appl;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


@Tag("Application-tier")
/**
 * This class is a tester for the PlayerLobby class
 */

public class PlayerServicesTest {

    @Test
    public void notnullPlayerServices() {
        GameCenter CuT = new GameCenter();
        // Invoke test
        PlayerServices playerSvc = new PlayerServices(CuT);
        // Analyze results
        assertNotNull(playerSvc);
    }

    /**
     * Test if the PlayerServices object is null.
     */
    @Test
    public void nullPlayerServices(){
        GameCenter gameCenter = new GameCenter();
        PlayerServices playerServices = gameCenter.newPlayerServices();
        assertEquals(playerServices.getUsername(),null);
    }

    /**
     * Test if they player name that signed in matches the one provided.
     */
    @Test
    public void UserPlayerServices(){
        GameCenter gameCenter = new GameCenter();
        PlayerServices playerServices = gameCenter.newPlayerServices();
        playerServices.SignIn("Jack");
        assertEquals(playerServices.getUsername(),"Jack");
    }

    /**
     * Tests if multiple users can join the lobby, if the lobby size is correctly managed, and if players with duplicate names are handled correctly.
     */
    @Test
    public void MultipleUserPlayerServices(){
        GameCenter gameCenter = new GameCenter();
        PlayerServices playerService1 = gameCenter.newPlayerServices();
        playerService1.SignIn("Jack");
        PlayerServices playerService2 = gameCenter.newPlayerServices();
        playerService2.SignIn("Jack");
        assertEquals(gameCenter.getPlayerLobby().PlayerList.size(),1);
        PlayerServices playerService3 = gameCenter.newPlayerServices();
        playerService3.SignIn("Bob");
        assertEquals(gameCenter.getPlayerLobby().PlayerList.size(),2);
    }
}
