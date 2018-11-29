package com.webchess.model;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This is a JUnit test class for the Position class in the model tier
 * @author Brunon Sztuba bjs2864@rit.edu
 */
@Tag("Model-tier")
public class PositionTest {

    /**
     * this test ensures that the position gets the correct row
     */
    @Test
    public void constructorRow(){
        Position po=new Position(3,5);
        assertTrue(po.getRow()==3,"Does not have right row.");
    }

    /**
     * this test ensures that the constructor gets the correct cell
     */
    @Test
    public void constructorCell(){
        Position po=new Position(3,5);
        assertTrue(po.getCell()==5,"Does not have right cell.");
    }

    /**
     * this test ensures that the position's reverse method is correct.
     */
    @Test
    public void reverseTest(){
        Position po=new Position(3,5);
        assertTrue(po.reverse().getCell()==2&&po.reverse().getRow()==4,"Reverse is wrong.");
    }
}
