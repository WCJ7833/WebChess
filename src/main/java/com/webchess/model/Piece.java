package com.webchess.model;

/**
 * This class represents a single piece in the game. It is aware of its type, color, and column/row location
 * @author bjs2864@rit.edu
 * @author smd6336@rit.edu
 */
public class Piece {
    /**
     * This is an enum representing what kind of piece it is (ie, single or king)
     */
    public enum TYPE {SINGLE, KING, PAWN, ROOK, KNIGHT, BISHOP, QUEEN}

    /**
     * This is an enum representing what color the piece is
     */
    public enum COLOR {WHITE, RED}

    /**
     * This is the COLOR of this piece
     */
    private COLOR color;

    /**
     * This is the TYPE of this piece
     */
    private TYPE type;

    /**
     * row is the row in which this piece is
     */
    private int row;

    /**
     * col is the column in which this piece is
     */
    private int col;

    /**
     * owner is the Player that owns this piece
     */
    private Player owner;

    /**
     *
     */
    private boolean firstMove;

    /**
     * reverseJump is a boolean that represents whether or not the piece is
     * being used in a backup
     */
    private boolean reverseJump;

    /**
     * board is the board to which this piece belongs (yes, this is needed,
     * sadly)
     */

    private BoardView board;

    /**
     * this is a getter for the owner
     * @return owner
     */

    public Player getOwner(){
        return owner;
    }

    /**
     * This is the constructor for a Piece.
     * @return a Piece with the color and type desired
     */
    public Piece(COLOR colo, TYPE rol, int row, int col, Player owner, BoardView view) {
        color = colo;
        type = rol;
        this.row=row;
        this.col=col;
        this.owner=owner;
        this.firstMove = true;
        this.reverseJump = false;
        board=view;
    }

    /**
     * This method is a setter for board
     * @param board is the BoardView that the board data member should be set to
     */
    public void setBoard(BoardView board){
        this.board=board;
    }

    /**
     * This is a getter for board
     */
    public BoardView getBoard(){
        return board;
    }

    /**
     * This  is a getter for type
     * @return type
     */
    public TYPE getType() {
        return type;
    }

    /**
     * this is a getter for color
     * @return color
     */
    public COLOR getColor() {
        return color;
    }

    /**
     * Makes this piece into a king piece
     */
    public void makeKing(){
            type = TYPE.KING;
    }

    /**
     * This method overrides the default hashCode method for Piece type objects
     * @return a hashcode for this Piece
     */
    public int hashCode(){
        return row+col*col;
    }

    /**
     * This is a getter for row
     * @return row
     */
    public int getRow(){
        return row;
    }

    /**
     * This is a getter for col
     * @return col
     */
    public int getCol(){
        return col;
    }

    /**
     * This method overrides the default equals method for a Piece object
     * @param other is the object to which this is being compared
     * @return true iff the other's column and row equal this Piece's col and row, else, false
     */
    public boolean equals(Object other){
        if(other instanceof Piece) {
            other = (Piece) other;
            return (row == ((Piece) other).getRow() && col == ((Piece) other).getCol() &&
                    ((Piece) other).getOwner().equals(owner));
        }
        else{
            return false;
        }
    }

    /**
     * This is a setter for col
     * @param column is the value to update col to
     */
    public void setCol(int column){
        col=column;
    }

    /**
     * This is a setter for row
     * @param ro is the value to update row to
     */
    public void setRow(int ro){
        row=ro;
    }

    /**
     * This method is a getter for reverseJump
     * @return reverseJump
     */
    public boolean isReverseJump() {
        return reverseJump;
    }

    /**
     * This method is a setter for type
     * @param type is the TYPE to change type data member to
     */
    public void setType(TYPE type) {
        this.type = type;
    }

    /**
     * This is a setter for colo
     * @param color is a COLOR to which the color data member is set
     */
    public void setColor(COLOR color) {
        this.color = color;
    }

    /**
     * This method is a setter for reverseJump
     * @param reverseJump is the boolean to which reverseJump is set
     */
    public void setReverseJump(boolean reverseJump) {
        this.reverseJump = reverseJump;
    }

    /**
     * This method is used to determine if the piece is on the given board
     * @param other is a BoardView
     * @return true iff this piece is on other, else, false
     */
    public boolean sameBoard(BoardView other){
        return this.board.equals(other);
    }

    public boolean isFirstMove() {
        return firstMove;
    }

    public void setFirstMove(boolean firstMove) {
        this.firstMove = firstMove;
    }

    /**
     * This method overrides the default toString() method for Piece objects
     * @return a string representing this Piece
     */
    @Override
    public String toString() {
        return this.getColor() + " R:" + this.getRow() + " C:" + this.getCol();
    }
}