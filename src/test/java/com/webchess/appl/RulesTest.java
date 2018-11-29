package com.webchess.appl;
import com.webchess.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class is a JUnit test for rules. Due to the nature of this class,
 * it will require full instances of BoardView, Player, and Move objects.
 * This is O.K. because these classes have been tested already, and it otherwise
 * @author Brunon Sztuba bjp52864@rit.edu
 */
@Tag("Model-tier")
public class RulesTest {
    /**
     * This is one of the players (red)
     */
    private Player p1=new Player("George");

    /**
     * this is the other player(black)
     */
    private Player p2=new Player("Julia");

    /**
     * This is the board for which the rules are used
     */
    private BoardView board;

    /**
     * this is a move attempt for testing
     */
    private MoveAttempt move1;

    /**
     * this is a move attempt for testing
     */
    private MoveAttempt move2;

    /**
     * this is a move attempt for testing
     */
    private MoveAttempt move3;

    /**
     * this is a move attempt for testing
     */
    private MoveAttempt move4;

    /**
     * this is a move attempt for testing
     */
    private MoveAttempt move5;

    /**
     * this is a move attempt for testing
     */
    private MoveAttempt move6;

    /**
     * this is a move attempt for testing
     */
    private MoveAttempt move7;

    /**
     * This is a move used for testing
     */
    private MoveAttempt move8;

    /**
     * this is a position used for testing
     */
    private Position p70;

    /**
     * this is a position used for testing
     */
    private Position p52;

    /**
     * this is a position used for testing
     */
    private Position p45;

    /**
     * this is a position used for testing
     */
    private Position p36;

    /**
     * this is a position used for testing
     */
    private Position p54;

    /**
     * this is a position used for testing
     */
    private Position p41;

    /**
     * this is a position used for testing
     */
    private Position p27;

    /**
     * this is a position used for testing
     */
    private Position p00;

    /**
     * this is a position used for testing
     */
    private Position p50;

    /**
     * This is the object being tested
     */
    private Rules testee;

    /**
     * This is a setup method that gets called before each test to reset the data members
     */
    @BeforeEach
    public void setup(){
        p1.setColor(Piece.COLOR.RED);
        p2.setColor(Piece.COLOR.WHITE);
        board=new BoardView(p1,p2,p1);
        testee=board.getRules();
        p1.setColor(Piece.COLOR.RED);
        p2.setColor(Piece.COLOR.WHITE);
        p70=new Position(7,0);//p52 to p41 should work
        p52=new Position(5,2);
        p45=new Position(4,5);
        p41=new Position(4,1);
        p36=new Position(3,6);
        p54=new Position(5,4);
        p27=new Position(2,7);
        p00=new Position(0,0);
        p50=new Position(5,0);
        move1=new MoveAttempt(p70,p41,board);//false
        move2=new MoveAttempt(p70,p27,board);//false
        move3=new MoveAttempt(p70,p00,board);//false
        move4=new MoveAttempt(p52,p41,board);//true (standard move)
        move5=new MoveAttempt(p45,p27,board);//true after setup
        move6=new MoveAttempt(p27,p45,board);//true after king setup
        move7=new MoveAttempt(p45,p27,board);//true after king setup
        move8=new MoveAttempt(p50,p41,board);
    }

    /**
     * this test checks that a piece is not allowed to go into an occupied adjacent space
     */
    @Test
    public void testIsValid1(){
        p1.setTurn();
        p2.clearTurn();
        assertFalse((Boolean)testee.isValid(move1).get(0),"piece allowed to occupy occupied space");
    }

    /**
     * this test ensures that a piece cannot be moved to an occupied, far away spot
     */
    @Test
    public void testIsValid2(){
        p1.setTurn();
        p2.clearTurn();
        assertFalse((Boolean)testee.isValid(move2).get(0),"piece allowed to go too far");
    }

