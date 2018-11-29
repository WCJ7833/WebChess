package com.webchess.model;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.mockito.Mockito.*;

/**
 * This is the unit test class for the AI, and ensures that it moves correctly
 * @author Brunon Sztuba bjs2864@rit.edu
 */
@Tag("Model-tier")
public class AITest {

    /**
     * This is one of the players (red)
     */
    private Player p1;

    /**
     * this is the AI player(black)
     */
    private AI p2;

    /**
     * This is the board for human
     */
    private BoardView board;

    /**
     * This is the board for AI
     */
    private BoardView board2;

    /**
     * this is a position used for testing
     */
    private Position p45;

    /**
     * this is a position used for testing
     */
    private Position p27;

    /**
     * This method sets up the data members before each test
     */
    @BeforeEach
    public void setUp(){
        p1=new Player("brosef");
        p2=new AI("'Computer'");
        board=new BoardView(p1,p2,p1);
        board2=new BoardView(p1,p2,p2);
        p27=mock(Position.class);
        p45=mock(Position.class);
        when(p27.getCell()).thenReturn(7);
        when(p27.getRow()).thenReturn(2);
        when(p45.getCell()).thenReturn(5);
        when(p45.getRow()).thenReturn(4);
    }

    /**
     * This test ensures that the AI moves correctly on its first turn
     */
    @Test
    public void testMoveStart(){
        p2.setTurn();
        p2.setInGame(true);
        p1.clearTurn();
        p1.setInGame(true);
        Piece goer=p2.getBoard().getPiece(5,0);
        p2.go();
        assertTrue(p2.getBoard().getPiece(4,1).equals(goer));
    }

    /**
     * This test ensures that the AI correctly makes a jump if possible
     */
    @Test
    public void testJump(){
        board.setForTest();
        p2.setTurn();
        p2.setInGame(true);
        p1.clearTurn();
        p1.setInGame(true);
        p2.go();
        assertTrue(board2.getPiece(2,3).getOwner().equals(p2));
    }

    /**
     * This test ensures that the AI correctly makes multijumps if needed
     */
    @Test
    public void testMultiJump(){
        board2.setForTestMulti();
        p2.setTurn();
        p2.setInGame(true);
        p1.clearTurn();
        p1.setInGame(true);
        p2.go();
        assertTrue(board2.getPiece(0,5).getOwner().equals(p2));
    }

    /**
     * This test ensures that the AI can move its king properly
     */
    @Test
    public void testAIKing(){
        board.setAIKing();
        p2.setTurn();
        p2.setInGame(true);
        p1.clearTurn();
        p1.setInGame(true);
        p2.go();
        assertTrue(board.getPiece(6,1).getOwner().equals(p2)&&
                board.getPiece(6,1).getType().equals(Piece.TYPE.KING),
                "King did not move right");
    }
}
