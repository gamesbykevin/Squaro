package com.gamesbykevin.squaro.player;

import com.gamesbykevin.squaro.board.Peg;
import com.gamesbykevin.squaro.engine.Engine;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * The human class will check for mouse input
 * @author GOD
 */
public final class Human extends Player
{
    @Override
    public void update(final Engine engine) throws Exception
    {
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
                //get the rectangle because we need to offset the coordinates for the mouse location
                Rectangle tmp = peg.getRectangle();
                
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
        
        //update board render image
        super.update(engine);
    }
}