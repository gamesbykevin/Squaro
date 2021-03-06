package com.gamesbykevin.squaro.menu.layer;

import com.gamesbykevin.framework.menu.Layer;
import com.gamesbykevin.squaro.engine.Engine;
import com.gamesbykevin.squaro.resource.Resources;
import com.gamesbykevin.squaro.menu.CustomMenu;

public class Instructions1 extends Layer implements LayerRules
{
    public Instructions1(final Engine engine)
    {
        super(Layer.Type.NONE, engine.getMain().getScreen());
        
        setImage(engine.getResources().getMenuImage(Resources.MenuImage.Instructions1));
        setNextLayer(CustomMenu.LayerKey.MainTitle);
        setForce(false);
        setPause(true);
        setTimer(null);
        
        setup(engine);
    }
    
    @Override
    public void setup(final Engine engine)
    {
        //no options here to setup
    }
}