    /**
     * this test ensures that a piece can make a standard moe that is valid
     */
    @Test
    public void testIsValid3(){
        p1.setTurn();
        p2.clearTurn();
        assertFalse((Boolean)testee.isValid(move3).get(0),"piece allowed to go crazy far");
    }

    /**
     * this test ensures that a piece can make a standard move if valid
     */
    @Test
    public void testIsValid4(){
        p1.setTurn();
        p2.clearTurn();
        assertTrue((Boolean)testee.isValid(move4).get(0),"piece not allowed to make standard move");
    }

    /**
     * this test ensures you cannot make moves in spaces where there are no pieces
     */
    @Test
    public void testIsValid5(){
        p1.setTurn();
        p2.clearTurn();
        assertFalse((Boolean)testee.isValid(move5).get(0),"piece allowed to occupy occupy space");
    }
    /**
     * this test ensures that valid single jumps are allowed
     */
    @Test
    public void testIsValid5A(){
        board.setForTest();
        p1.setTurn();
        p2.clearTurn();
        assertTrue((Boolean)testee.isValid(move5).get(0),"piece can't jump");
    }

    /**
     * this test ensures that kings can jump backwards
     */
    @Test
    public void kingTest5A(){
        board.setForTestKing(p52,p27,p27,p36);
        p1.setTurn();
        p2.clearTurn();
        assertTrue((Boolean)testee.isValid(move6).get(0),"king piece can't jump back left");
    }

    /**
     * this test ensures that kings can jump forwards
     */
    @Test
    public void kingTest6(){
        board.setForTestKing(p54,p45,p27,p36);
        p1.setTurn();
        p2.clearTurn();
        assertTrue((Boolean)testee.isValid(move7).get(0),"king piece can't jump front right");
    }

    /**
     * this test ensures that the player can move some piece in their front row at start
     */
    @Test
    public void testPieceCanMove(){
        p1.setTurn();
        p2.clearTurn();
        List<Piece> pie=new ArrayList<>();
        for(int i=0;i<8;i+=2){
            pie.add(board.getPiece(5,i));
        }
        assertTrue(testee.canMove(pie));
    }

    /**
     * this test ensures that no piece in the 6th t0w can move at the start
     */
    @Test
    public void testPieceCantMove(){
        p1.setTurn();
        p2.clearTurn();
        List<Piece> pie=new ArrayList<>();
        for(int i=1;i<8;i+=2){
            pie.add(board.getPiece(6,i));
        }
        assertTrue(!testee.canMove(pie));
    }

    /**
     * this test ensures that a piece in a valid jumping spot can jump
     */
    @Test
    public void canJump1(){
        board.setForTest();
        p1.setTurn();
        p2.clearTurn();
        Piece jumper=board.getPiece(4,5);
        assertTrue(testee.canJump(jumper),"piece that can jump not allowed");
    }

    /**
     * this test ensures that pieces not in a valid jump spot cannot jump
     */
    @Test
    public void canJump2(){
        Piece jumper=board.getPiece(5,4);
        assertTrue(!testee.canJump(jumper),"piece that can't jump allowed");
    }

    /**
     * this test ensures that a player whose turn it is at start of game can make moves
     */
    @Test
    public void  testCanMove(){
        assertTrue(board.canMove(board.getPieces()),"player cannot move at start");
    }

    /**
     * this test ensures that every piece in the player's first row can move
     */
    @Test
    public void testCanGo(){
        boolean check=true;
        for(int i=0;i<8;i+=2){
            boolean go=board.getRules().canGo(board.getPiece(5,i));
            if(!go){
                check=false;
            }
        }
        assertTrue(check,"piece in front row of player1 can't move");
    }

    /**
     * This tests column 0 standard moves
     */
    @Test
    public void testCol0(){
        p1.setTurn();
        p2.clearTurn();
        assertTrue((boolean)board.isValid(move8).get(0),
                "Cannot jump from column zero");
    }
}
