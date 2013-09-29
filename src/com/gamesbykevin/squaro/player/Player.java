package com.gamesbykevin.squaro.player;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.util.Timer;
import com.gamesbykevin.framework.util.TimerCollection;

import com.gamesbykevin.squaro.board.Board;
import com.gamesbykevin.squaro.board.Board.Difficulty;
import com.gamesbykevin.squaro.engine.Engine;
import java.awt.Color;

import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;

/**
 * This will contain all of the player objects
 * @author GOD
 */
public abstract class Player extends Sprite implements IPlayer
{
    //the game board
    private Board board;
    
    //the game timer
    private Timer timer;
    
    //where to draw the location and status
    private Point location, status;
    
    //seed used for random number generation
    private final long seed = System.nanoTime();
    
    //our random numbner generator
    private Random random;
    
    //has the entire board been solved
    private boolean solved = false;
    
    //is the player human
    private final boolean human;
    
    public Player(final boolean human)
    {
        //is this player human
        this.human = human;
        
        //create a random number generator
        this.random = new Random(seed);
    }
    
    /**
     * Has the board been solved
     * @return true if solved, false otherwise
     */
    public boolean hasSolved()
    {
        return this.solved;
    }
    
    protected void setSolved()
    {
        this.solved = true;
    }
    
    private void resetSolved()
    {
        this.solved = false;
    }
    
    protected Random getRandom()
    {
        return this.random;
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        if (this.timer == null)
        {
            //create new timer and location
            this.timer = new Timer();
        }
        
        timer.update(engine.getMain().getTime());
        
        getBoard().update(engine);
    }
    
    public void createBoard(final int boardDimension, final Difficulty difficulty)
    {
        //the player does not have the board solved
        resetSolved();
        
        //reset the timer
        if (timer != null)
            timer.reset();
        
        if (board != null)
        {
            board = new Board(boardDimension, difficulty, random);
            
            this.setLocation((int)getX(), (int)getY());
        }
        else
        {
            board = new Board(boardDimension, difficulty, random);
        }
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
    
    @Override
    public void render(final Graphics graphics)
    {
        //font size is 18
        graphics.setFont(graphics.getFont().deriveFont(18f));
        
        //draw board
        getBoard().render(graphics);
        
        if (timer != null)
        {
            if (location == null)
            {
                final int width = (int)(graphics.getFontMetrics().stringWidth(TimerCollection.FORMAT_6) * 1.25);
                
                location = new Point();
                location.x = (int)(getX());
                location.y = (int)(getY() + 60);
                
                status = new Point();
                status.x = location.x + width + 5;
                status.y = location.y;
            }
            
            graphics.setColor(Color.WHITE);
            graphics.drawString(timer.getDescPassed(TimerCollection.FORMAT_6), location.x, location.y);
            
            if (human)
            {
                graphics.drawString("Human", status.x, status.y);
            }
            else
            {
                graphics.drawString("Cpu", status.x, status.y);
            }
        }
    }
}