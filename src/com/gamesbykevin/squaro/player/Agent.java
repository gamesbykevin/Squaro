package com.gamesbykevin.squaro.player;

import com.gamesbykevin.framework.base.Cell;

import com.gamesbykevin.squaro.board.Peg;
import com.gamesbykevin.squaro.engine.Engine;

import java.util.ArrayList;
import java.util.List;

/**
 * The agent class will calculate the best possible moves
 * @author GOD
 */
public final class Agent extends Player
{
    private boolean firstRun = true;
    
    public Agent()
    {
        super();
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        //run ai puzzle solving logic here
        
        if (firstRun)
        {
            //pick random location
            final int col = getRandom().nextInt(getBoard().getSize());
            final int row = getRandom().nextInt(getBoard().getSize());
            
            //mark this random location as solved
            this.markResolved(col, row);
            
            //no longer first run
            firstRun = !firstRun;
            
            //exit
            return;
        }
        
        for (int row=0; row < getBoard().getSize(); row++)
        {
            for (int col=0; col < getBoard().getSize(); col++)
            {
                //if we have already solved the cell continue to the next one
                if (getBoard().isSolved(col, row))
                    continue;

                //if the solution count is the max possible or empty we can mark as resolved
                if (getBoard().getSolutionCount(col, row) == (getBoard().getMaxFillCount() * 4) || getBoard().getSolutionCount(col, row) == 0)
                {
                    markResolved(col, row);
                    continue;
                }
                
                //add all 4 pegs from the current Cell
                Cell NW = new Cell(col, row);
                Cell NE = new Cell(col + 1, row);
                Cell SE = new Cell(col + 1, row + 1);
                Cell SW = new Cell(col, row + 1);

                //if the cell is 1 less than the max then one side will be 100% filled for sure
                if ((getBoard().getMaxFillCount() * 4) - 1 == getBoard().getSolutionCount(col, row))
                {
                    //if the west side has only 1 solution
                    if (getBoard().getSolutionCount(col - 1, row) == 1)
                    {
                        //mark the east side as solved
                        mark(NE);
                        mark(SE);
                    }
                    
                    //if the east side has only 1 solution
                    if (getBoard().getSolutionCount(col + 1, row) == 1)
                    {
                        //mark the west side as solved
                        mark(NW);
                        mark(SW);
                    }
                    
                    //if the north side has only 1 solution
                    if (getBoard().getSolutionCount(col, row - 1) == 1)
                    {
                        //mark the south side as solved
                        mark(SW);
                        mark(SE);
                    }
                    
                    //if the south side has only 1 solution
                    if (getBoard().getSolutionCount(col, row + 1) == 1)
                    {
                        //mark the north side as solved
                        mark(NW);
                        mark(NE);
                    }
                    
                    //we have marked one side as solved so continue
                    //continue;
                }
                
                //remaining pegs to make selections
                List<Cell> pegs = new ArrayList<>();
                pegs.add(NW);
                pegs.add(NE);
                pegs.add(SE);
                pegs.add(SW);
                
                int count = 0;
                
                //if the west has been solved
                if (getBoard().isSolved(col - 1, row))
                {
                    if (pegs.indexOf(NW) > -1)
                    {
                        count += getBoard().getPeg(NW).getCurrentCount();
                        pegs.remove(NW);
                    }
                    
                    if (pegs.indexOf(SW) > -1)
                    {
                        count += getBoard().getPeg(SW).getCurrentCount();
                        pegs.remove(SW);
                    }
                }
                
                //if the north has been solved
                if (getBoard().isSolved(col, row - 1))
                {
                    if (pegs.indexOf(NW) > -1)
                    {
                        count += getBoard().getPeg(NW).getCurrentCount();
                        pegs.remove(NW);
                    }
                    
                    if (pegs.indexOf(NE) > -1)
                    {
                        count += getBoard().getPeg(NE).getCurrentCount();
                        pegs.remove(NE);
                    }
                }
                
                
                //if the east has been solved
                if (getBoard().isSolved(col + 1, row))
                {
                    if (pegs.indexOf(NE) > -1)
                    {
                        count += getBoard().getPeg(NE).getCurrentCount();
                        pegs.remove(NE);
                    }
                    
                    if (pegs.indexOf(SE) > -1)
                    {
                        count += getBoard().getPeg(SE).getCurrentCount();
                        pegs.remove(SE);
                    }
                }
                
                //if the south has been solved
                if (getBoard().isSolved(col, row + 1))
                {
                    if (pegs.indexOf(SE) > -1)
                    {
                        count += getBoard().getPeg(SE).getCurrentCount();
                        pegs.remove(SE);
                    }
                    
                    if (pegs.indexOf(SW) > -1)
                    {
                        count += getBoard().getPeg(SW).getCurrentCount();
                        pegs.remove(SW);
                    }
                }
                
                //the remaining directions are available spots
                if (!pegs.isEmpty())
                {
                    //the solution count minus the already occupied count so we know how much left
                    int remainingCount = (getBoard().getSolutionCount(col, row) - count);
                    
                    //get the remaining total count
                    int maxAvailableCount = getBoard().getMaxFillCount() * pegs.size();
                    
                    //if the remaining selections equal the max available, mark solved
                    if (remainingCount == maxAvailableCount)
                    {
                        this.markResolved(col, row);
                    }
                }
            }
        }
        
        //update board render image
        super.update(engine);
    }
    
    /**
     * Mark all 4 pegs in this location as resolved
     * @param col
     * @param row 
     */
    private void markResolved(final int col, final int row)
    {
        //mark all 4 pegs as solved
        mark(col, row);
        mark(col + 1, row);
        mark(col + 1, row + 1);
        mark(col, row + 1);
    }
    
    private void mark(final Cell cell)
    {
        mark(cell.getCol(), cell.getRow());
    }
    
    /**
     * Mark the current Peg as solved
     * @param col
     * @param row 
     */
    private void mark(final int col, final int row)
    {
        //get the correct peg
        Peg tmp = getBoard().getPeg(col, row);

        //set the fill to the solution
        tmp.setFill(tmp.getSolutionCount());
    }
}