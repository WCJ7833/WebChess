package com.webchess.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a single player, with a username.
 * @author Brunon Sztuba bjs2864@rit.edu
 */
public class Player {
    /**
     * This is the board of the player
     */
    private BoardView board;

    /**
     * This is the username of the player
     */
    private String username;

    /**
     * This is the color of the piece
     */
    private Piece.COLOR color;

    /**
     * This determines if the player is in a game
     */
    private Boolean InGame;

    /**
     * This is the player's pieces
     */
    private List<Piece> pieces;

    /**
     * This is whether or not it is this player's turn
     */
    private boolean turn;

    /**
     * This is whether or not the player lost due to stalemate
     */
    private boolean lost;

    /**
     * This is whether or not the player attempted a move
     */
    private boolean mademoveAttempt;

    /**
     * This is a default constructor so that Player may be extended
     */
    public Player(){

    }
    /**
     * This is the constructor for a player. It takes and assigns the username
     * @param name is the username of the player
     * @return a player with the specified username
     */
    public Player(String name) {
        username = name;
        InGame = false;
        pieces=new ArrayList<>();
        lost=false;
        mademoveAttempt = false;
    }

    /**
     * This is a setter for InGame
     * @param inGame is true if the player is in game, and false if not
     */
    public synchronized void setInGame(Boolean inGame) {
        this.InGame = inGame;
    }

    /**
     * This is a method to add a piece to the player's pieces
     * @param piece is the piece to add
     */
    public void addPiece(Piece piece){
        pieces.add(piece);
    }

    /**
     * This is the getter for pieces
     */
    public List<Piece> getPieces(){
        return pieces;
    }

    /**
     * This method is used to wipe pieces to a new ArrayList
     */
    public void clearPieces() {
        this.pieces = new ArrayList<>();
    }

    /**
     * This is a getter for inGame
     * @return InGame
     */

    public synchronized Boolean getInGame(){
        return this.InGame;
    }

    /**
     * this is the setter for color
     * @param color is the color for hte player
     */
    public void setColor(Piece.COLOR color){
        this.color=color;
    }

    /**
     * this is a getter for color
     * @return color
     */
    public Piece.COLOR getColor(){
        return color;
    }

    /**
     * Getter for player's board
     * @return Player's board
     */
    public BoardView getBoard() {
        return board;
    }

    /**
     * Sets player's board and piece's board
     * @param board that will be set to player's board
     */
    public void setBoard(BoardView board) {
        this.board = board;
        for(Piece p:pieces){
            if(p.getBoard()==null) {
                p.setBoard(board.getOpponent().getBoard());
            }
        }
    }

    /**
     * This method overrides the default equals method for a player
     * @param other is the object to which this is being compared
     * @return true iff the other's username is the same as this username, else, false
    */

    public boolean equals(Object other){
        if(!(other instanceof Player)){
            return false;
        }
        Player o =(Player)other;
        return this.getName().equals(o.getName());
    }

    /**
     * This is a getter for username
     * @return username
     */
    public synchronized String getName(){
        return this.username;
    }

    /**
     * This method overrides the default hashCode method for Player type objects
     * @return a hashcode for this Player
     */
    public int hashCode(){
        return (int)(username.hashCode());
    }

    /**
     * This method is used to determine if this player has lost the game
     *
     * @return true if the player left, has no pieces left, or cannot move, else, false
     */
    public boolean lost(){
        return (pieces.size()==0||!InGame||lost);
    }

    /**
     * This sets turn to true
     */
    public void setTurn(){
        turn=true;
    }

    /**
     * This method toggles turn
     */
    public void toggleTurn(){
        turn=!turn;
    }

    /**
     * This method sets turn to false
     */
    public void clearTurn(){
        turn=false;
    }

    /**
     * This method is a getter for turn
     * @return turn
     */
    public boolean isTurn(){
        return turn;
    }

    /**
     * This is a setter for lost
     * @param lost is the value to assign to lost
     */
    public void setLost(boolean lost){
        this.lost=lost;
    }

    /**
     * The getter for isLost
     * @return true if the player has lost
     */
    public boolean isLost() {
        return lost;
    }

    public void setMademoveAttempt(boolean mademoveAttempt) {
        this.mademoveAttempt = mademoveAttempt;
    }

    /**
     * This method is a getter for mademoveAttempt
     * @return mademoveAttempt
     */
    public boolean alreadyMademoveAttempt() {
        return mademoveAttempt;
    }

    /**
     * This method is used to set up the player's jumping pieces
     */
    public void setJumps(){
        board.setUpJumps();
    }

    /**
     * This method is a getter for the forced jumper of the player in a
     * multijump
     * @return the forced jumping piece
     */
    public Piece getForced(){
        return board.getForced();
    }

    /**
     * This method is used to determine if the given piece can jump
     * @param p is the piece to check for
     * @return true iff p can make a MoveJump, else, false
     */
    public boolean canJump(Piece p){
        return board.canJump(p);
    }

    /**
     * This method is used to determine if a piece can make a standard move
     * @param p is the piece to check
     * @return true iff p can make a standard move, else, false
     */
    public boolean canGo(Piece p){
        return board.canGo(p);
    }

    /**
     * This method determines if a Move is allowed
     * @param move is the move to check the legality of
     * @return true iff move is legal, else,false
     */
    public boolean isValid(Move move){
        return board.allowed(move);
    }
}
