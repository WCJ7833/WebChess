package com.webchess.model;

import java.util.Iterator;

/**
 * This class represents a single row on the checkers board. It implements Iterable.
 * @author bjs2864@rit.edu
 * @author Sean Dunn smd6336@rit.edu
 */
public class Row implements Iterable<Space>{
    /**
     * index is the row's number
     */
    private int index;

    /**
     * spaces is the array of Space objects in the row
     */
    private Space[] spaces;

    /**
     * This is the creator fr a Row object. It sets up the row with spaces and, if needed, pieces
     * @param idx is the row number
     * @param isRed is the boolean that is true if this is a row where the player viewing it is player 1, else,
     *                  false
     * @return a Row object that is set up for the start of a game
     */
    public Row(int idx, boolean isRed, Player player,BoardView board){
        index=idx;
        spaces=new Space[8];
        if(isRed) {
            // Red or Black Player
            if (idx == 6) {
                //Pawn Creation
                for (int i = 0; i < 8; i++) {
                    Space emptySpace = new Space(i, idx);
                    if (emptySpace.isValid()) {
                        Piece newPiece = new Piece(Piece.COLOR.RED, Piece.TYPE.PAWN, idx, i, player, board);
                        player.addPiece(newPiece);
                        emptySpace.setPiece(newPiece);
                    }
                    spaces[i] = emptySpace;
                }
            }
            else if (idx == 7){
                for (int i = 0; i < 8; i++) {
                    Space emptySpace = new Space(i, idx);
                    //Rook Creation
                    if ( i == 0 || i == 7){
                        Piece newPiece = new Piece(Piece.COLOR.RED, Piece.TYPE.ROOK, idx, i, player, board);
                        player.addPiece(newPiece);
                        emptySpace.setPiece(newPiece);
                    }
                    //Knight Creation
                    else if (i == 1 || i == 6){
                        Piece newPiece = new Piece(Piece.COLOR.RED, Piece.TYPE.KNIGHT, idx, i, player, board);
                        player.addPiece(newPiece);
                        emptySpace.setPiece(newPiece);
                    }
                    //Bishop Creation
                    else if (i == 2 || i == 5){
                        Piece newPiece = new Piece(Piece.COLOR.RED, Piece.TYPE.BISHOP, idx, i, player, board);
                        player.addPiece(newPiece);
                        emptySpace.setPiece(newPiece);
                    }
                    else if (i == 3){
                        // Black (Red) Queen
                        Piece newPiece = new Piece(Piece.COLOR.RED, Piece.TYPE.QUEEN, idx, i, player, board);
                        player.addPiece(newPiece);
                        emptySpace.setPiece(newPiece);
                    }
                    else {
                        // Black (Red) King
                        Piece newPiece = new Piece(Piece.COLOR.RED, Piece.TYPE.KING, idx, i, player, board);
                        player.addPiece(newPiece);
                        emptySpace.setPiece(newPiece);
                    }
                    spaces[i] = emptySpace;
                }
            }
            // White Player Opponent
            else if (idx == 1) {
                //Pawn Creation
                for (int i = 0; i < 8; i++) {
                    Space emptySpace = new Space(i, idx);
                    if (emptySpace.isValid()) {
                        Piece newPiece = new Piece(Piece.COLOR.WHITE, Piece.TYPE.PAWN, idx, i, player, board);
                        player.addPiece(newPiece);
                        emptySpace.setPiece(newPiece);
                    }
                    spaces[i] = emptySpace;
                }
            }
            else if (idx == 0) {

                for (int i = 0; i < 8; i++) {
                    Space emptySpace = new Space(i, idx);
                    //Rook Creation
                    if ( i == 0 || i == 7){
                        Piece newPiece = new Piece(Piece.COLOR.WHITE, Piece.TYPE.ROOK, idx, i, player, board);
                        player.addPiece(newPiece);
                        emptySpace.setPiece(newPiece);
                    }
                    //Knight Creation
                    else if (i == 1 || i == 6){
                        Piece newPiece = new Piece(Piece.COLOR.WHITE, Piece.TYPE.KNIGHT, idx, i, player, board);
                        player.addPiece(newPiece);
                        emptySpace.setPiece(newPiece);
                    }
                    //Bishop Creation
                    else if (i == 2 || i == 5){
                        Piece newPiece = new Piece(Piece.COLOR.WHITE, Piece.TYPE.BISHOP, idx, i, player, board);
                        player.addPiece(newPiece);
                        emptySpace.setPiece(newPiece);
                    }
                    else if (i == 3){
                        // White King
                        Piece newPiece = new Piece(Piece.COLOR.WHITE, Piece.TYPE.KING, idx, i, player, board);
                        player.addPiece(newPiece);
                        emptySpace.setPiece(newPiece);
                    }
                    else {
                        // White Queen
                        Piece newPiece = new Piece(Piece.COLOR.WHITE, Piece.TYPE.QUEEN, idx, i, player, board);
                        player.addPiece(newPiece);
                        emptySpace.setPiece(newPiece);
                    }
                    spaces[i] = emptySpace;
                }
            } else {
                for (int i = 0; i < 8; i++) {
                    spaces[i] = new Space(i, idx);
                }
            }
        }
        else{
            // White Player
            if (idx == 6) {
                //Pawn Creation
                for (int i = 0; i < 8; i++) {
                    Space emptySpace = new Space(i, idx);
                    if (emptySpace.isValid()) {
                        Piece newPiece = new Piece(Piece.COLOR.WHITE, Piece.TYPE.PAWN, idx, i, player, board);
                        player.addPiece(newPiece);
                        emptySpace.setPiece(newPiece);
                    }
                    spaces[i] = emptySpace;
                }
            }
            else if (idx == 7){
                for (int i = 0; i < 8; i++) {
                    Space emptySpace = new Space(i, idx);
                    //Rook Creation
                    if ( i == 0 || i == 7){
                        Piece newPiece = new Piece(Piece.COLOR.WHITE, Piece.TYPE.ROOK, idx, i, player, board);
                        player.addPiece(newPiece);
                        emptySpace.setPiece(newPiece);
                    }
                    //Knight Creation
                    else if (i == 1 || i == 6){
                        Piece newPiece = new Piece(Piece.COLOR.WHITE, Piece.TYPE.KNIGHT, idx, i, player, board);
                        player.addPiece(newPiece);
                        emptySpace.setPiece(newPiece);
                    }
                    //Bishop Creation
                    else if (i == 2 || i == 5){
                        Piece newPiece = new Piece(Piece.COLOR.WHITE, Piece.TYPE.BISHOP, idx, i, player, board);
                        player.addPiece(newPiece);
                        emptySpace.setPiece(newPiece);
                    }
                    // White Queen
                    else if (i == 3){
                        Piece newPiece = new Piece(Piece.COLOR.WHITE, Piece.TYPE.QUEEN, idx, i, player, board);
                        player.addPiece(newPiece);
                        emptySpace.setPiece(newPiece);
                    }
                    // White King
                    else{
                        Piece newPiece = new Piece(Piece.COLOR.WHITE, Piece.TYPE.KING, idx, i, player, board);
                        player.addPiece(newPiece);
                        emptySpace.setPiece(newPiece);
                    }
                    spaces[i] = emptySpace;
                }
            }
            // Red  Opponent
            else if (idx == 1) {
                //Pawn Creation
                for (int i = 0; i < 8; i++) {
                    Space emptySpace = new Space(i, idx);
                    if (emptySpace.isValid()) {
                        Piece newPiece = new Piece(Piece.COLOR.RED, Piece.TYPE.PAWN, idx, i, player, board);
                        player.addPiece(newPiece);
                        emptySpace.setPiece(newPiece);
                    }
                    spaces[i] = emptySpace;
                }
            }
            else if (idx == 0) {
                for (int i = 0; i < 8; i++) {
                    Space emptySpace = new Space(i, idx);
                    //Rook Creation
                    if ( i == 0 || i == 7){
                        Piece newPiece = new Piece(Piece.COLOR.RED, Piece.TYPE.ROOK, idx, i, player, board);
                        player.addPiece(newPiece);
                        emptySpace.setPiece(newPiece);
                    }
                    //Knight Creation
                    else if (i == 1 || i == 6){
                        Piece newPiece = new Piece(Piece.COLOR.RED, Piece.TYPE.KNIGHT, idx, i, player, board);
                        player.addPiece(newPiece);
                        emptySpace.setPiece(newPiece);
                    }
                    //Bishop Creation
                    else if (i == 2 || i == 5){
                        Piece newPiece = new Piece(Piece.COLOR.RED, Piece.TYPE.BISHOP, idx, i, player, board);
                        player.addPiece(newPiece);
                        emptySpace.setPiece(newPiece);
                    }
                    else if (i == 3){
                        // RED King

                        Piece newPiece = new Piece(Piece.COLOR.RED, Piece.TYPE.KING, idx, i, player, board);
                        player.addPiece(newPiece);
                        emptySpace.setPiece(newPiece);

                    }
                    else {
                        // RED Queen
                        Piece newPiece = new Piece(Piece.COLOR.RED, Piece.TYPE.QUEEN, idx, i, player, board);
                        player.addPiece(newPiece);
                        emptySpace.setPiece(newPiece);
                    }
                    spaces[i] = emptySpace;
                }
            } else {
                for (int i = 0; i < 8; i++) {
                    spaces[i] = new Space(i, idx);
                }
            }
        }
    }

