
package com.webchess.model;

/**
 * This class represents a single space on the checkers board. It has an index, color, piece, and row number
 * @author bjs2864@rit.edu
 * @author Sean Dunn smd6336@rit.edu
 */
public class Space {
    /**
     * This is the index of the space (ie, how far from the leftmost column it is)
     */
    private int cellIdx;

    /**
     * This is an enum identifying the color of a space
     */
    public enum SPACECOLOR{WHITE,BLACK}

    /**
     * This is the color of this space
     */
    private SPACECOLOR color;

    /**
     * This is the piece on this space
     */
    private Piece piece;

    /**
     * This is the row number that this space belongs to
     */
    private int row;

    /**
     * This is the constructor for Space. It sets up the space and its color.
     * @param index is the column the space is in
     * @param row is the row the space is in
     * @return Space with appropriate data members
     */
    public Space(int index, int row) {
        cellIdx=index;
        this.row=row;
        if(index%2==0){
            if(row%2==0){
                color=SPACECOLOR.WHITE;
            }
            else{
                color=SPACECOLOR.BLACK;
            }
        }
        else{
            if(row%2==0){
                color=SPACECOLOR.BLACK;
            }
            else{
                color=SPACECOLOR.WHITE;
            }
        }
    }

    /**
     * This is a getter for cellIdx
     * @return cellIdx
     */
    public int  getCellIdx(){
        return cellIdx;
    }

    /**
     * This is a getter for piece
     * @return piece
     */
    public Piece getPiece() {
        if(piece!=null){
            return piece;
        }
        else{
            return null;
        }
    }

    /**
     * This method clears the space (sets piece to null)
     */
    public void clear(){
        piece=null;
    }

    /**
     * This method sets the piece on the space.
     * @param piece is the piece to occupy the space
     */
    public void setPiece(Piece piece){
        this.piece=piece;
    }

    /**
     * This is a getter for the row of the space
     * @return row
     */
    public int getRow(){
        return row;
    }

    /**
     * This method determines if the space is capable of holding a piece.
     * @return true iff color=BLACK AND piece==null
     * else, false
     */
    public boolean isValid(){
        return piece == null;
        //return (color.equals(SPACECOLOR.BLACK)&&piece==null);
    }

    /**
     * This is an override of hashCode for Space objects
     * @return a hashcode for the object
     */
    public int hashCode(){
        double a=92.3234;
        return (int)(cellIdx+Math.pow(a,(((Math.sqrt((double)row+1))))));
    }

    /**
     * This method overrides the equals method for Space objects
     * @param other is an object to which the space is being compared to
     * @return
     */
    public boolean equals(Object other) {
        if (other instanceof Space) {
            Space oth = (Space) other;
            return (oth.getRow()==this.row&&oth.getCellIdx() == this.cellIdx);
        }
        else{
            return false;
        }
    }

    /**
     * This is a color getter used for testing purposes
     * @return color
     */
    public SPACECOLOR getColor() {
        return color;
    }
}