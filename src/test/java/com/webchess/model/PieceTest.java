package com.webchess.model;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

/**
 * This is a test for the Piece class in the model tier
 * @author bjs2864@rit.edu Brunon Sztuba
 */
@Tag("Model-tier")
public class PieceTest {
    /**
     * This is a mock of the first player
     */
    private Player p1;
    /**
     * This is a mock of the second player
     */
    private Player p2;

    private BoardView test;
    private BoardView test2;
    /**
     * This setup occurs before each test, and sets up the mocks for players
     */
    @BeforeEach
    public void setup(){
        p1=mock(Player.class);
        p2=mock(Player.class);
        String b1="Bob";
        int b1h=b1.hashCode();
        String b2="Jack";
        int b2h=b2.hashCode();
        when(p1.getName()).thenReturn(b1);
        when(p1.getColor()).thenReturn(Piece.COLOR.RED);
        when(p2.getName()).thenReturn(b2);
        when(p2.getColor()).thenReturn(Piece.COLOR.WHITE);
        test=new BoardView(p1,p2,p1);
        test2=new BoardView(p1,p2,p2);
    }

    /**
     * This test makes sure that the constructor assigns white color correctly
     */
    @Test
    public void constructionWhite() {
        Piece piece = new Piece(Piece.COLOR.WHITE, Piece.TYPE.SINGLE, 7, 7, p2,test2);
        assertTrue(piece.getColor().equals(Piece.COLOR.WHITE), "Piece got wrong color.");
    }

    /**
     * This test makes sure that the constructor assigns red color correctly
     */
    @Test
    public void constructionRed() {
        Piece piece = new Piece(Piece.COLOR.RED, Piece.TYPE.SINGLE, 7, 7, p1,test);
        assertTrue(piece.getColor().equals(Piece.COLOR.RED), "Piece has no color.");
    }

    /**
     * This test makes sure that the constructor assigns column correctly
     */
    @Test
    public void constructionCol() {
        Piece piece = new Piece(Piece.COLOR.RED, Piece.TYPE.SINGLE, 7, 7, p1,test);
        assertTrue(piece.getCol() == 7, "Column 7 is wrong!");
    }

    /**
     * This test makes sure that the constructor assigns row correctly
     */
    @Test
    public void constructionRow() {
        Piece piece = new Piece(Piece.COLOR.WHITE, Piece.TYPE.SINGLE, 0, 5, p1,test);
        assertTrue(piece.getRow() == 0, "Row 0 is wrong!");
    }

    /**
     * This is another test for construction and column
     */
    @Test
    public void constructionCol2() {
        Piece piece = new Piece(Piece.COLOR.RED, Piece.TYPE.SINGLE, 5, 0, p1,test);
        assertTrue(piece.getCol() == 0, "Column 0 is wrong!");
    }

    /**
     * This is another test for construction and row
     */
    @Test
    public void constructionRow2() {
        Piece piece = new Piece(Piece.COLOR.WHITE, Piece.TYPE.SINGLE, 7, 5, p1,test);
        assertTrue(piece.getRow() == 7, "Row 7 is wrong!");
    }

    /**
     * This is a test that checks that the constructor gives the piece the correct owner
     */
    @Test
     public void constructorOwnerP1() {
        Piece piece = new Piece(Piece.COLOR.WHITE, Piece.TYPE.SINGLE, 0, 5, p1,test);
        assertTrue(piece.getOwner().equals(p1), "P1 has wrong owner!");
    }

    /**
     * This is another test for construction and owner assignment
     */
    @Test
    public void constructorOwnerP2()   {
        Piece piece = new Piece(Piece.COLOR.WHITE, Piece.TYPE.SINGLE, 0, 5, p2,test2);
        assertTrue(piece.getOwner().equals(p2), "P2 wrong owner!");
    }

    /**
     * This test checks that creating a king assigns it the correct type
     */
    @Test
    public void typeKing(){
        Piece piece = new Piece(Piece.COLOR.WHITE, Piece.TYPE.KING, 0, 5, p1,test);
        assertTrue(piece.getType().equals(Piece.TYPE.KING), "King type not created.");
    }

