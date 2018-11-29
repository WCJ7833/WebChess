package com.webchess.model;

import java.util.ArrayList;

/**
 * This abstract class is used to represent a single Move within the game.
 * @author Brunon Sztuba bjs2864@rit.edu
 */
public abstract class Move{


    /**
     * This is the position at which the piece being moved started
     */
    private Position start;

    /**
     * This is the ending position for the piece
     */
    private Position end;

    /**
     * This is the BoardView within which the piece is used
     */
    private BoardView view;

    /**
     * This is the constructor for a move, which sets up the start and end data members
     * @param start is the start Position
     * @param end is the end Position
     */
    public Move(Position start, Position end){
        this.start = start;
        this.end = end;
    }

    /**
     * This method is a setter for view
     * @param board is the BoardView for this Move
     */
    public void setBoard(BoardView board){
        view=board;
    }

    /**
     * This method is a getter for the start
     * @return start
     */
    public Position getStart(){
        return start;
    }

    /**
     * This method is a getter for the end
     * @return end
     */
    public Position getEnd(){
        return end;
    }

    /**
     * This is a getter for view
     * @return view
     */
    public BoardView getView() {
        return view;
    }

    /**
     * This is an abstract method to be implemented by subclasses
     * @return true if the player can keep going (more jumps allowed)
     */
    public abstract boolean allowExtraMove();

    /**
     * This is an abstract method to be implemented by subclasses, which executes their logic
     */
    public abstract void execute();

    /**
     * This method is a getter for the Piece at the specified row and column
     * @param row is the row in which the piece resides
     * @param col is the column in which the piece resides
     * @return the piece that is there
     */
    public Piece getAt(int row, int col){
        return view.getPiece(row, col);
    }

    /**
     * This method returns whether or not a given piece can make a jump
     * @param z is a piece
     * @return true if z can jump, else, false
     */
    public boolean jumper(Piece z){
        return view.jumpy(z);
    }

    /**
     * This method is used to simulate the deletion of a piece by another via a jump
     * @param jumped is the piece that got jumped
     */
    public BoardView delete(Piece jumped){
        view = view.killPiece(jumped);

        return view;
    }

    /**
     * This method is used to simulate the changing of a piece's location
     * @param piece
     */
    public void move(Piece piece){
        view.movePiece(piece,start.getRow(),start.getCell(),end.getRow(),end.getCell());
    }

    /**
     * This method is the toString() method for a Move class
     * @return a string representation of the Move
     */
    @Override
    public String toString() {
        return "Start: (" + this.getStart().getRow() +", " + this.getStart().getCell()+ ") End: (" + this.getEnd().getRow()
                +", " + this.getEnd().getCell() +")";
    }


    /**
     * This method is used to king the piece that moved into p, if needed
     * @param start is the position from which the piece moved
     * @param end is the position into which the piece moved
     */
    public void king(Position start,Position end){
        if(end.getRow()==0){
            view.doKing(start,end);
        }
    }

    /**
     * This method sets up and returns the reversed version of the move
     * @param moveAttempt is the move you want the reversed version of
     * @param board is the board the reversed move is for
     * @return this move reversed and set up for the specified baord
     */
    public MoveAttempt reverse(Move moveAttempt,BoardView board){

        Position end = moveAttempt.getEnd().reverse();
        Position start=moveAttempt.getStart().reverse();

        MoveAttempt reverse = new MoveAttempt(start,end,board);
        reverse.setReverse();
        return reverse;
    }

    /**
     * This method is used to set the specified piece as the forced jumper
     * @param p is the piece that is forced to jump
     */
    public void setupForce(Piece p){
        view.setForce(p);
    }

    /**
     * This method is used to clear the player's move attempt
     */
    public void clearAttempt(){
        view.clearMade();
    }

    /**
     * This method overrides the default equals method for Move objects
     * @param obj is the object to which this is being compared
     * @return true iff obj is equal to this, else, false
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Move){
            obj = (Move)obj;
            return this.start.equals(((Move) obj).start) && this.end.equals(((Move) obj).end);
        }
        else{
            return false;
        }
    }

    /**
     * This method overrides the default hashCode method for Move objects
     * @return a hash value for this move
     */
    @Override
    public int hashCode() {
        return start.getRow()+start.getCell()-end.getCell()*end.getRow();
    }
}
