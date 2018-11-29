package com.webchess.model;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.mockito.Mockito.*;

/**
 * This is a JUnit test class for the BoardView
 * Because this class performs work on other classes, as well
 * as created them, the testing
 * necessitates full instances of players, rows, spaces, rules, and pieces.
 * Essentially, his class exists to edit others, so this test class
 * will check to make sure that the edits are correct.
 * @author Brunon Sztuba bjs2864@rit.edu
 */
@Tag("Model-tier")
public class BoardViewTest {

    /**
     * this is the first player in the game, who will be the red player
     */
    private Player p1=new Player("Tom");

    /**
     * this is the second player in the game, who will be the white player
     */
    private Player p2=new Player("Jessica");

    /**
     * this is the board being tested
     */
    private BoardView board;

    /**
     * this is the opponents board, used to test king making
     */
    private BoardView board2;

    /**
     * this method is a setup method, which resets the data members, as they get edited
     * from one test to the other
     */
    @BeforeEach
    public void setup(){
        board=new BoardView(p1,p2,p1);
        BoardView board2=new BoardView(p1,p2,p2);
    }

    /**
     * this test ensures that getPlayer() is returning the correct Player
     */
    @Test
    public void testGetPlayer(){
        assertTrue(board.getPlayer().equals(p1),"getPlayer is returning wrong thing");
    }

    /**
     * this test ensures that getOpponent() is returning the correct Player
     */
    @Test
    public void testGetOpponent(){
        assertTrue(board.getOpponent().equals(p2),"getOpponent is returning wrong thing");
    }

    /**
     * this test ensures that the hashCode method is behaving
     */
    @Test
    public void testHaschCode(){
        int exp=p1.getName().hashCode()+p2.getName().hashCode();
        assertTrue(board.hashCode()==exp,"hashCode is wrong");
    }

    /**
     * this test ensures that the equals method works when comparing one BoardView to an identical one
     */
    @Test
    public void testEquals(){
        assertTrue(board.equals(board),"board not equal to identical board!");
    }

    /**
     * this test ensures that the equals method successfully returns false when comparing the board
     * to objects not of the BoardView class
     */
    @Test
    public void testEquals2(){
        assertFalse(board.equals(5),"board equals...an int!?!??!");
    }

    /**
     * this test ensures that the equals method succeeds in differentiating between
     * unequal instances of BoardView
     */
    @Test
    public void testEquals3(){
        BoardView other=new BoardView(p2,p1,p2);
        assertTrue(board.equals(board),"board equal to different board!");
    }

    /**
     * this test ensures that the equals method succeeds in differentiating between
     * unequal instances of BoardView
     */
    @Test
    public void testEquals4(){
        BoardView other=new BoardView(p1,p1,p1);
        assertTrue(board.equals(board),"board equal to different board!");
    }

    /**
     * this test ensures that the iterator's hasNext method at least works at first
     */
    @Test
    public void testIterator(){
        Iterator it=board.iterator();
        assertTrue(it.hasNext()==true,"iterator suggests no more rows at creation");
    }

    /**
     * this method ensures that the next() method works at least the first time
     */
    @Test
    public void testIterator2(){
        Row r=board.getRows()[0];
        Iterator it=board.iterator();
        assertTrue(it.next().equals(r),"get next not getting next row");
    }

    /**
     * this test ensures that the iterator does, int fact, iterate over the rows
     */
    @Test
    public void testIterator3(){
        Iterator it=board.iterator();
        for(int i=0;i<8;i++){
            it.next();
        }
        assertTrue(!it.hasNext(),  "iterator did not stop at end");
    }

    /**
     * this test ensures that the player gets the proper number of pieces
     */
    @Test
    public void testGetPieceCountP1(){
        int num=board.getPieceCount(p1);
        assertTrue(num==12,"not creating right number of pieces for p1");
    }

    /**
     * This test ensures that the opponent gets the proper number of pieces
     */
    @Test
    public void testGetPieceCountP2(){
        int num=board.getPieceCount(p2);
        assertTrue(num==12,"not creating right number of pieces for p2");
    }

    /**
     * this test ensures that killing a piece correctly removes the piece from the list of the player's pieces
     */
    @Test
    public void testKillPiece(){
        Piece die=board.getPieces().get(0);
        Piece killer=board.getPieces().get(board.getPieces().size()-1);
        board.killPiece(die);
        assertTrue(!board.getPieces().contains(die),"killing did not remove piece from pieces");
    }

    /**
     * this test ensures that killing a piece correctly changes the size of the player's list of pieces
     */
    @Test
    public void testKillPiece2(){
        Piece die=board.getPieces().get(0);
        int row=die.getRow();
        int column=die.getCol();
        Piece killer=board.getPieces().get(board.getPieces().size()-1);
        board.killPiece(die);
        assertTrue(board.getRows()[row].getSpace(column).getPiece()==null,
                "killing did not remove piece from space");
    }

    /**
     * this test ensures that moving a piece results in the space to which it is moved getting its piece
     * set to the piece moved
     */
    @Test
    public void testMovePiece(){
        Piece p=board.getPieces().get(0);
        board.movePiece(p,p.getRow(),p.getCol(),4,4);
        assertTrue(board.getPiece(4,4).equals(p),"piece not placed where it is to go");
    }

    /**
     * this test ensures that moving a piece removes it from the space at which it started
     */
    @Test
    public void testMovePiece2(){
        Piece p=board.getPieces().get(0);
        int oldRow=p.getRow();
        int oldCol=p.getCol();
        board.movePiece(p,p.getRow(),p.getCol(),4,4);
        assertTrue(board.getPiece(oldRow,oldCol)==null,"piece not removed from start when moved");
    }

