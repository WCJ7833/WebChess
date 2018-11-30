package com.webchess.model;

import com.webchess.appl.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents the checkerboard. It has players, an array of rows, and a 2d array of spaces for speedy
 * access. It implements Iterable for Row objects.
 * @author bjs2864@rit.edu
 * @author Sean Dunn smd6336@rit.edu
 */
public class BoardView implements Iterable<Row>{

    /**
     * this is the rules for this game
     */
    private Rules rules;
    /**
     * This is an array of rows that make up the board
     */
    private Row[] rows;

    /**
     * This is the player for whom this board view is made
     */
    private Player player;

    /**
     * This is the opponent
     */
    private Player opponent;

    /**
     * This is the moves that the player made
     */
    private List<Move> Moves = new ArrayList<>();

    /**
     * This is the pieces removed from the board
     */
    private List<Piece> removedPieces = new ArrayList<>();

    private boolean forceJump;

    private boolean forceJumpPiece;

    /**
     * this is the currently active player
     */
    private Player current;
    /**
     * This is the constructor for a BoardView
     * @param red is red player
     * @param white is white player
     * @param current is player for whome board is made
     * @return a BoardView with complete data members
     */
    public BoardView(Player red, Player white, Player current) {
        boolean isRed = current.equals(red);
        if(current.equals(red)){
                player=red;
                opponent=white;
                player.setTurn();
                opponent.clearTurn();
        }
        else{
            player=white;
            opponent=red;
            player.clearTurn();
            opponent.setTurn();
        }
        rows = new Row[8];
        for (int i = 0; i < 8; i++) {
            rows[i] = new Row(i,isRed, player, this);
        }
        this.rules=new Rules(this);
        this.forceJump = false;
        this.current = current;
        current.setBoard(this);//redundant but needed for testing!
    }

    /**
     * This is a getter for rows
     */
    public Row[] getRows(){
        return rows;
    }

    /**
     * This is a getter for player
     * @return
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * This is a getter for opponent
     * @return
     */
    public Player getOpponent() {
        return opponent;
    }

    /**
     * This method overrides the default hashcode method for BoardView objects
     * @return a hashcode value for this
     */
    public int hashCode(){
        return opponent.hashCode()+player.hashCode();
    }

    /**
     * This method overrides the default equals method for boardviews
     * @param other if the object to which this is being compared
     * @return true iff the data members in other are the same as the data members in this
     *  else, false
     */
    public boolean equals(Object other){
        if(other instanceof BoardView) {
            other = (BoardView) other;
            return (((BoardView) other).getPlayer().equals(this.player)
                    && ((BoardView) other).getOpponent().equals(this.opponent));
        }
        else{
            return false;
        }
    }

    /**
     * This is an override of iterator, implemented because BoardView implements iterable.
     * @return an Iterator that gets Row objects from BoardView's rows data member
     */
    @Override
    public Iterator<Row> iterator() {
        class bIter implements Iterator<Row>{
            private int i=-1;
            private int max=8;
            public bIter bIter(){
                return this;
            }
            public boolean hasNext(){
                return i<(max-1);
            }

            public Row next(){
                i+=1;
                return(rows[i]);
            }
        }
        return new bIter();
    }

