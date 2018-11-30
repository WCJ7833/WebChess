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
     * This is the constructor for a Rules object
     * @param board is the boardview this object will be used for
     */
    public Rules(BoardView board) {
        this.board = board;
        rows = board.getRows();
    }

    /**
     * This method is used to determine if a Move attempt is valid
     * @param attempt is the move the player ///is attempting to perform
     * @return true if the ove is valid, else, false
     */
    public ArrayList<Object> isMoveValid(Move attempt) {
        Piece mover = rows[attempt.getStart().getRow()].getSpace(attempt.getStart().getCell()).getPiece();
        if (mover == null){
            ArrayList<Object>returnInfo=new ArrayList();
            returnInfo.add(false);
            returnInfo.add("Empty piece cannot be moved");
            return returnInfo;
        }
        else {
            Space into = rows[attempt.getEnd().getRow()].getSpace(attempt.getEnd().getCell());
            //Pawn can only take piece via diagonol move

            if (into.getPiece() == null || !into.getPiece().getOwner().equals(mover.getOwner())){
                return finalValid(mover,into);
            }
            else{
                ArrayList<Object>returnInfo=new ArrayList();
                returnInfo.add(false);
                returnInfo.add("You can't move into a space that has one of your piece");
                return returnInfo;
            }

        }
    }

    /**
     * This method is used to determine if the selected piece is allowed to move into the space
     * @param mover is the piece trying to be moved
     * @param into is the space into which the user is trying to move the piece
     * @return true if it is allowed, else, false
     */
    public ArrayList<Object> finalValid(Piece mover, Space into) {
        if(board.getPlayer().alreadyMademoveAttempt()){
            ArrayList<Object> returns=new ArrayList<>();
            returns.add(false);
            returns.add("You can't keep moving");
            return returns;
        }
        else if(mover.getType().equals(Piece.TYPE.PAWN)) {
            ArrayList<Object> returnMessage = regularPawnMove(mover,into);
            return returnMessage;
        }
        else if(mover.getType().equals(Piece.TYPE.KING)) {
            ArrayList<Object> returnMessage = regularKingMove(mover,into);
            return returnMessage;
        }
        else if(mover.getType().equals(Piece.TYPE.BISHOP)) {
            ArrayList<Object> returnMessage = regularPawnMove(mover,into);
            return returnMessage;
        }
        else if(mover.getType().equals(Piece.TYPE.ROOK)) {
            ArrayList<Object> returnMessage = regularPawnMove(mover,into);
            return returnMessage;
        }
        else if(mover.getType().equals(Piece.TYPE.QUEEN)) {
            ArrayList<Object> returnMessage = regularPawnMove(mover,into);
            return returnMessage;
        }
        else if(mover.getType().equals(Piece.TYPE.KNIGHT)) {
            ArrayList<Object> returnMessage = regularPawnMove(mover,into);
            return returnMessage;
        }

            //int dist1 = Math.abs(mover.getCol() - into.getCellIdx());
            //int dist2 = Math.abs(mover.getRow() - into.getRow());
            //if (dist1 == 1 && dist2 == 1) {
            //    boolean allow=rows[into.getRow()].getSpace(into.getCellIdx()).isValid();
            //    if(allow){
            //        ArrayList<Object> returns=new ArrayList<>();
            //        returns.add(false);
            //        returns.add("");
                    //return returns;
           //     }
           //     else{
           //         ArrayList<Object> returns=new ArrayList<>();
           //         returns.add(allow);
           //         returns.add("Cannot move into an occupied or white square.");
           //         return returns;
          //      }
           // }
        else {
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(false);
            objects.add("King piece can only move into "+
                    "squares diagonal, in front, and adjacent.");
            return objects;
        }
    }

    public ArrayList<Object> regularPawnMove(Piece mover, Space into){
        if (mover.isFirstMove()){
            int distanceTraveled = Math.abs(into.getRow() - mover.getRow());
            if (distanceTraveled == 2){
                int columnChange = Math.abs(into.getCellIdx() - mover.getCol());
                if (columnChange == 0){
                    if (into.isValid()) {
                        Space spaceBetween = board.getRows()[mover.getRow() - 1].getSpace(mover.getCol());
                        if (spaceBetween.isValid()) {
                            ArrayList<Object> returns = new ArrayList<>();
                            returns.add(true);
                            returns.add("");
                            return returns;
                        } else {
                            ArrayList<Object> returns = new ArrayList<>();
                            returns.add(false);
                            returns.add("There is a piece between your start and end spaces");
                            return returns;
                        }
                    }
                    else{
                        ArrayList<Object> returns = new ArrayList<>();
                        returns.add(false);
                        returns.add("You can't take a piece via forward movement");
                        return returns;
                    }
                }
                else {
                    ArrayList<Object> returns=new ArrayList<>();
                    returns.add(false);
                    returns.add("Your regular pawn move can't have a column change");
                    return returns;
                }
            }
            else if (distanceTraveled == 1){
                int columnChange = Math.abs(into.getCellIdx() - mover.getCol());
                if (columnChange == 0){
                    Space spaceBetween = board.getRows()[mover.getRow()-1].getSpace(mover.getCol());
                    if (spaceBetween.isValid()){
                        ArrayList<Object> returns=new ArrayList<>();
                        returns.add(true);
                        returns.add("");
                        return returns;
                    }
                    else{
                        ArrayList<Object> returns=new ArrayList<>();
                        returns.add(false);
                        returns.add("There is a piece in front of your pawn");
                        return returns;
                    }
                }
                else {
                    ArrayList<Object> returns=new ArrayList<>();
                    returns.add(false);
                    returns.add("Your regular pawn move can't have a column change");
                    return returns;
                }
            }
            else{
                ArrayList<Object> returns=new ArrayList<>();
                returns.add(false);
                returns.add("Your first pawn move can only go forward 1 or 2 spaces");
                return returns;
            }
        }
        else{
            int distanceTraveled = Math.abs(into.getRow() - mover.getRow());
            if (distanceTraveled == 1){
                int columnChange = Math.abs(into.getCellIdx() - mover.getCol());
                if (columnChange == 0){
                    Space spaceBetween = board.getRows()[mover.getRow()].getSpace(mover.getCol()+1);
                    if (spaceBetween.isValid()){
                        ArrayList<Object> returns=new ArrayList<>();
                        returns.add(true);
                        returns.add("");
                        return returns;
                    }
                    else{
                        ArrayList<Object> returns=new ArrayList<>();
                        returns.add(false);
                        returns.add("There is a piece in front of your pawn");
                        return returns;
                    }
                }
                else {
                    ArrayList<Object> returns=new ArrayList<>();
                    returns.add(false);
                    returns.add("Your regular pawn move can't have a column change");
                    return returns;
                }
            }
            else{
                ArrayList<Object> returns=new ArrayList<>();
                returns.add(false);
                returns.add("Your pawn can only go forward 1 space");
                return returns;
            }
        }
    }

    public ArrayList<Object> regularKingMove(Piece mover, Space into){
        //Castling not availiable yet
        if (!mover.isFirstMove()){
            int rowChange = Math.abs(into.getRow() - mover.getRow());
            int colChange = Math.abs(into.getCellIdx() - mover.getCol());
            if (rowChange == 1){
                if (colChange == 1 || colChange == 0){
                    ArrayList<Object> returns=new ArrayList<>();
                    returns.add(true);
                    returns.add("");
                    return returns;
                }
                else{
                    //king can only move 1 space in any direction
                    ArrayList<Object> returns=new ArrayList<>();
                    returns.add(false);
                    returns.add("A king can only move 1 space in any direction except castling");
                    return returns;
                }
            }
            else if (rowChange == 0){
                if (colChange == 1){
                    ArrayList<Object> returns=new ArrayList<>();
                    returns.add(true);
                    returns.add("");
                    return returns;
                }
                else{
                    ArrayList<Object> returns=new ArrayList<>();
                    returns.add(false);
                    returns.add("You didn't move the piece");
                    return returns;
                    //king need to move
                }
            }
            else{
                ArrayList<Object> returns=new ArrayList<>();
                returns.add(false);
                returns.add("A king can only move 1 space in any direction except castling");
                return returns;
            }
        }
        else{
            int rowChange = Math.abs(into.getRow() - mover.getRow());
            int colChange = Math.abs(into.getCellIdx() - mover.getCol());
            if (rowChange == 1){
                if (colChange == 1 || colChange == 0){
                    ArrayList<Object> returns=new ArrayList<>();
                    returns.add(true);
                    returns.add("");
                    return returns;
                }
                else{
                    //king can only move 1 space in any direction
                    ArrayList<Object> returns=new ArrayList<>();
                    returns.add(false);
                    returns.add("A king can only move 1 space in any direction except castling");
                    return returns;
                }
            }
            else if (rowChange == 0){
                if (colChange == 1){
                    ArrayList<Object> returns=new ArrayList<>();
                    returns.add(true);
                    returns.add("");
                    return returns;
                }
                else{
                    ArrayList<Object> returns=new ArrayList<>();
                    returns.add(false);
                    returns.add("You didn't move the piece");
                    return returns;
                    //king need to move
                }
            }
            else{
                ArrayList<Object> returns=new ArrayList<>();
                returns.add(false);
                returns.add("A king can only move 1 space in any direction except castling");
                return returns;
            }
        }
    }

    public ArrayList<Object> finalTake(Piece mover, Space into) {
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