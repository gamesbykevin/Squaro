package com.gamesbykevin.squaro.menu.layer;

import com.gamesbykevin.framework.menu.Layer;
import com.gamesbykevin.framework.util.Timer;
import com.gamesbykevin.framework.util.TimerCollection;
import com.gamesbykevin.squaro.engine.Engine;
import com.gamesbykevin.squaro.resource.Resources;
import com.gamesbykevin.squaro.menu.CustomMenu;

public class Credits extends Layer implements LayerRules
{
    public Credits(final Engine engine)
    {
        super(Layer.Type.SCROLL_VERTICAL_NORTH, engine.getMain().getScreen());
        
        setImage(engine.getResources().getMenuImage(Resources.MenuImage.Credits));
        setForce(true);
        setPause(false);
        setNextLayer(CustomMenu.LayerKey.MainTitle);
        setTimer(new Timer(TimerCollection.toNanoSeconds(7500L)));
    }
    
    @Override
    public void setup(final Engine engine)
    {
        //no options here to setup
    }
}