package com.gamesbykevin.squaro.player;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.util.Timer;
import com.gamesbykevin.framework.util.TimerCollection;

import com.gamesbykevin.squaro.board.Board;
import com.gamesbykevin.squaro.engine.Engine;
import java.awt.Color;

import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;

/**
 * This will contain all of the player objects
 * @author GOD
 */
public abstract class Player extends Sprite
{
    //the game board
    private Board board;
    
    //the game timer
    private Timer timer;
    
    //where to draw the location
    private Point location;
    
    //seed used for random number generation
    private final long seed = System.nanoTime();
    
    //our random numbner generator
    private Random random;
    
    public Player()
    {
        //create a random number generator
        this.random = new Random(seed);
        
        System.out.println("The seed = \"" + seed + "\"");
    }
    
    protected Random getRandom()
    {
        return this.random;
    }
    
    public void update(final Engine engine) throws Exception
    {
        if (this.timer == null)
        {
            //create new timer and location
            this.timer = new Timer();
            this.location = new Point((int)getX() + 50, (int)getY() + 50);
        }
        
        timer.update(engine.getMain().getTime());
        
        getBoard().update(engine);
    }
    
    public void createBoard(final int boardDimension, final int difficultyIndex)
    {
        board = new Board(boardDimension, difficultyIndex, random);
    }
    
    public Board getBoard()
    {
        return this.board;
    }
    
    @Override
    public void setLocation(final int x, final int y)
    {
        super.setLocation(x, y);
        
        //place board below, so we have space above to render additional information
        getBoard().setLocation(x, y + 75);
    }
    
    public void render(final Graphics graphics)
    {
        getBoard().render(graphics);
        
        if (timer != null && location != null)
        {
            graphics.setColor(Color.WHITE);
            graphics.drawString(timer.getDescPassed(TimerCollection.FORMAT_6), location.x, location.y);
        }
    }
}