package com.webchess.model;

import java.util.ArrayList;

/**
 * This class stores the information necessary to replay a previously played game.
 * @author  Sean Dunn smd6336@rit.edu
 */
public class Replay{
    private String player1;
    private String player2;
    private Player winner;
    private int size;
    private int index;
    private ArrayList<Move> moves;

    public Replay(){
        size = 0;
        index = 0;
        moves = new ArrayList<>();
    }

    /**
     * This method adds a move to the list of moves made that game, and increments the number of turns.
     * @param m Move to be added
     */
    public void addTurn(Move m){
        moves.add(m);
        ++size;
    }

    /**
     * Sets the names of the players involved in the saved game
     * @param p1 Player 1's name
     * @param p2 Player 2's name
     */
    public void setPlayers(String p1, String p2){
        player1 = p1;
        player2 = p2;
    }

    /**
     * Sets the winner of the game once the game ends.
     * @param winner The player who won the game
     */
    public void setWinner(Player winner){
        this.winner = winner;
    }

    /**
     * Returns the name of player 1
     * @return player 1's name
     */
    public String getPlayer1(){
        return player1;
    }

    /**
     * Returns the name of player 2
     * @return player 2's name
     */
    public String getPlayer2(){
        return player2;
    }

    /**
     * Returns the number of turns in the game
     * @return number of turns
     */
    public int getNumTurns(){
        return size;
    }

    /**
     * Returns the list of moves made in the game
     * @return list of moves
     */
    public ArrayList<Move> getMoves() {
        return moves;
    }

    /**
     * Returns the next move in the list
     * @return the Move 1 spot forward in the list
     */
    public Move getNextMove(){
        this.index++;
        return moves.get(this.index);
    }

    /**
     * Returns the previous move in the list
     * @return the Move one spot back in the list
     */
    public Move getPreviousMove(){
        this.index--;
        return moves.get(this.index);
    }

    /**
     * Returns the current index of the move list
     * @return the list index
     */
    public int getIndex(){
        return this.index;
    }

    /**
     * Returns the number of turns in the game
     * @return the size of the move list
     */
    public int getSize(){
        return this.size;
    }

    /**
     * Returns the player who won the saved game.
     * @return the game's winner
     */
    public Player getWinner(){
        return winner;
    }

    /**
     * Determines if this object and another replay object represent the same game.
     * @param obj the second replay to be compared
     * @return true if the 2 objects represent the same game
     */
    @Override
    public boolean equals(Object obj) {
        Replay r = (Replay)obj;
        if((r.getPlayer1().equals(player1)) && (r.getPlayer2().equals(player2)) && (r.getNumTurns() == size) && (r.getMoves().equals(moves))){
            return true;
        }
        return false;
    }

    /**
     * This method overrides the default hashcode method
     * @return a hashcode for the Replay object
     */
    @Override
    public int hashCode() {
        return (player1.hashCode() - player2.hashCode()) * size;
    }
}
