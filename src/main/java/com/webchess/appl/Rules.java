package com.webchess.appl;

import com.webchess.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a case of pure fabrication; rather than distribute rule checking to several other classes, all move validation
 * is moved to this class, not present in the domain model.
 * @author Brunon Sztuba bjs2864@rit.edu
 */
public class Rules {


    /**
     * This is the BoardView for which the rules are used
     */
    private BoardView board;

    /**
     * This is the board's Row array
     */
    private Row[] rows;

    /**
     * This is a list of pieces that can jump
     */
    private List<Piece> jumpers;//

    /**
     * This is a piece that must jump during the next move.
     */
    private Piece forcedJumper;

    /**
     * This is the constructor for a Rules object
     * @param board is the boardview this object will be used for
     */
    public Rules(BoardView board) {
        this.board = board;
        rows = board.getRows();
        jumpers = new ArrayList<>();
        setUpJumpers();
    }

    public Piece getForcedJumper(){
        return forcedJumper;
    }


    /**
     * This method is used to determine if a Move attempt is valid
     * @param attempt is the move the player ///is attempting to perform
     * @return true if the ove is valid, else, false
     */
    public ArrayList<Object> isValid(Move attempt) {
        setUpJumpers();
        if(forcedJumper!=null&&Math.abs((attempt.getStart().getRow()-attempt.getEnd().getRow()))==1){
            ArrayList<Object>returns=new ArrayList();
            returns.add(false);
            returns.add("Forced jump piece from last jump cannot make a standard move.");
            return returns;
        }
        if (rows[attempt.getEnd().getRow()].getSpace(attempt.getEnd().getCell()).isValid()) {
            Piece mover = rows[attempt.getStart().getRow()].getSpace(attempt.getStart().getCell()).getPiece();
            if(forcedJumper != null) { //forcedJumper is the last piece to move this turn, if any.
                if (forcedJumper != mover) {
                    ArrayList<Object>returns=new ArrayList();
                    returns.add(false);
                    returns.add("Must keep using the same piece for multi jumps.");
                    return returns;
                }
            }
            Space into = rows[attempt.getEnd().getRow()].getSpace(attempt.getEnd().getCell());
            if (jumpers.contains(mover) || jumpers.isEmpty()) {
                return finalValid(mover,into);
            } else {
                ArrayList<Object>returns=new ArrayList();
                returns.add(false);
                returns.add("Must make a jump if possible.");
                return returns;
            }
        } else {
            ArrayList<Object>returns=new ArrayList();
            returns.add(false);
            returns.add("Pieces cannot be moved to occupied or"+
                    " white squares.");
            return returns;
        }
    }

