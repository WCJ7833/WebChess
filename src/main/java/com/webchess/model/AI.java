package com.webchess.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This is an AI player
 * @author Brunon Sztuba bjs2864@rit.edu
 * @author Will Johnson wcj7833@rit.edu
 */
public class AI extends Player {

    /**
     * This is the move made when go is called
     */

    private Move made;

    /**
     * This is the constructor for an AI
     *
     * @param name is there to match the Player constructor
     */
    public AI(String name) {
        super(name);
        super.setColor(Piece.COLOR.WHITE);
    }


    /**
     * This is a getter for made
     */
    public Move getMade(){
        return made;
    }

    /**
     * This method is used to make the AI go
     */
    public void go(){
        if(this.lost()){
            return;
        }
        made=null;
        super.setJumps();
        boolean jumped=this.goJump();
        if(!jumped){
            this.goStandard();
            return;
        }
        Piece multi=super.getForced();
        if(multi!=null) {
            doJump(multi);
        }
    }

    /**
     * This method is used to have the AI make a jump
     */
    public boolean goJump() {
        Piece pie = null;
        List<Piece>pieces=super.getPieces();
        for (Piece p : pieces) {
            if (super.canJump(p)&&p.sameBoard(this.getBoard())) {//error?
                pie = p;
                break;
            }
        }
        if(pie==null) {
            return false;
        }
        while(super.isTurn()&&!super.isLost()){
            doJump(pie);
            if(this.getBoard().jumpy(pie)){
                super.getBoard().getRules().setForceJump(pie);
            }
            else{
                super.getBoard().getRules().clearForce();
            }
            if(this.lost()){
                super.setLost(true);
            }
            if(super.getBoard().getRules().getForcedJumper()==null){
                super.getBoard().toggleisTurn();
            }
        }
        return true;
    }

    /**
     * This method is used to have the AI make a jump
     */
    public void goStandard() {
        Piece pie = null;
        List<Piece>pieces=this.getBoard().getPieces();
        for (Piece p : pieces) {
            if (p.getOwner().getName().equals("'Computer'")&&this.canGo(p)&&p.sameBoard(this.getBoard())) {
                pie = p;
                break;
            }
        }
        if(pie==null) {
            return;//maybe throw exception?
        }
        if(pie.getType().equals(Piece.TYPE.SINGLE)){
            goSingleStandard(pie);
        }
        else{
            goKingStandard(pie);
        }
    }

    /**
     * This method is used to move the specified single type piece in a standard move
     * @param pie is the single type piece to move
     */
    public void goSingleStandard(Piece pie){
        Position piece=new Position(pie.getRow(), pie.getCol());
        ArrayList<Position> p=new ArrayList<>();
        ArrayList<Position> p2=new ArrayList<>();
        Position t1=new Position(piece.getRow()-1,piece.getCell()+1);
        Position t2=new Position(piece.getRow()-1,piece.getCell()-1);
        p.add(t1);
        p.add(t2);
        for(Position pos:p){
            if(pos.getRow()<8&&pos.getRow()>-1&&
                    pos.getCell()<8&&pos.getCell()>-1){
                p2.add(pos);
            }
        }
        for(Position pos:p2){
            MoveStandard move=new MoveStandard(piece,pos,this.getBoard());
            if(this.isValid(move)){
                made=move;
                move.execute();
                MoveAttempt rev=move.reverse(move,super.getBoard().getOpponent().getBoard());
                rev.setReverse();
                super.getBoard().getOpponent().getBoard().takeMove(rev);
                return;
            }
        }
        return;
    }

    /**
     * This method is used to move the specified king type piece in a standard move
     * @param pie is the king type piece to move
     */
    public void goKingStandard(Piece pie){
        Position piece=new Position(pie.getRow(), pie.getCol());
        ArrayList<Position> p=new ArrayList<>();
        ArrayList<Position> p2=new ArrayList<>();
        Position t1=new Position(piece.getRow()-1,piece.getCell()+1);
        Position t2=new Position(piece.getRow()-1,piece.getCell()-1);
        Position t3=new Position(piece.getRow()+1,piece.getCell()+1);
        Position t4=new Position(piece.getRow()+1,piece.getCell()-1);
        p.add(t1);
        p.add(t2);
        p.add(t3);
        p.add(t4);
        for(Position pos:p){
            if(pos.getRow()<8&&pos.getRow()>-1&&
                    pos.getCell()<8&&pos.getCell()>-1){
                p2.add(pos);
            }
        }
        for(Position pos:p2){
            MoveStandard move=new MoveStandard(piece,pos,this.getBoard());
            if(this.isValid(move)){
                move.execute();
                made=move;
                MoveAttempt rev=move.reverse(move,super.getBoard().getOpponent().getBoard());
                rev.setReverse();
                super.getBoard().getOpponent().getBoard().takeMove(rev);
                return;
            }
        }
        return;//maybe exception?
    }

    /**
     * This method is used to make the piece do a jump
     * @param pie is the piece to make perform a jump
     */
    public void doJump(Piece pie){
        Position piece=new Position(pie.getRow(), pie.getCol());
        ArrayList<Position> p=new ArrayList<>();
        ArrayList<Position> p2=new ArrayList<>();
        ArrayList<MoveJump> jumps=new ArrayList<>();
        Position t1=new Position(piece.getRow()-2,piece.getCell()+2);
        Position t2=new Position(piece.getRow()-2,piece.getCell()-2);
        Position t3=new Position(piece.getRow()+2,piece.getCell()+2);
        Position t4=new Position(piece.getRow()+2,piece.getCell()-2);
        p.add(t1);
        p.add(t2);
        p.add(t3);
        p.add(t4);
        for(Position pos:p){
            if(pos.getRow()<8&&pos.getRow()>-1&&
                    pos.getCell()<8&&pos.getCell()>-1){
                p2.add(pos);
            }
        }
        for(Position pos:p2){
            MoveJump jump=new MoveJump(piece,pos,this.getBoard());
            if(this.isValid(jump)){
                made=jump;
                jump.execute();
                MoveAttempt rev=jump.reverse(jump,super.getBoard().getOpponent().getBoard());
                rev.setReverse();
                super.getBoard().getOpponent().getBoard().takeMove(rev);
                return;
            }
        }
        return;
    }
}

