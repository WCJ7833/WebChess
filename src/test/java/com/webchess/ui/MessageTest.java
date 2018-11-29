package com.webchess.ui;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This is a JUnit test class for the ui/Message class
 * @author bjs2864@rit.edu Brunon Sztuba
 */
@Tag("UI-tier")
public class MessageTest {

    /**
     * This test ensures that a message gets the correct text
     */
    @Test
    public void constructorText(){
        Message mes=new Message("Hello there human bean", Message.TYPE.error);
        assertTrue(mes.getText().equals("Hello there human bean"), "String is not right!");
    }

    /**
     * This test ensures that a message made with an empty string gets an empty string
     */
    @Test
    public void constructorTextEmpty(){
        Message mes=new Message("", Message.TYPE.error);
        assertTrue(mes.getText().equals(""), "Empty string is not right!");
    }

    /**
     * This test ensures that error type messages are, in fact, error type
     */
    @Test
    public void constructorError(){
        Message mes=new Message("z-brah", Message.TYPE.error);
        assertTrue(mes.getType().equals(Message.TYPE.error),"Error type wrong!");
    }

    /**
     * This test ensures that info type messages are, in fact, info type
     */
    @Test
    public void constructorInfo(){
        Message mes=new Message("potato", Message.TYPE.info);
        assertTrue(mes.getType().equals(Message.TYPE.info),"Info type wrong!");
    }

    /**
     * this test ensures that the hashCode() method is returning the correct value
     */
    @Test
    public void hashTest(){
        Message mes=new Message("blueberries", Message.TYPE.error);
        assertTrue(mes.hashCode()=="blueberries".hashCode(),"Hashcode gives wrong number!");
    }

    /**
     * this test checks that Messages do not equal other classes
     */
    @Test
    public void equals(){
        Message mes=new Message("Hola", Message.TYPE.error);
        assertTrue(!mes.equals(5),"Message equals...5?");
    }

    /**
     * this test checks to ensure that a message equals itself
     */
    @Test
    public void equals1(){
        Message mes=new Message("bruh", Message.TYPE.error);
        Message mes2=new Message("bruh", Message.TYPE.error);
        assertTrue(mes.equals(mes2),"Doesn't equal identical message!");
    }

    /**
     * This test checks to ensure that two messages with the same text but different type are not equal
     */
    @Test
    public void equals2(){
        Message mes=new Message("bruh", Message.TYPE.error);
        Message mes2=new Message("bruh", Message.TYPE.info);
        assertFalse(mes.equals(mes2),"Messages with same text but different types are seen as same!");
    }

    /**
     * This test checks to ensure that two messages with the same type but not the same message are not equal
     */
    @Test
    public void equals3(){
        Message mes=new Message("hurb", Message.TYPE.info);
        Message mes2=new Message("bruh", Message.TYPE.info);
        assertFalse(mes.equals(mes2),"Messages of same type with different text seen as same!");
    }
}