    /**
     * This method is used to determine if the selected piece is allowed to move into the space
     * @param mover is the piece trying to be moved
     * @param into is the space into which the user is trying to move the piece
     * @return true if it is allowed, else, false
     */
    public ArrayList<Object> finalValid(Piece mover, Space into) {
        if(mover==null||board.getPlayer().alreadyMademoveAttempt()){
            ArrayList<Object> returns=new ArrayList<>();
            returns.add(false);
            returns.add("You can't keep going bucko.");
            return returns;
        }
        if(mover.getType().equals(Piece.TYPE.KING)) {
            int dist1 = Math.abs(mover.getCol() - into.getCellIdx());
            int dist2 = Math.abs(mover.getRow() - into.getRow());
            if (dist1 == 1 && dist2 == 1) {
                boolean allow=rows[into.getRow()].getSpace(into.getCellIdx()).isValid();
                if(allow){
                    ArrayList<Object> returns=new ArrayList<>();
                    returns.add(allow);
                    returns.add("");
                    return returns;
                }
                else{
                    ArrayList<Object> returns=new ArrayList<>();
                    returns.add(allow);
                    returns.add("Cannot move into an occupied or white square.");
                    return returns;
                }
            }
        }
        if(jumpers.contains(mover)) {
            int targetRow = (int) ((mover.getRow() + into.getRow()) / 2);
            int targetCol = (int) ((mover.getCol() + into.getCellIdx()) / 2);
            Space loc = rows[targetRow].getSpace(targetCol);
            if (!loc.isValid()) {
                Piece target = rows[targetRow].getSpace(targetCol).getPiece();
                boolean allow=goodTarget(mover,target);
                if(allow){
                    ArrayList<Object> objects = new ArrayList<>();
                    objects.add(allow);
                    objects.add("");
                    return objects;
                }
                else{
                    ArrayList<Object> objects = new ArrayList<>();
                    objects.add(allow);
                    objects.add("That was not a valid jump.");
                    return objects;
                }
            } else {
                ArrayList<Object> objects = new ArrayList<>();
                objects.add(false);
                objects.add("Cannot jump into an occupied square.");
                return objects;
            }
        }
        if(mover.getType().equals(Piece.TYPE.SINGLE)) {
            int dist1 = Math.abs(mover.getCol() - into.getCellIdx());
            int dist2 = mover.getRow() - into.getRow();
            if (dist1 == 1 && dist2 == 1) {
                boolean allow=rows[into.getRow()].getSpace(into.getCellIdx()).isValid();
                if(allow){
                    ArrayList<Object> objects = new ArrayList<>();
                    objects.add(allow);
                    objects.add("");
                    return objects;
                }
                else{
                    ArrayList<Object> objects = new ArrayList<>();
                    objects.add(allow);
                    objects.add("Cannot move into an occupied or white square.");
                    return objects;
                }
            }
            else{
                ArrayList<Object> objects = new ArrayList<>();
                objects.add(false);
                objects.add("Single piece can only move into "+
                        "squares diagonal, in front, and adjacent.");
                return objects;

            }
        }
        else {
            if (mover.getType().equals(Piece.TYPE.KING)) {
                ArrayList<Object> objects = new ArrayList<>();
                objects.add(false);
                objects.add("King piece can only move into "+
                        "squares diagonal, in front, and adjacent.");
                return objects;
            }
            else{
                ArrayList<Object> objects = new ArrayList<>();
                objects.add(false);
                objects.add("Single piece can only move into "+
                        "squares diagonal, in front, and adjacent.");
                return objects;
            }
        }
    }

