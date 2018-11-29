package com.webchess.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * This class tests the methods and functionality of the Replay class
 * @author Sean Dunn smd6336@rit.edu
 */
public class ReplayTest{

    /**
     * Name of the first player in the game
     */
    String player1;

    /**
     * Name of the second player in the game
     */
    String player2;

    /**
     * The number of turns in the game
     */
    int size;

    /**
     * The list of moves made during the game
     */
    ArrayList<Move> moves;

    /**
     * Sets up the test
     */
    @BeforeEach
    public void setup(){
        moves = new ArrayList<Move>();
        size = 0;
    }

    @Test
    public void testAddTurn(){

    }

    /**
     * Tests the getPlayer1() method
     */
    @Test
    public void testGetPlayer1(){
        Replay r = new Replay();
        r.setPlayers("Joe", "Steve");
        assertTrue(r.getPlayer1().equals("Joe"));
    }

    /**
     * Tests the getPlayer2() method
     */
    @Test
    public void testGetPlayer2(){
        Replay r = new Replay();
        r.setPlayers("Joe", "Steve");
        assertTrue(r.getPlayer2().equals("Steve"));
    }

    /**
     * Tests the getNumTurns() method
     */
    @Test
    public void testGetNumTurns(){
        Replay r = new Replay();
        assertEquals(0, r.getNumTurns());
    }

    @Test
    public void testGetMoves(){
        Replay r = new Replay();
    }

    /**
     * Tests the equals() method
     */
    @Test
    public void testEquals1(){
        Replay r1 = new Replay();
        Replay r2 = new Replay();
        r1.setPlayers("Test", "Test");
        r2.setPlayers("Test", "Tes");
        assertFalse(r1.equals(r2));
    }

    @Test
    public void testEquals2(){
        Replay r1 = new Replay();
        Replay r2 = new Replay();
        r1.setPlayers("Test", "Test");
        r2.setPlayers("Test", "Test");
    }

    @Test
    public void testHashcode(){

    }
}
