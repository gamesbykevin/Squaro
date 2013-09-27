package com.gamesbykevin.squaro.board;

import com.gamesbykevin.framework.base.Sprite;

import java.awt.Color;

import java.awt.Graphics;

/**
 * Each cell contains a number of pegs
 * @author GOD
 */
public class Peg extends Sprite
{
    //default to empty
    private float fill = 0;
    
    //the increase rate every time a peg has been clicked by the mouse
    private float fillRate = 0;
    
    //the count we are currently at
    private int currentCount;
    
    //the correct count to solve
    private int solutionCount;
    
    public Peg(final float fillRate, final float solution)
    {
        //set the rate
        this.fillRate = fillRate;
        
        //count the number of loops until we find the solution
        int count = 0;
        
        //our tmp test variable
        float test = 0;
        
        //continue until the test has reached the solution
        while(test < solution)
        {
            //increment the count
            count++;
            
            //increase the temp fill
            test += fillRate;
        }
        
        //we have found the correct count to solve the board
        this.solutionCount = count;
    }
    
    /**
     * Move to the next index in the Status collection
     */
    public void changeFill()
    {
        //increase the fill
        this.fill += this.fillRate;
        
        ///increment the current count
        this.currentCount++;
        
        //if we reached the end, start back at begining
        if (fill > 1)
        {
            fill = 0;
            currentCount = 0;
        }
    }
    
    /**
     * Get the solution count for this Peg
     * @return count 0, 1, etc.....
     */
    public int getSolutionCount()
    {
        return this.solutionCount;
    }
    
    /**
     * Get the count based on the current selection
     * @return count 0, 1, etc.....
     */
    public int getCurrentCount()
    {
        return this.currentCount;
    }
    
    /**
     * Get the current fill that is set
     * @return float 0, .25, .5 etc..
     */
    private float getCurrentFill()
    {
        return this.fill;
    }
    
    /**
     * Draw the peg accordingly
     * @param graphics 
     */
    public void render(final Graphics graphics)
    {
        //fill empty oval in
        graphics.setColor(Color.BLACK);
        graphics.fillOval((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
        
        //quarter
        if (getCurrentFill() == 0.25)
        {
            graphics.setColor(Color.WHITE);
            graphics.fillArc((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), 180, 180);

            graphics.setColor(Color.BLACK);
            graphics.fillArc((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), 90, 180);
        }

        //half
        if (getCurrentFill() == 0.50)
        {
            graphics.setColor(Color.WHITE);
            graphics.fillArc((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), 180, 180);
        }
        
        //3 quarters
        if (getCurrentFill() == 0.75)
        {
            graphics.setColor(Color.WHITE);
            graphics.fillArc((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), 180, 180);

            graphics.setColor(Color.BLACK);
            graphics.fillArc((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), 90, 180);

            graphics.setColor(Color.WHITE);
            graphics.fillArc((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), 90, 180);
        }
        
        //full
        if (getCurrentFill() == 1)
        {
            graphics.setColor(Color.WHITE);
            graphics.fillOval((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
        }
        
        //draw the outline
        graphics.setColor(Color.WHITE);
        graphics.drawOval((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
    }
}