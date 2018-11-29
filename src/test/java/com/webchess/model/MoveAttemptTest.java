package com.webchess.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class MoveAttemptTest{
    Player p1;
    Player p2;
    Position p27;
    Position p45;
    BoardView board;

    @BeforeEach
    public void setup(){
        p1=mock(Player.class);
        p2=mock(Player.class);
        when(p1.getName()).thenReturn("Francis");
        when(p2.getName()).thenReturn("Jessica");
        board=new BoardView(p1,p2,p1);
        board.setForTest();
        p27=mock(Position.class);
        p45=mock(Position.class);
        when(p27.getCell()).thenReturn(7);
        when(p27.getRow()).thenReturn(2);
        when(p45.getCell()).thenReturn(5);
        when(p45.getRow()).thenReturn(4);
    }

    @Test
    public void testExtraJump(){
        Move m = new MoveAttempt(p27, p45, board);
        assertFalse(m.allowExtraMove()); //should always return false
    }

    @Test
    public void testExecute(){

    }

    @Test
    public void testEquals(){
        MoveJump j1=new MoveJump(p27,p45,board);
        MoveJump j2=new MoveJump(p27,p45,board);
        assertTrue(j1.equals(j2),"Identical boards not equal!");
    }

    @Test
    public void testEquals2(){
        MoveJump j1=new MoveJump(p27,p45,board);
        MoveJump j2=new MoveJump(p27,p27,board);
        assertFalse(j1.equals(j2),"Non-identical boards equal!");
    }

    @Test
    public void testEquals3(){
        MoveJump j1=new MoveJump(p45,p45,board);
        MoveJump j2=new MoveJump(p27,p45,board);
        assertFalse(j1.equals(j2),"Non-identical boards equal!");
    }

    @Test
    public void testEquals4(){
        MoveJump j1=new MoveJump(p45,p45,board);
        assertFalse(j1.equals("A++ please"),"MoveJump equals a string...");
    }

    @Test
    public void testHash(){
        MoveJump j1=new MoveJump(p45,p45,board);
        assertTrue(j1.hashCode()==-11,"wrong hash");
    }
}