    /**
     * This is a getter for index
     * @return index
     */
    public int getIndex(){
        return index;
    }

    /**
     * This is a getter for the space at a certain location in spaces
     * @param i is the index of the desired space
     * @return spaces[i] if i is in range, else, null
     */
    public Space getSpace(int i){
        if(0<=i&&i<8) {
            return spaces[i];
        }
        else{
            return null;
        }
    }

    /**
     * hashCode is an override of the hashCode method for a Row object
     * @return a hashcode for the row object
     */
    public int hashCode(){
        return (int)(Math.sqrt(Math.abs(index+3372))*Math.cos(index+3));
    }

    /**
     * this is a getter for spaces
     * @return spaces
     */
    public Space[] getSpaces(){
        return spaces;
    }

    /**
     * This method overrides the equals method for Row objects
     * @param other is the object to which this is being compared
     * @return true iff other is this, else, false
     */
    public boolean equals(Object other){
        if(!(other instanceof Row)){
            return false;
        }
        return((((Row) other).getIndex()==this.index));
    }

    /**
     * This is an override of iterator, implemented because Row implements iterable.
     * @return an Iterator that gets Space objects from Row's spaces data member
     */
    @Override
    public Iterator<Space> iterator() {
        class rIter implements Iterator<Space>{
            private int i=-1;
            private int max=8;
            public rIter rIter(){
                return this;
            }
            public boolean hasNext(){
                return i<(max-1);
            }

            public Space next(){
                i+=1;
                return(spaces[i]);
            }
        }
        return new rIter();
    }

    /**
     * This method returns a string representation of the row
     * @return a string representation of the row
     */
    @Override
    public String toString() {
        String string = "";
        for (Space space: this.spaces){
            if (space.getPiece() == null){
                string += " ";
            }
            else if (space.getPiece().getColor() == Piece.COLOR.RED){
                string += "R";
            }
            else{
                string += "W";
            }
        }
        return string;
    }

    /**
     * This method sets the spaces of the row to the argument
     * @param spaces is the Space array to set the spaces to
     */
    public void setSpaces(Space[] spaces) {
        this.spaces = spaces;
    }

    /**
     * This method reverses the spaces of the row
     * @return this, with reversed spaces
     */
    public Row reverse() {
        Space[] newspaces=new Space[8];
        int count = 7;
        for (Space space: this.spaces){
            newspaces[count] = space;
            count--;
        }
        this.setSpaces(newspaces);
        return this;
    }
}
