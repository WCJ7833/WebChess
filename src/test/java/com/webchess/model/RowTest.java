package com.webchess.model;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This is a JUnit test class for the Row class in the model tier
 * @author Brunon Sztuba bjs2864@rit.edu
 */
@Tag("Model-tier")
public class RowTest {
  /**
     * this is a mock player used for testing
     */
    private static Player player;

    /**
     * this is a mock Player used for testing
     */
    private static Player player2;

    /**
     * this is a mock space used for testing
     */
    private static Space space;


    private BoardView test;
    private BoardView test2;
    /**
     * this method is used to set up the data members before each test
     */
    @BeforeEach
    public void SetUp(){
        player=mock(Player.class);
        player2=mock(Player.class);
        when(player.getColor()).thenReturn(Piece.COLOR.RED);
        when(player2.getColor()).thenReturn(Piece.COLOR.WHITE);
        when(player.getName()).thenReturn("me");
        when(player2.getName()).thenReturn("Nome");
        space=mock(Space.class);
        when(space.getCellIdx()).thenReturn(1);
        when(space.getRow()).thenReturn(5);
        test=new BoardView(player,player2,player);
        test2=new BoardView(player,player2,player2);
    }

    /**
     * this test is used to ensure that the row created have the correct index
     */
    @Test
    public void testIndex(){
        Row row=new Row(5,true,player,player2,test,test2);
        assertTrue(row.getIndex()==5,"Has wrong index");
    }

    /**
     * this test is used to ensure that the red player gets their pieces in the correct location
     */
    @Test
    public void testSpacesHomeRed(){
        Row row=new Row(7,true,player,player2,test,test2);
        assertTrue(row.getSpace(0).getPiece().getOwner().equals(player)
                &&row.getSpace(1).getPiece()==null,
                "Did not set up home row for player correctly");
    }

    /**
     * this test is used to ensure that the rows in the middle do not get pieces
     */
    @Test
    public void testSpaces(){
        Row row=new Row(4,true,player,player2,test,test2);
        assertTrue(row.getSpace(0).getPiece()==null&&
                row.getSpace(1).getPiece()==null,
                "middle of board has pieces");
    }

    /**
     * this test is used to check that the coloring pattern is correct
     */
    @Test
    public void testSpacesColor(){
        Row row=new Row(4,true,player,player2,test,test2);
        assertTrue(row.getSpace(1).getColor().equals(Space.SPACECOLOR.BLACK)&&
                        row.getSpace(2).getColor().equals(Space.SPACECOLOR.WHITE),
                "middle of board has pieces");
    }

    /**
     * this test is used to ensure that the white players gets their pieces in the correct spot
     */
    @Test
    public void testSpacesHomeWhite(){
        Row row=new Row(6,false,player2,player,test2,test);
        assertTrue(row.getSpace(1).getPiece().getOwner().equals(player2)
                &&row.getSpace(2).getPiece()==null,"white home not right");
    }

    /**
     * this test is used to ensure that the red player's board shows the opposing player's pieces
     * in the correct spot
     */
    @Test
    public void testSpacesOppRed(){
        Row row=new Row(1,true,player,player2,test,test2);
        assertTrue(row.getSpace(0).getPiece().getOwner().equals(player2)
                &&row.getSpace(1).getPiece()==null,"white's enemy not right");
    }

    /**
     * this test is used to ensure that the white player's board shows their opponent's pieces
     * in the correct spot
     */
    @Test
    public void testSpacesOppWhite(){
        Row row=new Row(2,false,player2,player,test2,test);
        assertTrue(row.getSpace(1).getPiece().getOwner().equals(player)
                        &&row.getSpace(2).getPiece()==null,
                "white's enemy not right");
    }

    /**
     * this method is used to test the getSpace method
     */
    @Test
    public void testGetSpace(){
        Row row=new Row(5,true,player2,player,test2,test);
        assertTrue(row.getSpace(1).equals(space),"getSpace not finding space at spot correctly");
    }

    /**
     * this test ensures that trying to access a space out of index won't create an outofindex error
     */
    @Test
    public void testGetSpace2(){
        Row row=new Row(5,true,player2,player,test2,test);
        assertTrue(row.getSpace(19)==null,"getSpace returning something when out of index?");
    }

    /**
     * this tests that the hashCode method functions properly
     */
    @Test
    public void testHash(){
        Row row=new Row(4,false,player2,player,test2,test);
        assertTrue(row.hashCode()==(int)(Math.sqrt(Math.abs(4+3372))*Math.cos(4+3)),"");
    }


    /**
     * this test checks that the getSpaces() method is correct, and also that it makes an 8 element array
     */
    @Test
    public void testGetSpaces(){
        Row row=new Row(2,false,player,player2,test,test2);
        assertTrue(row.getSpaces().length==8,"getSpaces is not working right");
    }

    /**
     * this test ensures that identical rows are equal
     */
    @Test
    public void testEquals(){
        Row row=new Row(6,true,player,player2,test,test2);
        Row row2=new Row(6,true,player,player2,test,test2);
        assertTrue(row.equals(row2),"row not equal to an identical row");
    }

    /**
     * this test ensures that two rows identical in all but index are different
     */
    @Test
    public void testEquals2(){
        Row row=new Row(6,true,player2,player,test2,test);
        Row row2=new Row(5,true,player2,player,test2,test);
        assertFalse(row.equals(row2),"row is equal to row with diff index");
    }

    /**
     * this test ensures that a row is not equal to an object that is not an instance of Row
     */
    @Test
    public void testEquals3(){
        Row row=new Row(6,true,player2,player,test2,test);
        assertFalse(row.equals("nope"),"row is equal to a string");
    }

    /**
     * this test ensures that, at the start, there are more spaces through which t iterate
     */
    @Test
    public void testIterator(){
        Row row=new Row(7,true,player,player2,test,test2);
        Iterator it=row.iterator();
        assertTrue(it.hasNext()==true,"iterator suggests no more spaces at creation");
    }

    /**
     * this test ensures that the iterator's next method gets the next space
     */
    @Test
    public void testIterator2(){
        Row row=new Row(0,true,player,player2,test,test2);
        assertTrue(row.iterator().next().equals(row.getSpace(0)),"get next not getting next space");
    }

    /**
     * this test ensures that the iterator actually goes through the spaces and, at the end, does not keep iterating
     */
    @Test
    public void testIterator3(){
        Row row=new Row(0,true,player,player2,test,test2);
        Iterator it=row.iterator();
        for(int i=0;i<8;i++){
            it.next();
        }
        assertTrue(!it.hasNext(),  "iterator did not stop at end");
    }

    /**
     * This test checks that the toString method returns the desired string for a row
     */
    @Test
    public void testToString(){
        Row row=new Row(7,true,player,player2,test,test2);
        System.out.println(row.toString());
        assertTrue(row.toString().equals("R R R R "),"String representation not as intended");
    }

    /**
     * This test checks that the reverse method reverse the spaces of the row
     */
    @Test
    public void testReverse(){
        Row row=new Row(6,true,player,player2,test,test2);
        row.reverse();
        assertTrue(row.toString().equals("R R R R "),"row did not reverse spaces correctly");
    }

    /**
     * This test ensures that setSpaces correctly sets spaces
     */
    @Test
    public void testSetSpaces(){
        Row row=new Row(6,true,player,player2,test,test2);
        row.setSpaces(null);
        assertTrue(row.getSpaces()==null,"spaces not updated!");
    }
}
