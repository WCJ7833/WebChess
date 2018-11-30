package com.webchess.model;

/**
 * This class is used to model a jump move by the user, in which the user uses one of their pieces
 * to jump over an opponent's
 *
 * @author bjs2864@rit.edu
 */
public class MoveJump extends Move{

    /**
     * This is the constructor for a MoveJump
     * @param start is the starting Position of the Piece
     * @param end is the ending Position of the Piece
     * @param board is the player's board
     */
    public MoveJump(Position start, Position end, BoardView board) {
        super(start, end);
        super.setBoard(board);
    }

    /**
     * This method is used to execute a jump action in the model.
     * It moves the piece used to jump and deletes the piece that gets jumped
     */
    @Override
    public void execute() {
        Piece boyo=super.getAt(super.getStart().getRow(),super.getStart().getCell());
        boyo.setCol(super.getEnd().getCell());
        boyo.setRow(super.getEnd().getRow());
        int col=(int)((super.getStart().getCell()+super.getEnd().getCell())/2);
        int row=(int)((super.getStart().getRow()+super.getEnd().getRow())/2);
        Piece other=super.getAt(row,col);
        super.move(boyo);
        BoardView boardView = super.delete(other);
        super.setBoard(boardView);
        //if(!allowExtraMove()) {
        //    boyo.setForceJump(false);
        //    super.setupForce(null);
        //} else {
        //    boyo.setForceJump(true);
        //    super.setupForce(boyo);
        //    super.clearAttempt();
        //}
    }


    /***
     * This method overrides the default equals method for MoveJump objects
     * @param obj is the object to which this is being compared
     * @return true iff obj equals this, else, false
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MoveJump){
            obj = (MoveJump)obj;
            return this.getStart().equals(((Move) obj).getStart()) && this.getEnd().equals(((Move) obj).getEnd());
        }
        else{
            return false;
        }
    }

    /**
     * This method overrides the default hashCode method for MoveJump objects
     * @return an int hash for this
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