    /**
     * This method returns the number of pieces a player has
     * @param player is the player to get count of pieces
     * @return number of pieces the player has
     */
    public int getPieceCount( Player player){
        int count = 0;
        for ( int i = 0; i<8;i++){
            for (int j = 0; j < 8; j++){
                Space space = rows[i].getSpace(j);
                Piece next=space.getPiece();
                if(next!=null) {
                    if (next.getOwner().equals(player)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * This method determines if a move is valid
     * @param tryDo is a Move whose validity must be determined
     * @return true if the Move is allowed; otherwise, false
     */
    public boolean allowed(Move tryDo){
        return (Boolean)rules.isMoveValid(tryDo).get(0);
    }

    /**
     * this method is used to kill a piece that has been jumped over
     * @param jumped is the piece that gets jumped
     */
    public BoardView killPiece(Piece jumped){
        if(jumped==null){
            return this;
        }
        removedPieces.add(jumped);
        jumped.getOwner().getPieces().remove(jumped);
        rows[jumped.getRow()].getSpace(jumped.getCol()).clear();
        return this;
    }

    /**
     * This method is used to simulate the movement of a piece
     * @param piece is the piece that is moving
     * @param startRow is the row the piece starts in
     * @param startCol is the column the piece starts in
     * @param endRow is the row the piece ends in
     * @param endCol is the column the piece ends in
     */
    public void movePiece(Piece piece, int startRow, int startCol, int endRow, int endCol){
        if (piece.isFirstMove()){
            piece.setFirstMove(false);
        }
        killPiece(rows[endRow].getSpace(endCol).getPiece());
        piece.setRow(endRow);
        piece.setCol(endCol);
        this.getRows()[startRow].getSpace(startCol).clear();
        this.getRows()[endRow].getSpace(endCol).setPiece(piece);
    }

    /**
     * This message is used to implement a StandardMove action and give info the
     * server about whether or not the action worked
     * @param move is the move the player is making
     * @return true if there is a piece to move, else, false
     */
    public boolean implementMove(Move move){
        int startRow = move.getStart().getRow();
        int startCol = move.getStart().getCell();
        int endRow = move.getEnd().getRow();
        int endCol = move.getEnd().getCell();
        Piece piece = this.getRows()[startRow].getSpace(startCol).getPiece();
        if (piece == null){
            return false;
        }

        movePiece(piece,startRow,startCol,endRow,endCol);
        return true;
    }

    /**
     * This is a toggle for the turn
     */
    public void toggleisTurn(){
        player.toggleTurn();
        opponent.toggleTurn();

    }

    /**
     * This is a getter for the player's list of pieces
     * @return player.getPieces()
     */
    public List<Piece> getPieces(){
        return player.getPieces();
    }

    /**
     * This method is used to determine if any of the player's pieces can move
     * @param p is the List of the player's pieces
     * @return true if the player has a piece that can move, else, return false
     */
    public boolean canMove(List p){
        return rules.canMove(p);
    }

    /**
     * This is a getter for Moves
     * @return Moves
     */
    public List<Move> getMoves() {
        return Moves;
    }
    /**
     * This is a getter for lastMove
     * @return lastMove
     */
    public Move getLastMove() {
        return Moves.get(Moves.size()-1);
    }

    /**
     * This method is a getter for the Piece at a specific location
     * @param row is the row at which to get the piece
     * @param col is the column at which to get the piece
     * @return the Piece at rows[row].getSpace(col)
     */
    public Piece getPiece(int row, int col){
        return rows[row].getSpace(col).getPiece();
    }


    /**
     * This method is used to update the appearance of the board to match
     * the move made by the opponent
     * @param move is the opponent's move
     */
    public void takeMove(Move move){
        Moves.add(move);
        move.execute();
    }


    /**
     * This is a getter for rules
     * @return rules
     */
    public Rules getRules(){
        return rules;
    }

    /**
     * This overrides the default toString method of BoardView
     * @return a string represntation of the board
     */
    @Override
    public String toString() {
        String string = "";
        for (Row row: this.rows){
            string += row.toString() + "\n";
        }

        return string;
    }

    /**
     * This method tells whether or not a piece can make a standard move
     * @param p is the piece to check
     * @return true iff p can make a standard move, else, false
     */
    public boolean canGo(Piece p){
        return rules.canGo(p);
    }

    /**
     * This method tells whether or not a move is valid
     * @param move is the move to check validity of
     * @return true iff move is valid; else, false
     */
    public List<Object> isAttemptValid(Move move){
        return rules.isMoveValid(move);
    }

    /**
     * This method updates the pieces of a player
     */
    public void updatePieceList(){
        player.clearPieces();
        for (int i = 0; i < 8; i++) {
            Row row = rows[i];
            for (int j = 0; j<8;j++){
                Space space = row.getSpace(j);
                if (space.getPiece() != null){
                    player.addPiece(space.getPiece());
                }
            }
        }
    }

    /**
     * This method adds a piece to a player's pieces and places it on the board
     * @param piece is the piece to add
     */
    public void addPiece(Piece piece){
        int rowIndex = piece.getRow();
        int colIndex = piece.getCol();
        rows[rowIndex].getSpace(colIndex).setPiece(piece);
    }

    /**
     * This method gets the last piece that was removed
     * @return the last piece removed from the board
     */
    public Piece getLastRemovedPiece() {
        Piece removedPiece = removedPieces.get(removedPieces.size()-1);
        removedPieces.remove(removedPiece);
        return removedPiece;
    }

    /**
     * This method clears the player's mademoveattempt
     */
    public void clearMade(){
        player.setMademoveAttempt(false);
    }
}
