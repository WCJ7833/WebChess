package com.webchess.model;

import static java.lang.Math.abs;

/**
 * This class is used to model an move attempted by the user (when they move a piece and click submit)
 * @extends Move
 * @author bjs264@rit.edu
 */
public class MoveAttempt extends Move {
    private  Boolean isReverse;
    /**
     * This is the constructor for a MoveAttempt
     * @param p1 is the starting position fo the piece that the user is trying to move
     * @param p2 is the ending position for the piece that the user is trying to move
     */
    public MoveAttempt(Position p1, Position p2, BoardView b){
        super(p1,p2);
        super.setBoard(b);
        isReverse = false;
    }
    /**
     * this is the implementation of the abstract method allowExtraMove() from the superclass Move
     * @return false
     */
    @Override
    public boolean allowExtraMove() {
        return false;
    }

    /**
     * This is the implementation of the abstract method execute() from the superclass Move
     * It determines if the attempt is allowed, and, if so, created  and executes the appropriate
     * move
     */
    @Override
    public void execute() {

        boolean allowed = super.getView().allowed(this);
        if (allowed || isReverse) {
            if(abs(super.getStart().getRow() - super.getEnd().getRow()) == 2 &&
                    abs(super.getStart().getRow() - super.getEnd().getRow()) == 2  && isReverse){
                super.getAt(super.getStart().getRow(), super.getStart().getCell()).setReverseJump(true);
            }
            Piece yaBoi = super.getAt(super.getStart().getRow(), super.getStart().getCell());
            if (super.jumper(yaBoi) || yaBoi.isReverseJump()) {
                MoveJump doThis = new MoveJump(super.getStart(), super.getEnd(),super.getView());
                doThis.execute();
            } else {
                MoveStandard doThis = new MoveStandard(super.getStart(), super.getEnd(),super.getView());
                doThis.execute();
            }
            if(isReverse){
                if(super.getAt(super.getEnd().getRow(), super.getEnd().getCell()) == null){
                    return;
                }
                super.getAt(super.getEnd().getRow(), super.getEnd().getCell()).setReverseJump(false);
            }
            super.king(super.getStart(),super.getEnd());
        }
    }

    /**
     * This method overrides the default equals method for MoveAttempts
     * @param obj is the object to which this is being compared
     * @return true iff obj equals this, else, false
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MoveAttempt){
            obj = (MoveAttempt)obj;
            return this.getStart().equals(((Move) obj).getStart()) && this.getEnd().equals(((Move) obj).getEnd());
        }
        else{
            return false;
        }
    }

    /**
     * This method is used to set isReverse to true
     */
    public void setReverse() {
        isReverse = true;
    }

    /**
     * This method overrides the default hashCode method for MoveAttempt objects
     * @return a hash value for this
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