    /**
     * This method is used to set up the jumpers data member
     * @return none
     */
    public void setUpJumpers() {
        jumpers = new ArrayList<>();
        ArrayList<Piece> boiz = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int z = 0; z < 8; z++) {
                Piece curr = rows[z].getSpace(i).getPiece();
                if (curr != null && curr.getOwner().equals(board.getPlayer())) {
                    boiz.add(curr);
                }
            }
        }
        for (Piece p : boiz) {
            if (canJump(p)) {
                jumpers.add(p);
            }
        }
    }

    /**
     * This method finds a piece that must make the next move, if any.
     * @return none
     */
    public void setForceJump(Piece jumper) {
        forcedJumper=jumper;
    }

    /**
     * This method sets forcedJumper to null
     */
    public void clearForce(){
        forcedJumper=null;
    }

    /**
     * This method is used to determine if a piece can make a jump move
     * @param p is a piece
     * @return true if it can jump a piece, else, false
     */
    public boolean canJump(Piece p) {
        if(p==null){
            return false;
        }
        if(p.getRow()==7&&p.getType().equals(Piece.TYPE.SINGLE)&&p.getOwner().equals(this.board.getOpponent())){
            return false;
        }
        if(p.getType().equals(null)){
            return false;
        }
        if(!p.sameBoard(board)){
            return false;
        }
        if (p.getType().equals(Piece.TYPE.SINGLE)) {
            if(p.getRow()==0){
                return false;
            }
            else if (p.getCol() == 0) {
                Space s1 = rows[p.getRow() - 1].getSpace(p.getCol() + 1);
                if (s1.isValid()) {
                    return false;
                }
                Piece t = s1.getPiece();
                return goodTarget(p, t);
            } else if (p.getCol() == 7) {
                Space s1 = rows[p.getRow() - 1].getSpace(p.getCol() - 1);
                if (s1.isValid()) {
                    return false;
                }
                Piece t = s1.getPiece();
                return goodTarget(p, t);
            }
            else if(p.isReverseJump()){

                Space s1 = rows[p.getRow() + 1].getSpace(p.getCol() - 1);
                Space s2 = rows[p.getRow() + 1].getSpace(p.getCol() + 1);

                if (s1.isValid() && s2.isValid()) {
                    return false;
                }
                boolean check1 = false;
                boolean check2 = false;
                if (!s1.isValid()) {
                    Piece t1 = s1.getPiece();
                    check1 = goodTarget(p, t1);
                }
                if (!s2.isValid()) {
                    Piece t2 = s2.getPiece();
                    check2 = goodTarget(p, t2);
                }
                return (check1 || check2);
            }
            else {
                Space s1 = rows[p.getRow() - 1].getSpace(p.getCol() - 1);
                Space s2 = rows[p.getRow() - 1].getSpace(p.getCol() + 1);
                if (s1.isValid() && s2.isValid()) {
                    return false;
                }
                boolean check1 = false;
                boolean check2 = false;
                if (!s1.isValid()) {
                    Piece t1 = s1.getPiece();
                    check1 = goodTarget(p, t1);
                }
                if (!s2.isValid()) {
                    Piece t2 = s2.getPiece();
                    check2 = goodTarget(p, t2);
                }
                return (check1 || check2);
            }
        } else {
            if (p.getCol() == 0) {
                if (p.getRow() == 0) {
                    Space s1 = rows[p.getRow() + 1].getSpace(p.getCol() + 1);
                    if (s1.isValid()) {
                        return false;
                    }
                    Piece t = s1.getPiece();
                    return goodTarget(p, t);
                } else if (p.getRow() == 7) {
                    Space s1 = rows[p.getRow() - 1].getSpace(p.getCol() + 1);
                    if (s1.isValid()) {
                        return false;
                    }
                    Piece t = s1.getPiece();
                    return goodTarget(p, t);
                } else {
                    Space s1 = rows[p.getRow() + 1].getSpace(p.getCol() + 1);
                    Space s2 = rows[p.getRow() - 1].getSpace(p.getCol() + 1);
                    if (s1.isValid() && s2.isValid()) {
                        return false;
                    }
                    boolean check1 = false;
                    boolean check2 = false;
                    if (!s1.isValid()) {
                        Piece t1 = s1.getPiece();
                        check1 = goodTarget(p, t1);
                    }
                    if (!s2.isValid()) {
                        Piece t2 = s2.getPiece();
                        check2 = goodTarget(p, t2);
                    }
                    return (check1 || check2);
                }
            } else if (p.getCol() == 7) {
                if (p.getRow() == 0) {
                    Space s1 = rows[p.getRow() + 1].getSpace(p.getCol() - 1);
                    if (s1.isValid()) {
                        return false;
                    }
                    Piece t = s1.getPiece();
                    return goodTarget(p, t);
                } else if (p.getRow() == 7) {
                    Space s1 = rows[p.getRow() - 1].getSpace(p.getCol() - 1);
                    if (s1.isValid()) {
                        return false;
                    }
                    Piece t = s1.getPiece();
                    return goodTarget(p, t);
                } else {
                    Space s1 = rows[p.getRow() + 1].getSpace(p.getCol() - 1);
                    Space s2 = rows[p.getRow() - 1].getSpace(p.getCol() - 1);
                    if (s1.isValid() && s2.isValid()) {
                        return false;
                    }
                    boolean check1 = false;
                    boolean check2 = false;
                    if (!s1.isValid()) {
                        Piece t1 = s1.getPiece();
                        check1 = goodTarget(p, t1);
                    }
                    if (!s2.isValid()) {
                        Piece t2 = s2.getPiece();
                        check2 = goodTarget(p, t2);
                    }
                    return (check1 || check2);
                }
            }else if(p.getRow() == 0){
                if (p.getCol() == 0){
                    Space s1 = rows[p.getRow() + 1].getSpace(p.getCol() + 1);
                    if (s1.isValid()) {
                        return false;
                    }
                    boolean check1 = false;
                    if (!s1.isValid()) {
                        Piece t1 = s1.getPiece();
                        check1 = goodTarget(p, t1);
                    }
                    return check1;
                }
                else if (p.getCol() == 7){
                    Space s1 = rows[p.getRow() + 1].getSpace(p.getCol() - 1);
                    if (s1.isValid()) {
                        return false;
                    }
                    boolean check1 = false;
                    if (!s1.isValid()) {
                        Piece t1 = s1.getPiece();
                        check1 = goodTarget(p, t1);
                    }
                    return check1;
                }
                else {
                    Space s1 = rows[p.getRow() + 1].getSpace(p.getCol() - 1);
                    Space s2 = rows[p.getRow() + 1].getSpace(p.getCol() + 1);
                    if (s1.isValid() && s2.isValid()) {
                        return false;
                    }
                    boolean check1 = false;
                    boolean check2 = false;
                    if (!s1.isValid()) {
                        Piece t1 = s1.getPiece();
                        check1 = goodTarget(p, t1);
                    }
                    if (!s2.isValid()) {
                        Piece t2 = s2.getPiece();
                        check2 = goodTarget(p, t2);
                    }
                    return check1 || check2;
                }
            }
            else if(p.getRow() == 7){
                if (p.getCol() == 0){
                    Space s1 = rows[p.getRow() - 1].getSpace(p.getCol() + 1);
                    if (s1.isValid()) {
                        return false;
                    }
                    boolean check1 = false;
                    if (!s1.isValid()) {
                        Piece t1 = s1.getPiece();
                        check1 = goodTarget(p, t1);
                    }
                    return check1;
                }
                else if (p.getCol() == 7){
                    Space s1 = rows[p.getRow() - 1].getSpace(p.getCol() - 1);
                    if (s1.isValid()) {
                        return false;
                    }
                    boolean check1 = false;
                    if (!s1.isValid()) {
                        Piece t1 = s1.getPiece();
                        check1 = goodTarget(p, t1);
                    }
                    return check1;
                }
                else {
                    Space s1 = rows[p.getRow() - 1].getSpace(p.getCol() - 1);
                    Space s2 = rows[p.getRow() - 1].getSpace(p.getCol() + 1);
                    if (s1.isValid() && s2.isValid()) {
                        return false;
                    }
                    boolean check1 = false;
                    boolean check2 = false;
                    if (!s1.isValid()) {
                        Piece t1 = s1.getPiece();
                        check1 = goodTarget(p, t1);
                    }
                    if (!s2.isValid()) {
                        Piece t2 = s2.getPiece();
                        check2 = goodTarget(p, t2);
                    }
                    return check1 || check2;
                }
            }
            else {
                Space s1 = rows[p.getRow() + 1].getSpace(p.getCol() - 1);
                Space s2 = rows[p.getRow() + 1].getSpace(p.getCol() + 1);
                Space s3 = rows[p.getRow() - 1].getSpace(p.getCol() - 1);
                Space s4 = rows[p.getRow() - 1].getSpace(p.getCol() + 1);
                if (s1.isValid() && s2.isValid() && s3.isValid() && s4.isValid()) {
                    return false;
                }
                boolean check1 = false;
                boolean check2 = false;
                boolean check3 = false;
                boolean check4 = false;
                if (!s1.isValid()) {
                    Piece t1 = s1.getPiece();
                    check1 = goodTarget(p, t1);
                }
                if (!s2.isValid()) {
                    Piece t2 = s2.getPiece();
                    check2 = goodTarget(p, t2);
                }
                if (!s3.isValid()) {
                    Piece t3 = s3.getPiece();
                    check3 = goodTarget(p, t3);
                }
                if (!s4.isValid()) {
                    Piece t4 = s4.getPiece();
                    check4 = goodTarget(p, t4);
                }
                return (check1 || check2 || check3 || check4);
            }
        }
    }

    /**
     * This method is used to determine whether or not a specific piece is able to be jumped over
     * @param p is the piece that is trying to jump
     * @param t is the piece trying to jump over
     * @return true if p can jump over t, else, false
     */
    private boolean goodTarget(Piece p, Piece t) {
        if(t==null){
            return false;
        }
        else if(t.getRow()==7||t.getRow()==0||t.getCol()==0||t.getCol()==7){
            return false;
        }
        if(p.getOwner().equals(t.getOwner())){
            return false;
        }
        if (p.getType().equals(Piece.TYPE.SINGLE) && p.getRow() <= t.getRow()) {
            return false;
        } else if (p.getType().equals(Piece.TYPE.SINGLE)) {
            if (t.getCol() > p.getCol()) {
                Space landing = rows[t.getRow() - 1].getSpace(t.getCol() + 1);
                return landing.isValid();
            } else {
                Space landing = rows[t.getRow() - 1].getSpace(t.getCol() - 1);
                return landing.isValid();
            }
        } else {
            if (t.getRow() > p.getRow()) {
                if (t.getCol() > p.getCol()) {
                    Space landing = rows[t.getRow()+1].getSpace(t.getCol() + 1);
                    return landing.isValid();
                } else {
                    Space landing = rows[t.getRow()+1].getSpace(t.getCol() - 1);
                    return landing.isValid();
                }
            } else {
                if (t.getCol() > p.getCol()) {
                    Space landing = rows[t.getRow() - 1].getSpace(t.getCol() + 1);
                    return landing.isValid();
                } else {
                    Space landing = rows[t.getRow() - 1].getSpace(t.getCol() - 1);
                    return landing.isValid();
                }
            }
        }
    }

    /**
     * This method determines if a piece can move standard
     * @param p is a piece
     * @return true if the piece can jump or move standard, else, false
     */
    public boolean canGo(Piece p){
        boolean go=false;
        int row=p.getRow();
        int col=p.getCol();
        if(p.getType().equals(Piece.TYPE.KING)) {
            for (int r = -1; r < 2; r += 2) {
                for (int c = -1; c < 2; c += 2) {
                    if (row + r < 8 && row + r >= 0) {
                        if (col + c < 8 && col + c >= 0) {
                            if (rows[row + r].getSpace(col + c).isValid()) {
                                go = true;
                            }
                        }
                    }
                }
            }
        }
        else {
            for (int c = -1; c < 2; c += 2) {
                if (row -1 < 8 && row -1 >= 0) {
                    if (col + c < 8 && col + c >= 0) {
                        if (rows[row + -1].getSpace(col + c).isValid()) {
                            go = true;
                        }
                    }
                }
            }
        }
        return go;
    }

    /**
     * This method determines if any piece in a list of pieces can move or jump
     * @param p is a list of pieces
     * @return true if any of the pieces can move, else, false
     */
    public boolean canMove(List p){
        boolean check=false;
        for(Object i:p){
            Piece piece=(Piece)i;
            if(canJump(piece)){
                check=true;
                break;
            }
            if(canGo(piece)){
                check=true;
                break;
            }
        }
        return check;
    }
    /**
     * This method is used to determine if a player is stuck
     * If so, it sets lost to true
     * if not, lost is not changed
     */
    public void checkStuck(){
        if(!canMove(board.getPieces())){
            board.getPlayer().setLost(true);
        }
    }
}