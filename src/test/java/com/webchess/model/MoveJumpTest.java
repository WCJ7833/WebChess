package com.webchess.model;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
/**
 * This class is a JUnit test for MoveJump
 * Again, due to the nature of this class, it requires a full instance of
 * a BoardView
 * @author Brunon Sztuba bjs2864@rit.edu
 */
@Tag("Model-tier")
public class MoveJumpTest {

    /**
     * This is one of the players (red)
     */
    private Player p1;

    /**
     * this is the other player(black)
     */
    private Player p2;

    /**
     * This is the board for which the rules are used
     */
    private BoardView board;

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
        p1=mock(Player.class);
        p2=mock(Player.class);
        when(p1.getName()).thenReturn("Francis");
        when(p2.getName()).thenReturn("Jessica");
        board=new BoardView(p1,p2,p1);
        board.setForTest();
        p27=mock(Position.class);
        p45=mock(Position.class);
        when(p27.getCell()).thenReturn(7);
        when(p27.getRow()).thenReturn(2);
        when(p45.getCell()).thenReturn(5);
        when(p45.getRow()).thenReturn(4);
    }

    /**
     * This test ensures that execute works right in updating jumper data members
     */
    @Test
    public void testExecute(){
        MoveJump jump=new MoveJump(p45,p27,board);
        Piece jumper=board.getPiece(p45.getRow(),p45.getCell());
        jump.execute();
        assertTrue(jumper.getRow()==p27.getRow()&&jumper.getCol()==p27.getCell(),
                "jumper did not change positions!");
    }

    /**
     * This test ensures that execute works right in removing the jumpee
     */
    @Test
    public void testExecute2(){
        MoveJump jump=new MoveJump(p45,p27,board);
        Piece jumpee=board.getPiece(p45.getRow()-1,p45.getCell()+1);
        jump.execute();
        assertTrue(board.getPiece(jumpee.getRow(),jumpee.getCol())==null,
                "jumpee not removed");
    }

    /**
     * This test ensures that execute works right in removing the jumper from start
     */
    @Test
    public void testExecute3(){
        MoveJump jump=new MoveJump(p45,p27,board);
        jump.execute();
        assertTrue(board.getRows()[p45.getRow()].getSpace(p45.getRow()).getPiece()==null,
                "jumper not removed from start!");
    }

    /**
     * This test ensures that execute works right in moving the jumper
     */
    @Test
    public void testExecute4(){
        MoveJump jump=new MoveJump(p45,p27,board);
        Piece jumper=board.getPiece(p45.getRow(),p45.getCell());
        jump.execute();
        assertTrue(board.getPiece(p27.getRow(),p27.getCell()).equals(jumper),
                "end did not get piece!");
    }

    /**
     * This test ensures that allowExtraMove() doesn't return true when it should be false
     */
    @Test
    public void testExtra(){
        MoveJump jump=new MoveJump(p45,p27,board);
        jump.execute();
        assertFalse(jump.allowExtraMove(),"extra jump allowed when shouldn't be!");
    }

    /**
     * This test ensures that allowExtraMove() returns true when it should
     */
    @Test
    public void testExtra2(){
        MoveJump jump=new MoveJump(p45,p45,board);//same position to trick it
        assertTrue(jump.allowExtraMove(),"extra jump allowed when shouldn't be!");
    }

    /**
     * This test ensures that two identical MoveJumps are the same
     */
    @Test
    public void testEquals(){
        MoveJump j1=new MoveJump(p27,p45,board);
        MoveJump j2=new MoveJump(p27,p45,board);
        assertTrue(j1.equals(j2),"Identical boards not equal!");
    }

    /**
     * This test ensures that two nonidentical MoveJumps are not the same
     */
    @Test
    public void testEquals2(){
        MoveJump j1=new MoveJump(p27,p45,board);
        MoveJump j2=new MoveJump(p27,p27,board);
        assertFalse(j1.equals(j2),"Non-identical boards equal!");
    }

    /**
     * This test ensures that two nonidentical MoveJumps are not the same
     */
    @Test
    public void testEquals3(){
        MoveJump j1=new MoveJump(p45,p45,board);
        MoveJump j2=new MoveJump(p27,p45,board);
        assertFalse(j1.equals(j2),"Non-identical boards equal!");
    }

    /**
     * This test ensures that a MoveJump is not equal to something that's not a movejump
     */
    @Test
    public void testEquals4(){
        MoveJump j1=new MoveJump(p45,p45,board);
        assertFalse(j1.equals("A++ please"),"MoveJump equals a string...");
    }

    /**
     * This tests hashCode
     */
    @Test
    public void testHash(){
        MoveJump j1=new MoveJump(p45,p45,board);
        assertTrue(j1.hashCode()==-11,"wrong hash");
    }
}
