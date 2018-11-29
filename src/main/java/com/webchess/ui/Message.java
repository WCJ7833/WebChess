package com.webchess.ui;

/**
 * This class represents a text message from the server
 * @author <a href='bjs2864@rit.edu'>Brunon Sztuba</a>
 */
public class Message {
    /**
     * This is the message form the server
     */
    private String text;

    /**
     * This is an enum that is used to identify if the message contains info or if it is an error
     */
    public enum    TYPE{info, error}

    /**
     * This is the TYPE of this message
     */
    private TYPE    type;

    /**
     * This is the constructor for a Message object
     * @param mes is the message for the Message
     * @param kind is the TYPE for the message
     * #return a Message object with the appropriate data members
     */
    public Message(String mes, TYPE kind){
        text=mes;
        type=kind;
    }

    /**
     * This is a getter for text
     * @return text
     */
    public String getText(){
        return text;
    }

    /**
     * This is a getter for type
     * @return type
     */
    public TYPE getType(){
        return type;
    }

    /**
     * This method overrides the default hashCode method for Message type objects
     * @return a hashcode for this Message
     */
    public int hashCode(){
        return text.hashCode();
    }

    /**
     * This method overrides the default equals method for Message type objects
     * @param other is the object to which this Message is being compared
     * @return true iff both texts and types are the same, else, false
     */
    public boolean equals(Object other){
        if(other instanceof Message) {
            other = (Message) other;
            return (((Message) other).getText().equals(text) && ((Message) other).getType().equals(type));
        }
        else{
            return false;
        }
    }
}
