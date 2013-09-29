package com.gamesbykevin.squaro.player;

import com.gamesbykevin.squaro.engine.Engine;

import java.awt.Graphics;

/**
 * Interface for Player
 * @author GOD
 */
public interface IPlayer 
{
    public void update(final Engine engine) throws Exception;
    
    public void render(final Graphics graphics);
}