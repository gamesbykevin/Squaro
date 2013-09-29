package com.gamesbykevin.squaro.player;

import com.gamesbykevin.squaro.board.Peg;
import com.gamesbykevin.squaro.engine.Engine;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * The human class will check for mouse input
 * @author GOD
 */
public final class Human extends Player implements IPlayer
{
    public Human()
    {
        super(true);
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        //if the puzzle is solved no need to continue
        if (hasSolved())
            return;
        
        //update board render image
        super.update(engine);
        
        //if the board was solved
        if (getBoard().hasSolved())
        {
            super.setSolved();
            return;
        }
        
        //was the mouse pressed
        final boolean hasMousePress = (engine.getMouse().isMousePressed());
        
        //if the mouse was pressed check for collision
        if (hasMousePress)
        {
            //get the mouse location
            Point location = new Point(engine.getMouse().getLocation());
            
            //offset the location because the board could be drawn anywhere
            location.x -= getBoard().getX();
            location.y -= getBoard().getY();
            
            for (Peg peg : getBoard().getPegs())
            {
                //if the peg we clicked is contained inside a Rectangle
                if (peg.getRectangle().contains(location))
                {
                    //increase the fill
                    peg.changeFill();
                    
                    //at this point there is no possible way to check another peg so exit loop
                    break;
                }
            }
            
            //reset the mouse events
            engine.getMouse().reset();
        }
    }
}