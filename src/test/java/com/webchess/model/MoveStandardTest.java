package com.webchess.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This is a JUnit test class for MoveStandardTest
 * because of the nature of the class in test, a full instance of BoardView is needed
 * @author Brunon Sztuba bjs2864@rit.edu
 */
public class MoveStandardTest {

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
    private Position p50;

    /**
     * this is a position used for testing
     */
    private Position p41;

    /**
     * This method sets up the data members before each test
     */
    @BeforeEach
    public void setUp() {
        p1 = mock(Player.class);
        p2 = mock(Player.class);
        when(p1.getName()).thenReturn("Francis");
        when(p2.getName()).thenReturn("Jessica");
        board = new BoardView(p1, p2, p1);
        board.setForTest();
        p50 = mock(Position.class);
        p41 = mock(Position.class);
        when(p50.getCell()).thenReturn(0);
        when(p50.getRow()).thenReturn(5);
        when(p41.getCell()).thenReturn(1);
        when(p41.getRow()).thenReturn(4);
    }

    /**
     * This test ensures that execute works right in updating mover data members
     */
    @Test
    public void testExecute() {
        MoveStandard move = new MoveStandard(p50, p41, board);
        Piece mover = board.getPiece(p50.getRow(), p50.getCell());
        move.execute();
        assertTrue(mover.getRow() == p41.getRow() && mover.getCol() == p41.getCell(),
                "mover did not change positions!");
    }


    /**
     * This test ensures that execute works right in removing the mover from start
     */
    @Test
    public void testExecute2() {
        MoveStandard move = new MoveStandard(p50, p41, board);
        move.execute();
        assertTrue(board.getRows()[p50.getRow()].getSpace(p50.getRow()).getPiece() == null,
                "mover not removed from start!");
    }

    /**
     * This test ensures that execute works right in moving the mover
     */
    @Test
    public void testExecute3() {
        MoveStandard move = new MoveStandard(p50, p41, board);
        Piece mover = board.getPiece(p50.getRow(), p50.getCell());
        move.execute();
        assertTrue(board.getPiece(p41.getRow(), p41.getCell()).equals(mover),
                "end did not get piece!");
    }

    /**
     * This test ensures that allowExtraMove() doesn't return true when it should be false
     */
    @Test
    public void testExtra() {
        MoveStandard move = new MoveStandard(p50, p41, board);
        move.execute();
        assertFalse(move.allowExtraMove(), "extra move allowed when shouldn't be!");
    }

    /**
     * This test ensures that two identical MoveStandards are the same
     */
    @Test
    public void testEquals() {
        MoveStandard j1 = new MoveStandard(p50, p41, board);
        MoveStandard j2 = new MoveStandard(p50, p41, board);
        assertTrue(j1.equals(j2), "Identical boards not equal!");
    }

    /**
     * This test ensures that two nonidentical MoveStandards are not the same
     */
    @Test
    public void testEquals2() {
        MoveStandard j1 = new MoveStandard(p50, p41, board);
        MoveStandard j2 = new MoveStandard(p50, p50, board);
        assertFalse(j1.equals(j2), "Non-identical boards equal!");
    }

    /**
     * This test ensures that two nonidentical MoveStandards are not the same
     */
    @Test
    public void testEquals3() {
        MoveStandard j1 = new MoveStandard(p41, p41, board);
        MoveStandard j2 = new MoveStandard(p50, p41, board);
        assertFalse(j1.equals(j2), "Non-identical boards equal!");
    }

    /**
     * This test ensures that a MoveStandard is not equal to something that's not a MoveStandard
     */
    @Test
    public void testEquals4() {
        MoveStandard j1 = new MoveStandard(p41, p41, board);
        assertFalse(j1.equals("A++ please"), "MoveStandard equals a string...");//
    }

    /**
     * This tests hashCode
     */
    @Test
    public void testHash(){
        MoveStandard m1=new MoveStandard(p41,p50,board);
        assertTrue(m1.hashCode()==5,"wrong hash");
    }
}
