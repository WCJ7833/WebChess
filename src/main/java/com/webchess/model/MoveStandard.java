package com.webchess.model;

/**
 * This method is used to simulate a standard (ie, non-jump) move
 * within the model.
 * @extends Move
 * @author Brunon Sztuba bjs2864@rit.edu
 */
public class MoveStandard extends Move {

    /**
     * This is the constructor for a MoveStandard
     * @param start is the starting Position of the Piece
     * @param end is the ending Position of the Piece
     * @param board is the player's board
     */
    public MoveStandard(Position start, Position end, BoardView board) {
        super(start, end);
        super.setBoard(board);
    }

    /**
     * This method is the implementation of the abstract method execute() from the superclass Move(), and
     * is used to simulate the movement of a piece within the business logic
     * It removes the piece from its previous space, puts it in the next space, as well as updating
     * the piece's data members
     */
    @Override
    public void execute() {
        Piece boyo=super.getAt(super.getStart().getRow(),super.getStart().getCell());
        super.move(boyo);
        super.king(super.getStart(),super.getEnd());
    }

    /**
     * This method is the implementation of the abstract method allowExtraMove() from the superclass Move()
     * @return false, as standard moves never enable a second move
     */
    @Override
    public boolean allowExtraMove() {
        return false;
    }

    /**
     * This method overrides the default equals() method and determines if another object equals this MoveStandard
     * @param obj is the object this is being compared to
     * @return true iff obj is an identical MoveStandard, else, false
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MoveStandard){
            obj = (MoveStandard)obj;
            return this.getStart().equals(((Move) obj).getStart()) && this.getEnd().equals(((Move) obj).getEnd());
        }
        else{
            return false;
        }
    }

    /**
     * This method is the override for the default hashCode() method
     * @return a hash for this move
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
