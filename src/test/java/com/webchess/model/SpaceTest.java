package com.webchess.model;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * This is a JUnit test class for the Space class in the model tier
 * @author Brunon Sztuba bjs2864@rit.edu
 */
@Tag("Model-tier")
public class SpaceTest {
    /**
     * This is a mock piece used for testing
     */
    private static Piece piece;

    /**
     * This is a mock player used for testing
     */
    private static Player player;

    /**
     * This is a mock piece used for testing
     */
    private static Piece piece2;

    /**
     * This is a mock player used for testing
     */
    private static Player player2;

    /**
     * This is a setup used for creating the mock piece
     */
    @BeforeAll
    public static void setup(){
        piece=mock(Piece.class);
        player=mock(Player.class);
        piece2=mock(Piece.class);
        player2=mock(Player.class);
        when(player.getName()).thenReturn("Bobby");
        when(player2.getName()).thenReturn("Bobby Jr.");
        when(piece.getCol()).thenReturn(5);
        when(piece.getRow()).thenReturn(5);
        when(piece2.getCol()).thenReturn(2);
        when(piece2.getRow()).thenReturn(6);
        when(piece.getOwner()).thenReturn(player);
        when(piece2.getOwner()).thenReturn(player2);
    }

    /**
     * This test ensures that an even/even space is white
     */
    @Test
    public void colorTest1(){
        Space space=new Space(4,4);
        assertTrue(space.getColor().equals(Space.SPACECOLOR.WHITE),"Even/even not white");
    }

    /**
     * This test ensures that an even/odd space is black
     */
    @Test
    public void colorTest2(){
        Space space=new Space(4,5);
        assertTrue(space.getColor().equals(Space.SPACECOLOR.BLACK),"Even/odd not black");
    }

    /**
     * This test ensures that an odd/even space is black
     */
    @Test
    public void colorTest3(){
        Space space=new Space(5,4);
        assertTrue(space.getColor().equals(Space.SPACECOLOR.BLACK),"odd/even not black");
    }

    /**
     * This test ensures that an odd/odd space is white
     */
    @Test
    public void colorTest4(){
        Space space=new Space(5,5);
        assertTrue(space.getColor().equals(Space.SPACECOLOR.WHITE),"odd/odd not white");
    }

    /**
     * This test ensures that getCell is correct
     */
    @Test
    public void getCellTest(){
        Space space=new Space(3,5);
        assertTrue(space.getCellIdx()==3,"getCell is wrong");
    }

    /**
     * This test ensures that getPiece() is correct for a cell with a piece
     */
    @Test
    public void getPieceTest(){
        Space space=new Space(4,3);
        space.setPiece(piece);
        assertTrue(space.getPiece().equals(piece),"getPiece is wrong");
    }

    /**
     * This test ensures that get getPiece() is correct for a cell without a piece
     */
    @Test
    public void getPieceTest2(){
        Space space=new Space(4,3);
        assertTrue(space.getPiece()==null,"getPiece is wrong");
    }

    /**
     * This test ensures that clear() correctly sets the piece on a space to null
     */
    @Test
    public void clearTest(){
        Space space=new Space(6,2);
        space.setPiece(piece2);
        space.clear();
        assertTrue(space.getPiece()==null,"clearing doesn't remove a piece");
    }

    /**
     * This test ensures that setPiece() correctly gives a space a piece
     */
    @Test
    public void setPieceTest(){
        Space space=new Space(3,1);
        space.setPiece(piece2);
        assertTrue(space.getPiece().equals(piece2),"setPiece does not work properly");
    }

    /**
     * This test ensures that getRow is correct
     */
    @Test
    public void getRowTest(){
        Space space=new Space(4,5);
        assertTrue(space.getRow()==5,"getRow is not correct");
    }

    /**
     * This test ensures that an empty white space is invalid
     */
    @Test
    public void isValidTest1(){
        Space space=new Space(1,1);
        assertFalse(space.isValid(),"white space is valid");
    }

    /**
     * This test ensures that an occupied white space is invalid
     */
    @Test
    public void isValidTest2(){
        Space space=new Space(0,4);
        space.setPiece(piece);
        assertFalse(space.isValid(),"white space with a piece is valid");
    }

    /**
     * This test ensures that an empty black space is valid
     */
    @Test
    public void isValidTest3(){
        Space space=new Space(2,3);
        assertTrue(space.isValid(),"empty black is invalid");
    }

    /**
     * This test ensures that an occupied black space is invalid
     */
    @Test
    public void isValidTest4(){
        Space space=new Space(5,6);
        space.setPiece(piece2);
        assertFalse(space.isValid(),"occupied black is valid");
    }

    /**
     * This test ensures that two non-identical spaces are not equal
     */
    @Test
    public void equalsTest1(){
        Space space=new Space(4,2);
        Space space2=new Space(5,2);
        assertTrue(!space.equals(space2),"");
    }

    /**
     * This test ensures that two non-identical spaces are not equal in other order
     */
    @Test
    public void equalsTest4(){
        Space space=new Space(2,3);
        Space space2=new Space(2,7);
        assertTrue(!space.equals(space2),"");
    }

    /**
     * This test ensures that two identical spaces are equal
     */
    @Test
    public void equalsTest2(){
        Space space=new Space(4,4);
        Space space2=new Space(4,4);
        space.setPiece(piece);
        space2.setPiece(piece);
        assertTrue(space.equals(space2),"identical spaces not equal");
    }

    /**
     * This test ensures that a space is not equal to objects that are not instances of Space
     */
    @Test
    public void equalsTest3(){
        Space space=new Space(4,7);
        assertTrue(!space.equals("airplane"),"space equals...a string?!??!?!");
    }

    /**
     * This test ensures that the hashCode method for Space works properly
     */
    @Test
    public void hashTest(){
        Space space=new Space(0,3);
        double a=92.3234;
        int b=(int)(Math.pow(a,(((Math.sqrt((double)(4)))))));
        assertTrue(space.hashCode()==b,"hashCode not behaving as expected");
    }
}
