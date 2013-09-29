package com.gamesbykevin.squaro.menu.layer;

import com.gamesbykevin.framework.menu.Layer;
import com.gamesbykevin.framework.menu.Option;
import com.gamesbykevin.framework.util.Timer;
import com.gamesbykevin.framework.util.TimerCollection;

import com.gamesbykevin.squaro.board.Board.Difficulty;
import com.gamesbykevin.squaro.engine.Engine;
import com.gamesbykevin.squaro.resource.Resources;
import com.gamesbykevin.squaro.menu.CustomMenu.*;

public class Options extends Layer implements LayerRules
{
    public Options(final Engine engine) throws Exception
    {
        super(Layer.Type.NONE, engine.getMain().getScreen());
        
        setTitle("Options");
        setImage(engine.getResources().getMenuImage(Resources.MenuImage.TitleBackground));
        setTimer(new Timer(TimerCollection.toNanoSeconds(5000L)));
        setForce(false);
        setPause(true);
        setOptionContainerRatio(RATIO);
        setup(engine);
    }
    
    @Override
    public void setup(final Engine engine) throws Exception
    {
        //setup options here
        Option tmp;
            
        tmp = new Option("Mode: ");
        tmp.add("1 Player", engine.getResources().getMenuAudio(Resources.MenuAudio.OptionChange));
        tmp.add("Vs.", engine.getResources().getMenuAudio(Resources.MenuAudio.OptionChange));
        super.add(OptionKey.Mode, tmp);
        
        tmp = new Option("Dimensions: ");
        for (int size=3; size <= 9; size++)
        {
            tmp.add(Integer.toString(size), engine.getResources().getMenuAudio(Resources.MenuAudio.OptionChange));
        }
        super.add(OptionKey.Dimensions, tmp);
        
        tmp = new Option("Dimensions: ");
        for (int size=3; size <= 9; size++)
        {
            tmp.add(Integer.toString(size), engine.getResources().getMenuAudio(Resources.MenuAudio.OptionChange));
        }
        super.add(OptionKey.Dimensions, tmp);
        
        tmp = new Option("Board Difficulty: ");
        for (Difficulty difficulty : Difficulty.values())
        {
            tmp.add(difficulty.toString(), engine.getResources().getMenuAudio(Resources.MenuAudio.OptionChange));
        }
        super.add(OptionKey.BoardDifficulty, tmp);
        
        tmp = new Option("Cpu Difficulty: ");
        for (Difficulty difficulty : Difficulty.values())
        {
            tmp.add(difficulty.toString(), engine.getResources().getMenuAudio(Resources.MenuAudio.OptionChange));
        }
        super.add(OptionKey.CpuDifficulty, tmp);
        
        
        tmp = new Option("Sound: ");
        for (Toggle toggle : Toggle.values())
        {
            tmp.add(toggle.toString(), engine.getResources().getMenuAudio(Resources.MenuAudio.OptionChange));
        }
        //super.add(OptionKey.Sound, tmp);
        
        tmp = new Option("FullScreen: ");
        for (Toggle toggle : Toggle.values())
        {
            tmp.add(toggle.toString(), engine.getResources().getMenuAudio(Resources.MenuAudio.OptionChange));
        }
        super.add(OptionKey.FullScreen, tmp);
        
        tmp = new Option(LayerKey.MainTitle);
        tmp.add("Go Back", null);
        super.add(OptionKey.GoBack, tmp);
    }
}