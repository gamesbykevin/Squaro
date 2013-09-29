package com.gamesbykevin.squaro.board;

import com.gamesbykevin.framework.base.Cell;
import com.gamesbykevin.framework.base.Sprite;

import com.gamesbykevin.squaro.engine.Engine;
import com.gamesbykevin.squaro.resource.Resources.GameImage;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
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
        
    //the peg size will be a % of the cell size
    private static final float PEG_SIZE_RATIO = .33F;
    
    //the board width and height
    private static final int DIMENSION = 300;
    
    //the number of different selections for a single peg
    private final int maxFillCount;
    
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
    public Board(final int size, final int difficultyIndex, final Random random)
    {
        //the number of column/row cells in this Board
        this.size = size;
        
        //width and height of a peg
        this.pegSize = (int)((DIMENSION / size) * PEG_SIZE_RATIO);
        
        //width and height of a cell
        this.cellDimension = ((DIMENSION - pegSize) / size);
        
        //now that we have the cell and peg sizes we can calculate the width/height of the entire board
        super.setWidth(DIMENSION + pegSize);
        super.setHeight(DIMENSION + pegSize);
        
        //create a new buffered image to write to
        this.bufferedImage = new BufferedImage((int)getWidth(), (int)getHeight(), BufferedImage.TYPE_INT_ARGB);
        
        //create a list of pegs
        this.pegs = new ArrayList<>();
        
        //get the fill rate based on the difficulty
        final float fillRate = Difficulty.values()[difficultyIndex].getFillRate();
        
        //create a list of possible solutions
        List<Float> solutions = new ArrayList<>();
        
        for (float current = 0; current <= 1; current += fillRate)
        {
            //add current fill rate to list
            solutions.add(current);
        }
        
        int count = 0;
        float start = 0;
        
        //loop until we reach 100% full so we know the max count
        while (start < 1)
        {
            start += fillRate;
            count++;
        }
        
        this.maxFillCount = count;
        
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
    
    /**
     * Get the count at which the fill for a specific peg is 100%
     * @return int
     */
    public int getMaxFillCount()
    {
        return this.maxFillCount;
    }
    
    /**
     * Get the Board dimensions. <br><br>
     * Each board will have the same number of columns and rows.
     * @return int The dimension(s)
     */
    public int getSize()
    {
        return this.size;
    }
    
    public void update(final Engine engine) throws Exception
    {
        //render our board to a single image
        renderImage(engine);
    }
    
    public List<Peg> getPegs()
    {
        return this.pegs;
    }
    
    public Peg getPeg(final Cell cell)
    {
        return getPeg(cell.getCol(), cell.getRow());
    }
    
    /**
     * Get the peg at the specified column and row
     * @param col Column peg is in
     * @param row Row peg is in
     * @return Peg, if no peg exists at the specified location null is returned
     */
    public Peg getPeg(final int col, final int row)
    {
        for (Peg peg : pegs)
        {
            if (peg.equals(col, row))
                return peg;
        }
        
        return null;
    }
    
    private void renderImage(final Engine engine) throws Exception
    {
        //create graphics object
        final Graphics graphics = bufferedImage.createGraphics();
        
        //start Point
        Point start = getPeg(0, 0).getCenter();
        
        //check all the board cells to display the count
        for (int row=0; row < size; row++)
        {
            for (int col=0; col < size; col++)
            {
                //the real solution
                final int countSolution = getSolutionCount(col, row);
                
                //the current value
                final int countSelection = getSelectionCount(col, row);
                
                //the appropriate Image to draw
                final Image img = engine.getResources().getGameImage(getNumberKey(countSelection, countSolution));

                //location
                final int x = start.x + (col * cellDimension);
                final int y = start.y + (row * cellDimension);

                //draw the image
                graphics.drawImage(img, x, y, cellDimension, cellDimension, null);
            }
        }
        
        //write all peg(s) to the Buffered Image
        for (Peg peg : pegs)
        {
            peg.render(graphics);
        }
    }
    
    public boolean hasSolved()
    {
        for (int col=0; col < getSize(); col++)
        {
            for (int row=0; row < getSize(); row++)
            {
                if (!isSolved(col, row))
                    return false;
            }
        }
        
        return true;
    }

    public boolean isSolved(final Cell cell)
    {
        return isSolved(cell.getCol(), cell.getRow());
    }
    
    /**
     * This method just checks to make sure the selection count equals the solution count for the specific Cell.
     * @param col
     * @param row
     * @return True if the solution count matches the selection count
     */
    public boolean isSolved(final int col, final int row)
    {
        //the real solution
        final int countSolution = getSolutionCount(col, row);

        //the current value
        final int countSelection = getSelectionCount(col, row);
        
        return (countSolution == countSelection);
    }
    
    /**
     * Get the appropriate Image to display
     * @param number The current number
     * @param solution The actual solution
     * @return GameImage key, the key that represents the Image
     * @throws Exception 
     */
    private GameImage getNumberKey(final int number, final int solution) throws Exception
    {
        switch(solution)
        {
            case 0:
                if (number == solution)
                    return GameImage.NS0;
                else
                    return GameImage.NE0;

            case 1:
                if (number == solution)
                    return GameImage.NS1;
                else
                    return GameImage.NE1;

            case 2:
                if (number == solution)
                    return GameImage.NS2;
                else
                    return GameImage.NE2;

            case 3:
                if (number == solution)
                    return GameImage.NS3;
                else
                    return GameImage.NE3;

            case 4:
                if (number == solution)
                    return GameImage.NS4;
                else
                    return GameImage.NE4;

            case 5:
                if (number == solution)
                    return GameImage.NS5;
                else
                    return GameImage.NE5;

            case 6:
                if (number == solution)
                    return GameImage.NS6;
                else
                    return GameImage.NE6;

            case 7:
                if (number == solution)
                    return GameImage.NS7;
                else
                    return GameImage.NE7;

            case 8:
                if (number == solution)
                    return GameImage.NS8;
                else
                    return GameImage.NE8;

            case 9:
                if (number == solution)
                    return GameImage.NS9;
                else
                    return GameImage.NE9;

            case 10:
                if (number == solution)
                    return GameImage.NS10;
                else
                    return GameImage.NE10;

            case 11:
                if (number == solution)
                    return GameImage.NS11;
                else
                    return GameImage.NE11;

            case 12:
                if (number == solution)
                    return GameImage.NS12;
                else
                    return GameImage.NE12;

            case 13:
                if (number == solution)
                    return GameImage.NS13;
                else
                    return GameImage.NE13;

            case 14:
                if (number == solution)
                    return GameImage.NS14;
                else
                    return GameImage.NE14;

            case 15:
                if (number == solution)
                    return GameImage.NS15;
                else
                    return GameImage.NE15;

            case 16:
                if (number == solution)
                    return GameImage.NS16;
                else
                    return GameImage.NE16;
        }
        
        throw new Exception("Number is not setup");
    }
    
    /**
     * Get the current count for the given column, row
     * @param col Column
     * @param row Row
     * @return int, The current count
     */
    public int getSelectionCount(final int col, final int row)
    {
        //get the selection count for all neighbors
        int count = 0;
        
        if (getPeg(col, row) != null)
        {
            //count current position
            count += getPeg(col, row).getCurrentCount();
        }
        
        if (getPeg(col, row + 1) != null)
        {
            //count position to the south
            count += getPeg(col, row + 1).getCurrentCount();
        }
        
        if (getPeg(col + 1, row + 1) != null)
        {
            //count position to the south east
            count += getPeg(col + 1, row + 1).getCurrentCount();
        }
        
        if (getPeg(col + 1, row) != null)
        {
            //count position to the east
            count += getPeg(col + 1, row).getCurrentCount();
        }
        
        return count;
    }
    
    /**
     * Get the solution count for the given column, row
     * @param col Column
     * @param row Row
     * @return int, The solution
     */
    public int getSolutionCount(final int col, final int row)
    {
        //get the solution count for all neighbors
        int count = 0;
        
        if (getPeg(col, row) != null)
        {
            //count current position
            count += getPeg(col, row).getSolutionCount();
        }
        
        if (getPeg(col, row + 1) != null)
        {
            //count position to the south
            count += getPeg(col, row + 1).getSolutionCount();
        }
        
        if (getPeg(col + 1, row + 1) != null)
        {
            //count position to the south east
            count += getPeg(col + 1, row + 1).getSolutionCount();
        }
        
        if (getPeg(col + 1, row) != null)
        {
            //count position to the east
            count += getPeg(col + 1, row).getSolutionCount();
        }
        
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