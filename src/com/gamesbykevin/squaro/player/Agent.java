package com.gamesbykevin.squaro.player;

import com.gamesbykevin.framework.base.Cell;
import com.gamesbykevin.framework.util.Timer;

import com.gamesbykevin.squaro.engine.Engine;

/**
 * The agent class will solved the puzzle in a timed manner
 * @author GOD
 */
public final class Agent extends Player implements IPlayer
{
    //the current target
    private Cell current;
    
    //this timer will dictate the delay in movement
    private Timer timer;
    
    public Agent(final long delay)
    {
        super(false);
        
        //the delay between mouse movement
        this.timer = new Timer(delay);
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        //if the puzzle is solved no need to continue
        if (hasSolved())
            return;
        
        //update board render image
        super.update(engine);
        
        //if the board is solved
        if (getBoard().hasSolved())
        {
            super.setSolved();
            return;
        }
        
        //if the current target is not set yet
        if (current == null)
        {
            for (int row=0; row < getBoard().getSize(); row++)
            {
                for (int col=0; col < getBoard().getSize(); col++)
                {
                    //if we have already solved the cell continue to the next one
                    if (getBoard().hasSolvedCell(col, row))
                        continue;

                    //set the current target
                    current = new Cell(col, row);
                    
                    //exit since we found a target
                    return;
                }
            }
        }
        else
        {
            if (!timer.hasTimePassed())
            {
                //update timer
                timer.update(engine.getMain().getTime());
                return;
            }
            
            //we have the target now lets target the pegs inside the target
            for (int row = 0; row <= 1; row++)
            {
                for (int col = 0; col <= 1; col++)
                {
                    //if the current peg is not solved lets do it now
                    if (!getBoard().getPeg(current.getCol() + col, current.getRow() + row).hasSolved())
                    {
                        //mark the peg as solved
                        getBoard().getPeg(current.getCol() + col, current.getRow() + row).setSolved();

                        //reset the timer
                        timer.reset();

                        //exit now
                        return;
                    }
                }
            }

            //all pegs have been solved, so now move to the next target
            current = null;
        }
    }
}