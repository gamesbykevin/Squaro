package com.gamesbykevin.squaro.manager;

import com.gamesbykevin.framework.resources.Disposable;
import com.gamesbykevin.framework.display.WindowHelper;

import com.gamesbykevin.squaro.board.Board;

import com.gamesbykevin.squaro.engine.Engine;
import com.gamesbykevin.squaro.menu.CustomMenu.LayerKey;
import com.gamesbykevin.squaro.menu.CustomMenu.OptionKey;
import com.gamesbykevin.squaro.resource.Resources.GameImage;
import com.gamesbykevin.framework.util.TimerCollection;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
/**
 * The parent class that contains all of the game elements
 * @author GOD
 */
public final class Manager implements Disposable
{
    private Board board;
    
    /**
     * Constructor for Manager, this is the point where we load any menu option configurations
     * @param engine
     * @throws Exception 
     */
    public Manager(final Engine engine) throws Exception
    {
        //this.musicSelection = engine.getMenu().getOptionSelectionIndex(LayerKey.Options, OptionKey.Music);
        //final Image image = engine.getResources().getGameImage(GameImage.Spritesheet);
        
        final int difficultyIndex = engine.getMenu().getOptionSelectionIndex(LayerKey.Options, OptionKey.Difficulty);
        
        //create a new board with the specified size and dimension
        board = new Board(5, new Dimension(400, 400), difficultyIndex);
        
        //set the location of the board
        board.setLocation(0, 0);
    }
    
    /**
     * Free up resources
     */
    @Override
    public void dispose()
    {
        
    }
    
    /**
     * Update all application elements
     * 
     * @param engine Our main game engine
     * @throws Exception 
     */
    public void update(final Engine engine) throws Exception
    {
        board.update(engine);
    }
    
    /**
     * Draw all of our application elements
     * @param graphics Graphics object used for drawing
     */
    public void render(final Graphics graphics)
    {
        board.render(graphics);
    }
}