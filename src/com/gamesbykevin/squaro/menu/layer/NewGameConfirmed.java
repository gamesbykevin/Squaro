package com.gamesbykevin.squaro.menu.layer;

import com.gamesbykevin.framework.menu.Layer;
import com.gamesbykevin.squaro.engine.Engine;

public class NewGameConfirmed extends Layer implements LayerRules
{
    public NewGameConfirmed(final Engine engine)
    {
        super(Layer.Type.NONE, engine.getMain().getScreen());
        
        setup(engine);
    }
    
    @Override
    public void setup(final Engine engine)
    {
        //no options here to setup
    }    
}