package com.gamesbykevin.squaro.manager;

import com.gamesbykevin.framework.resources.Disposable;
import com.gamesbykevin.framework.display.WindowHelper;
import com.gamesbykevin.framework.util.Timer;
import com.gamesbykevin.framework.util.TimerCollection;

import com.gamesbykevin.squaro.board.Board;

import com.gamesbykevin.squaro.engine.Engine;
import com.gamesbykevin.squaro.menu.CustomMenu.LayerKey;
import com.gamesbykevin.squaro.menu.CustomMenu.OptionKey;
import com.gamesbykevin.squaro.resource.Resources.GameImage;

import com.gamesbykevin.squaro.player.*;


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
    private Human human;
    private Agent agent;
    
    //the size of the board
    private int dimension = 5;
    
    private final int difficulty;
    /**
     * Constructor for Manager, this is the point where we load any menu option configurations
     * @param engine
     * @throws Exception 
     */
    public Manager(final Engine engine) throws Exception
    {
        //this.musicSelection = engine.getMenu().getOptionSelectionIndex(LayerKey.Options, OptionKey.Music);
        //final Image image = engine.getResources().getGameImage(GameImage.Spritesheet);
        
        this.dimension = engine.getMenu().getOptionSelectionIndex(LayerKey.Options, OptionKey.Dimensions) + 3;
        this.difficulty = engine.getMenu().getOptionSelectionIndex(LayerKey.Options, OptionKey.Difficulty);
        
        //human = new Human();
        
        //setupPlayer(human, new Rectangle(12, 0, 312, 400));
        
        agent = new Agent();
        
        setupPlayer(agent, new Rectangle(337, 0, 312, 400));
    }
    
    private void setupPlayer(final Player player, final Rectangle area)
    {
        //create a board with the specific dimension and difficulty
        player.createBoard(dimension, difficulty);
        
        //set the location of the player
        player.setLocation(area.x, area.y);
        
        //set the size of the player window
        player.setDimensions(area.width, area.height);
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
        if (human != null)
            human.update(engine);
        
        if (agent != null)
            agent.update(engine);
    }
    
    /**
     * Draw all of our application elements
     * @param graphics Graphics object used for drawing
     */
    public void render(final Graphics graphics)
    {
        if (human != null)
            human.render(graphics);
        
        if (agent != null)
            agent.render(graphics);
    }
}