    /**
     * this test ensures that moving a piece updates that piece's column data member correctly
     */
    @Test
    public void testMovePiece3(){
        Piece p=board.getPieces().get(0);
        board.movePiece(p,p.getRow(),p.getCol(),4,4);
        assertTrue(p.getCol()==4,"piece's column not updated");
    }

    /**
     * this test ensures that moving a piece updates that piece's row data member correctly
     */
    @Test
    public void testMovePiece4(){
        Piece p=board.getPieces().get(0);
        board.movePiece(p,p.getRow(),p.getCol(),4,4);
        assertTrue(p.getRow()==4,"piece's row not updated");
    }

    /**
     * this test ensures that implementMove correctly updates the row/column data members of a piece that is moved
     */
    @Test
    public void testImplement1(){
        MoveStandard move=new MoveStandard(new Position(5,0), new Position(4,1),board);
        Piece moved=board.getPiece(5,0);
        board.implementMove(move);
        assertTrue(moved.getRow()==4&&moved.getCol()==1,"implementMove doesn't actually move");
    }

    /**
     * this test ensures that implementMove correctly returns a true value for a good move
     */
    @Test
    public void testImplementBool(){
        MoveStandard move=new MoveStandard(new Position(5,0), new Position(4,1),board);
        assertTrue(board.implementMove(move),"implementMove doesn't return true when good move");
    }

    /**
     * this test ensures that implementMove correctly returns a false value for a bad move
     */
    @Test
    public void testImplement2(){
        MoveStandard move=new MoveStandard(new Position(4,0), new Position(4,1),board);
        board.implementMove(move);
        assertFalse( board.implementMove(move),"implementMove doesn't return false when bad move");
    }

    /**
     * this test ensures that toggleIsTurn correctly toggles the turns of the players
     */
    @Test
    public void testToggleIsTurn(){
        boolean playturn=board.getPlayer().isTurn();
        boolean oppturn=board.getOpponent().isTurn();
        board.toggleisTurn();
        assertTrue(board.getPlayer().isTurn()==!playturn&&
                board.getOpponent().isTurn()==!oppturn);
    }

    /**
     * this test ensures that pieces starts out with the right number and that getPieces
     * correctly returns that value
     */
    @Test
    public void testGetPieces() {
        int size = board.getPieces().size();
        assertTrue(size == 24, "pieces is not the right size");
    }

    /**
     * this test ensures that a player can, as the active player at start of game, make a move
     */
    @Test
    public void testCanMove(){
        assertTrue(board.canMove(board.getPieces()),"player cannot play at start of game");
    }

    /**
     * this test ensures that setLastMove correctly updates the last move
     */
    @Test
    public void testSetLastMove(){
        MoveAttempt tryit=new MoveAttempt(new Position(2,3),new Position(3,4),board);
        board.getMoves().add(tryit);
        assertTrue(board.getLastMove().equals(tryit),"setting last move did not work right");
    }

    /**
     * this test ensures that getPiece correctly returns the piece at the specified location
     */
    @Test
    public void testGetPiece(){
        Piece r=board.getPlayer().getPieces().get(7);
        int row=r.getRow();
        int col=r.getCol();
        assertTrue(board.getPiece(row,col).equals(r),"getPiece() did not find right piece");
    }

    /**
     * this test ensures that a piece not in row 0 is not kinged when doKing is called
     */
    @Test
    public void testDoKing() {
        Piece piece = board.getPiece(7, 0);
        board.doKing(new Position(7,0), new Position(7,0));
        assertTrue(piece.getType().equals(Piece.TYPE.KING), "getKing is not changing both copies of piece to" +
                " a king");
    }

    /**
     * this test ensures that takeMove correctly updates lastMove
     */
    @Test
    public void testTakeMove(){
        MoveAttempt attempt=new MoveAttempt(new Position(2,3), new Position(4,3),board);
        board.takeMove(attempt);
        assertTrue(board.getLastMove().equals(attempt),"taking move did not update lastMove");
    }

    /**
     * this test ensures that takeMove correctly rotates the given move attempt and executes it
     */
    @Test
    public void testTakeMove2(){
        MoveAttempt attempt=new MoveAttempt(new Position(2,3), new Position(3,4),board);
        MoveAttempt receive= (MoveAttempt) attempt.reverse(attempt,board);
        board.takeMove(receive);
        assertTrue(board.getPiece(4,3)!=null,"taking move did not properly rotate it and execute it");
    }

    /**
     * this test ensures that setForTest sets stuff up as wanted
     */
    @Test
    public void testSetForTest(){
        board.setForTest();
        boolean ready=board.jumpy(board.getPiece(4,5));
        assertTrue(ready,"board not set up for piece to jump");
    }

    /**
     * This test ensures that the toString() method returns the desired string
     */
    @Test
    public void testToString(){
        assertTrue(board.toString().equals(" W W W W\n" +
                "W W W W \n" +
                " W W W W\n" +
                "        \n" +
                "        \n" +
                "R R R R \n" +
                " R R R R\n" +
                "R R R R \n"));
    }

    /**
     * this test ensures that updating the list of pieces works properly
     */
    @Test
    public void testUpdatePieceList(){
        board.movePiece(board.getPiece(5,0),5,0,4,1);
        board.updatePieceList();
        assertTrue(board.getPlayer().getPieces().contains(new Piece(
                Piece.COLOR.RED, Piece.TYPE.SINGLE,4,1,
                board.getPlayer(),board)),"Player's piece list" +
                " not updated properly!");
    }
}