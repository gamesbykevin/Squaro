package com.gamesbykevin.squaro.menu.layer;

import com.gamesbykevin.framework.menu.Layer;
import com.gamesbykevin.framework.menu.Option;

import com.gamesbykevin.squaro.engine.Engine;
import com.gamesbykevin.squaro.menu.CustomMenu;

public class NewGameConfirm extends Layer implements LayerRules
{
    public NewGameConfirm(final Engine engine) throws Exception
    {
        super(Layer.Type.NONE, engine.getMain().getScreen());
        
        super.setTitle("Confirm New");
        super.setForce(false);
        super.setPause(true);
        super.setOptionContainerRatio(RATIO);
        setup(engine);
    }
    
    @Override
    public void setup(final Engine engine) throws Exception
    {
        //setup options here
        Option tmp;
        
        tmp = new Option(CustomMenu.LayerKey.NewGameConfirmed);
        tmp.add("Yes", null);
        super.add(CustomMenu.OptionKey.NewGameConfim, tmp);
        
        tmp = new Option(CustomMenu.LayerKey.StartGame);
        tmp.add("No", null);
        super.add(CustomMenu.OptionKey.NewGameDeny, tmp);
    }
}