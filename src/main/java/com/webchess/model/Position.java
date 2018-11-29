package com.webchess.model;

/**
 * This class represents a position within the BoardView, and is used by the UI package
 * to tell the model where to move things
 * @author Sean Dunn smd6336@rit.edu
 * @author Brunon Sztuba bjs2864@rit.edu
 */
public class Position{

    /**
     * This is the row of the position
     */
    private int row;

    /**
     * This si the cell of the position
     */
    private int cell;

    /**
     * This is the constructor for a Position, and sets up the data members
     * @param row is the row for the position
     * @param cell is the cell (column) for the position
     */
    public Position(int row, int cell){
        this.row = row;
        this.cell = cell;
    }

    /**
     * This is a getter for row
     * @return row
     */
    public int getRow(){
        return row;
    }

    /**
     * This is a getter for cell
     * @return cell
     */
    public int getCell(){
        return cell;
    }

    /**
     * This method is used to get the corresponding Position for the opponent
     * @return
     */
    public Position reverse(){
        return new Position(7-row,7-cell);
    }

    /**
     * This method overrides the toString method for Position objects
     * @return a string representation of the Position
     */
    @Override
    public String toString() {
        return "R:" + this.getRow() +  " C:" + this.getCell();
    }
}
