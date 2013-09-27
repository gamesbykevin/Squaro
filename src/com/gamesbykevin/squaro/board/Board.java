package com.gamesbykevin.squaro.board;

import com.gamesbykevin.framework.base.Sprite;

import com.gamesbykevin.squaro.engine.Engine;
import java.awt.Color;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import java.util.Random;

/**
 * This is the board where game play will occur
 * @author GOD
 */
public final class Board extends Sprite
{
    //we will write the board to a buffered image
    private BufferedImage bufferedImage;
    
    //the cell width/height will be the same
    private int cellDimension;
    
    //each board will have the same amount of columns and rows
    private int size;
    
    //the peg width/height will be the same
    private int pegSize;
    
    //list of the pegs
    private List<Peg> pegs;
    
    //seed used for random number generation
    private final long seed = System.nanoTime();
    
    //our random numbner generator
    private Random random;
    
    //the peg size will be a % of the cell size
    private static final float PEG_SIZE_RATIO = .33F;
    
    public enum Difficulty
    {
        Easy(1F), Medium(.5F), Hard(.25F);
        
        //the rate to fill the peg
        private float fillRate;
        
        private Difficulty(final float fillRate)
        {
            this.fillRate = fillRate;
        }

        public float getFillRate()
        {
            return this.fillRate;
        }
    }
    
    /**
     * Create a new board
     * @param cols The column board size. NOTE: This is not the number of pegs
     * @param rows The row board size. NOTE: This is not the number of pegs
     * @param container The container the board will be drawn within
     */
    public Board(final int size, final Dimension container, final int difficultyIndex)
    {
        //the number of column/row cells in this Board
        this.size = size;
        
        //width and height of a peg
        this.pegSize = (int)((container.width / size) * PEG_SIZE_RATIO);
        
        //width and height of a cell
        this.cellDimension = ((container.width - pegSize) / size);
        
        //now that we have the cell and peg sizes we can calculate the width/height of the entire board
        super.setWidth(container.width + pegSize);
        super.setHeight(container.height + pegSize);
        
        //create a new buffered image to write to
        this.bufferedImage = new BufferedImage((int)getWidth(), (int)getHeight(), BufferedImage.TYPE_INT_ARGB);
        
        //create a list of pegs
        this.pegs = new ArrayList<>();
        
        //create a random number generator
        this.random = new Random(seed);
        
        //get the fill rate based on the difficulty
        final float fillRate = Difficulty.values()[difficultyIndex].getFillRate();
        
        //create a list of possible solutions
        List<Float> solutions = new ArrayList<>();
        
        for (float current = 0; current <= 1; current += fillRate)
        {
            //add current fill rate to list
            solutions.add(current);
        }
        
        //there will be an extra peg on the end
        for (int row = 0; row <= size; row++)
        {
            //there will be an extra peg on the end
            for (int col = 0; col <= size; col++)
            {
                //get a random solution from our list
                final float randomSolution = solutions.get(random.nextInt(solutions.size()));
                
                //create a new instance of peg
                Peg peg = new Peg(fillRate, randomSolution);
                
                //set the column, row
                peg.setCol(col);
                peg.setRow(row);
                
                //set the dimensions of the peg
                peg.setWidth(pegSize);
                peg.setHeight(pegSize);
                
                //set the location of the peg
                peg.setX(col * cellDimension);
                peg.setY(row * cellDimension);
                
                //add the peg to the list
                pegs.add(peg);
            }
        }
    }
    
    public void update(final Engine engine)
    {
        final boolean hasMouseClick = (engine.getMouse().isMouseReleased());
        
        //if the mouse was clicked check for collision
        if (hasMouseClick)
        {
            //get the mouse location
            Point location = new Point(engine.getMouse().getLocation());
            
            //offset the location because the board could be drawn anywhere
            location.x -= getX();
            location.y -= getY();
            
            for (Peg peg : pegs)
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
        }
        
        //render our board to a single image
        renderImage();
    }
    
    /**
     * Get the peg at the specified column and row
     * @param col Column peg is in
     * @param row Row peg is in
     * @return Peg, if no peg exists at the specified location null is returned
     */
    private Peg getPeg(final int col, final int row)
    {
        for (Peg peg : pegs)
        {
            if (peg.equals(col, row))
                return peg;
        }
        
        return null;
    }
    
    private void renderImage()
    {
        //create graphics object
        final Graphics graphics = bufferedImage.createGraphics();
        
        graphics.setFont(graphics.getFont().deriveFont(Font.BOLD, 14));
        
        //draw horizontal and vertical lines on the board
        for (int count=0; count <= size; count++)
        {
            //get the two vertical endpoints
            Point center1 = getPeg(count, 0).getCenter();
            Point center2 = getPeg(count, size).getCenter();

            //draw the line
            graphics.drawLine(center1.x, center1.y, center2.x, center2.y);

            //get the two horizontal endpoints
            center1 = getPeg(0, count).getCenter();
            center2 = getPeg(size, count).getCenter();

            //draw the line
            graphics.drawLine(center1.x, center1.y, center2.x, center2.y);
        }
        
        //check all the board cells to display the count
        for (int row=0; row < size; row++)
        {
            for (int col=0; col < size; col++)
            {
                //get the temporary peg so we can tell where to draw the count
                Peg tmp = getPeg(col, row);
                
                final int countSolution = getSolutionCount(col, row);
                final int countSelection = getSelectionCount(col, row);
                
                //get the pixel width for the count number
                final int countWidth = graphics.getFontMetrics().stringWidth(countSolution + "");
                
                //get the pixel height for the count number
                final int countHeight = graphics.getFontMetrics().getHeight();
                
                //calculate the render position of the count
                final int x = (int)(tmp.getX() + (cellDimension / 2) + (countWidth));
                final int y = (int)(tmp.getY() + (cellDimension / 2) + (countHeight));
                
                //if the current count does not match the solution display red to the user
                if (countSolution != countSelection)
                    graphics.setColor(Color.RED);
                
                //if the current count matches the solution display green to the user
                if (countSolution == countSelection)
                    graphics.setColor(Color.GREEN);
                
                //display the count
                graphics.drawString(countSolution + "", x, y);
            }
        }
        
        //write all peg(s) to the Buffered Image
        for (Peg peg : pegs)
        {
            peg.render(graphics);
        }
    }
    
    private int getSelectionCount(final int col, final int row)
    {
        //get the selection count for all neighbors
        int count = 0;
        
        //count current position
        count += getPeg(col, row).getCurrentCount();
        
        //count position to the south
        count += getPeg(col, row + 1).getCurrentCount();
        
        //count position to the south east
        count += getPeg(col + 1, row + 1).getCurrentCount();
        
        //count position to the east
        count += getPeg(col + 1, row).getCurrentCount();
        
        return count;
    }
    
    private int getSolutionCount(final int col, final int row)
    {
        //get the solution count for all neighbors
        int count = 0;
        
        //count current position
        count += getPeg(col, row).getSolutionCount();
        
        //count position to the south
        count += getPeg(col, row + 1).getSolutionCount();
        
        //count position to the south east
        count += getPeg(col + 1, row + 1).getSolutionCount();
        
        //count position to the east
        count += getPeg(col + 1, row).getSolutionCount();
        
        return count;
    }
    
    /**
     * Draw the buffered image.
     * @param graphics 
     */
    public void render(final Graphics graphics)
    {
        graphics.drawImage(bufferedImage, (int)getX(), (int)getY(), (int)getWidth(), (int)getHeight(), null);
    }
}