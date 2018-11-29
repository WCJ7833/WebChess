package com.webchess.model;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/**
 * This is a JUnit test class that tests the Player class in the model tier
 * @author Brunon Sztuba bjs2864@rit.edu
 */
@Tag("Model-tier")
public class PlayerTest {
    /**
     * This is a list of piece mocks used for testing purposes
     */
    private List<Piece> pieces=new ArrayList();

    /**
     * This is a mock of a piece used for testing
     */
    private Piece p1;

    /**
     * This is a mock of another piece used for testing
     */
    private Piece p2;

    /**
     * This method resets the data members for each test
     */
    @BeforeEach
    public void setup(){
        pieces=new ArrayList<>();
        p1=mock(Piece.class);
        p2=mock(Piece.class);
        pieces.add(p1);
        pieces.add(p2);
    }

    /**
     * this test asserts that the player gets the proper username
     */
    @Test
    public void checkName(){
        Player player=new Player("bannanagram");
        assertTrue(player.getName().equals("bannanagram"),"Does not get correct name!");
    }

    /**
     * this test assures that adding pieces works properly
     */
    @Test
    public void checkAdd(){
        Player player=new Player("george");
        player.addPiece(p1);
        player.addPiece(p2);
        player.addPiece(p2);
        pieces.add(p2);
        assertTrue(player.getPieces().equals(pieces),"addPiece does not correctly add a piece");
    }

    /**
     * this test ensures that getPieces works proerly
     */
    @Test
    public void checkGetPieces(){
        Player player=new Player("");
        player.addPiece(p1);
        player.addPiece(p2);
        assertTrue(player.getPieces().equals(pieces),"getPieces, or pieces themselves, is wrong");
    }

    /**
     * this test ensures that inGame works properly for a newly-created player
     */
    @Test
    public void checkInGame(){
        Player player=new Player("mark");
        assertTrue(player.getInGame().equals(false),"New players are being created as if already in a game!");
    }

    /**
     * this test ensures that setting the color of the player to red works correctly
     */
    @Test
    public void checkSetColor() {
        Player player = new Player("Sarah");
        player.setColor(Piece.COLOR.RED);
        assertTrue(player.getColor().equals(Piece.COLOR.RED), "setColor is wrong");
    }

    /**
     * This test ensures that setting the color of the player to white works correctly
     */
    @Test
    public void checkSetColor2(){
        Player player = new Player("Sarah");
        player.setColor(Piece.COLOR.WHITE);
        assertTrue(player.getColor().equals(Piece.COLOR.WHITE), "setColor is wrong");
        }

    /**
     * this test ensures that getting the color of the player works properly
     */
    @Test
    public void checkGetColor(){
        Player player=new Player("");
        player.setColor(Piece.COLOR.WHITE);
        assertTrue(player.getColor().equals(Piece.COLOR.WHITE),"getColor is wrong");
    }

    /**
     * this test ensures that two identical players are seen as equal
     */
    @Test
    public void checkEquals(){
        Player player=new Player("Brianna");
        Player player2=new Player("Brianna");
        assertTrue(player.equals(player2),"Player not equal to identical player");
    }

    /**
     * this test ensures that two players who are identical in every way but name are not equa;
     */
    @Test
    public void checkEquals2(){
        Player player=new Player("John");
        Player player2=new Player("john");
        assertFalse(player.equals(player2),"Players with different names are the same!");
    }

    /**
     * this test ensures that players are not seen as equal to objects other than Player objects
     */
    @Test
    public void checkEquals3(){
        Player player=new Player("John");
        assertFalse(player.equals(5),"Players equal...ints?");
    }

    /**
     * this test ensures that the hashing function for player works properly
     */
    @Test
    public void checkHash(){
        Player player=new Player("iewnfwioengweng");
        assertTrue(player.hashCode()=="iewnfwioengweng".hashCode(),"hashCode is wrong");
    }

    /**
     * this test ensures that players who are created are not, by default, in the position of stalemate
     */
    @Test
    public void checkLost(){
        Player player=new Player("Jeremy");
        assertTrue(player.lost(),"Player not in game, with no pieces, has won");
    }

    /**
     * this test ensures that players who are in a game with no pieces have not won
     */
    @Test
    public void checkLost2(){
        Player player=new Player("Jeremy");
        player.setInGame(true);
        assertTrue(player.lost(),"Player in game with no pieces has won");
    }

    /**
     * this test ensures that players in a game with pieces that have not stalemated have not lost
     */
    @Test
    public void checkLost3(){
        Player player=new Player("Jeremy");
        player.setInGame(true);
        player.addPiece(p1);
        assertTrue(!player.lost(),"Player in game with pieces lost");
    }

    /**
     * this test ensures that players in a game with pieces but that have stalemated lost
     */
    @Test
    public void checkLost4(){
        Player player=new Player("");
        player.setInGame(true);
        player.addPiece(p1);
        player.setLost(true);
        assertTrue(player.lost(),"Player who stalemated didn't lose");
    }

    /**
     * this test ensures that the player who has no pieces left and is in a game loses
     */
    @Test
    public void checkLost5(){
        Player player=new Player("");
        player.setInGame(true);
        assertTrue(player.lost(),"Player who lost all pieces didn't lose");
    }

    /**
     * this test ensures that a play who leaves a game by forfeiting loses
     */
    @Test
    public void checkLost6(){
        Player player=new Player("");
        player.setInGame(false);
        player.addPiece(p1);
        assertTrue(player.lost(),"Player who left game didn't lose");
    }

    /**
     * this test ensures that setTurn makes a player active
     */
    @Test
    public void checkSetTurn(){
        Player player=new Player("");
        player.setTurn();
        assertTrue(player.isTurn(),"setTurn not working");
    }

    /**
     * this test ensures that toggle turn correctly ends a player's turn
     */
    @Test
    public void checkToggleTurn(){
        Player player=new Player("");
        player.setTurn();
        player.toggleTurn();
        assertTrue(!player.isTurn(),"Toggling to not turn doesn't work");
    }

    /**
     * this test ensures that toggleTurn correctly starts a player's turn
     */
    @Test
    public void checkToggleTurn2(){
        Player player=new Player("");
        player.clearTurn();
        player.toggleTurn();
        assertTrue(player.isTurn(),"Toggling to turn doesn't work");
    }

    /**
     * this test ensures that clear turn correctly ends a player's turn
     */
    @Test
    public void checkClearTurn(){
        Player player=new Player("");
        player.setTurn();
        player.clearTurn();
        assertTrue(!player.isTurn(),"clearTurn not working right");
    }

    /**
     * this test ensures that isTurn correctly returns false when it is not a player's turn
     */
    @Test
    public void checkIsTurn(){
        Player player=new Player("");
        assertTrue(!player.isTurn(),"isTurn is wrong");
    }

    /**
     * this test ensures that isTurn correctly returns true when it is a player's turn
     */
    @Test
    public void checkIsTurn2(){
        Player player=new Player("");
        player.setTurn();
        assertTrue(player.isTurn(),"isTurn is wrong");
    }

    /**
     * this test ensures that setting lost correctly makes a player lose
     */
    @Test
    public void checkSetLost(){
        Player player=new Player("");
        player.setLost(true);
        assertTrue(player.lost(),"setLost not working right");
    }

    /**
     * this test ensures that setting lost for a stalemate correctly makes a player lose
     */
    @Test
    public void checkSetLost2(){
        Player player=new Player("");
        player.setLost(false);
        player.addPiece(p1);
        player.setInGame(true);
        assertTrue(!player.lost(),"setLost not working right");
    }
}
