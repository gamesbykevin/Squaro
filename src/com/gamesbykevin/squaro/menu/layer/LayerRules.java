package com.gamesbykevin.squaro.menu.layer;

import com.gamesbykevin.squaro.engine.Engine;

public interface LayerRules 
{
    //default ratio for all the option containers
    public static final float RATIO = .80F;
    
    /**
     * Setup Layer options (if they exist)
     * 
     * @param engine 
     */
    public void setup(final Engine engine) throws Exception;
}