    /**
     * This test checks that creating a ssingle assigns it the correct type
     */
    @Test
    public void typeSingle(){
        Piece piece = new Piece(Piece.COLOR.WHITE, Piece.TYPE.SINGLE,3,0, p2,test2);
        assertTrue(piece.getType().equals(Piece.TYPE.SINGLE), "Single type not created.");
    }

    /**
     * This test checks that piece.equals(piece) returns true
     */
    @Test
    public void testEqualsPositive(){
        Piece piece = new Piece(Piece.COLOR.WHITE, Piece.TYPE.SINGLE,3,0, p2,test2);
        Piece piece2 = new Piece(Piece.COLOR.WHITE, Piece.TYPE.SINGLE,3,0, p2,test2);
        assertTrue(piece.equals(piece2), "Same piece not equal to itself!");
    }

    /**
     * This test checks that 2 different pieces are not the same
     */
    @Test
    public void testEqualsNegative(){
        Piece piece = new Piece(Piece.COLOR.WHITE, Piece.TYPE.SINGLE,3,0, p2,test2);
        Piece piece2 = new Piece(Piece.COLOR.WHITE, Piece.TYPE.SINGLE,3,0, p1,test);
        assertFalse(piece.equals(piece2), "Piece in same spot with different owner seen as same!");
    }

    /**
     * This test checks that 2 different pieces are not the same
     */
    @Test
    public void testEqualsNegative2(){
        Piece piece = new Piece(Piece.COLOR.WHITE, Piece.TYPE.SINGLE,1,0, p2,test2);
        Piece piece2 = new Piece(Piece.COLOR.WHITE, Piece.TYPE.SINGLE,3,0, p2,test2);
        assertFalse(piece.equals(piece2), "Piece in different row but same column and owner seen as same!");
    }

    /**
     * This test checks that 2 different pieces are not the same
     */
    @Test
    public void testEqualsNegative3(){
        Piece piece = new Piece(Piece.COLOR.WHITE, Piece.TYPE.SINGLE,5,0, p2,test2);
        Piece piece2 = new Piece(Piece.COLOR.WHITE, Piece.TYPE.SINGLE,5,4, p2,test2);
        assertFalse(piece.equals(piece2), "Piece in different column but same row and owner seen as same!");
    }

    /**
     * This test checks that the hashCode() method works as expected
     */
    @Test
    public void testHashCode(){
        Piece piece = new Piece(Piece.COLOR.WHITE, Piece.TYPE.SINGLE,3,4, p2,test2);
        assertTrue(piece.hashCode()==19, "hashCode() is not behaving right!");
    }

    /**
     * This test checks that the setCol() method sets the column correctly
     */
    @Test
    public void testSetCol(){
        Piece piece = new Piece(Piece.COLOR.WHITE, Piece.TYPE.SINGLE,3,0, p2,test2);
        piece.setCol(6);
        assertTrue(piece.getCol()==6,"Setting column did not change it to correct value.");
    }

    /**
     * This test checks that the setRow() method sets the row correctly
     */
    @Test
    public void testSetRow(){
        Piece piece = new Piece(Piece.COLOR.WHITE, Piece.TYPE.SINGLE,3,0, p2,test2);
        piece.setRow(5);
        assertTrue(piece.getRow()==5,"Setting row did not change it to correct value.");
    }

    /**
     * This is a test for makeKing() if the piece should not become a king
     */
    @Test
    public void testNoKing(){
        Piece piece = new Piece(Piece.COLOR.WHITE, Piece.TYPE.SINGLE,0,3, p2,test2);
        piece.makeKing();
        assertTrue(piece.getType().equals(Piece.TYPE.KING), "Piece not made king!");
    }

    /**
     * This is a test of equals when the other thing is not a Piece
     */
    @Test
    public void neverEquals(){
        Piece piece = new Piece(Piece.COLOR.WHITE, Piece.TYPE.SINGLE,3,0, p2,test2);
        int pi=3;
        assertFalse(piece.equals(pi),"Equals makes it equal to an in int...uh-oh!");
    }
}
