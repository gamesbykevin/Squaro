package com.gamesbykevin.squaro.resource;

import com.gamesbykevin.framework.resources.*;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.text.MessageFormat;
import java.util.LinkedHashMap;

/**
 * This class will load all resources in the collection and provide a way to access them
 * @author GOD
 */
public class Resources implements IResources
{   
    //this will contain all resources
    private LinkedHashMap<Object, Manager> everyResource;
    
    //collections of resources
    private enum Type
    {
        MenuImage, MenuAudio, Fonts, GameImage
    }
    
    //root directory of all resources
    public static final String RESOURCE_DIR = "resources/"; 
    
    public enum GameImage
    {
        //number success
        NS0, NS1, NS2, NS3, NS4, NS5, NS6, NS7, NS8, NS9, NS10, NS11, NS12, NS13, NS14, NS15, NS16, 
        
        //number error
        NE0, NE1, NE2, NE3, NE4, NE5, NE6, NE7, NE8, NE9, NE10, NE11, NE12, NE13, NE14, NE15, NE16,  
    }
    
    public enum MenuAudio
    {
        OptionChange
    }
    
    public enum MenuImage
    {
        TitleScreen, Credits, AppletFocus, TitleBackground, Mouse, MouseDrag, 
        Controls1,  
        Instructions1, Instructions2, Instructions3, Instructions4
    }
    
    public enum Fonts
    {
        Menu, Game
    }
    
    //indicates wether or not we are still loading resources
    private boolean loading = true;
    
    public Resources() throws Exception
    {
        everyResource = new LinkedHashMap<>();
        
        //load single sprite sheet
        add(Type.GameImage, (Object[])GameImage.values(), RESOURCE_DIR + "images/game/numbers/{0}.png", "Loading Game Image Resources", Manager.Type.Image);
        
        //load all menu images
        add(Type.MenuImage, (Object[])MenuImage.values(), RESOURCE_DIR + "images/menu/{0}.gif", "Loading Menu Image Resources", Manager.Type.Image);
        
        //load all game fonts
        add(Type.Fonts,  (Object[])Fonts.values(),  RESOURCE_DIR + "font/{0}.ttf", "Loading Font Resources", Manager.Type.Font);
        
        //load all menu audio effects
        add(Type.MenuAudio, (Object[])MenuAudio.values(), RESOURCE_DIR + "audio/menu/sound/{0}.wav", "Loading Menu Audio Resources", Manager.Type.Audio);
    }
    
    //add a collection of resources audio/image/font/text
    private void add(final Object key, final Object[] eachResourceKey, final String directory, final String loadDesc, final Manager.Type resourceType) throws Exception
    {
        String[] locations = new String[eachResourceKey.length];
        for (int i=0; i < locations.length; i++)
        {
            locations[i] = MessageFormat.format(directory, i);
        }

        Manager resources = new Manager(Manager.LoadMethod.OnePerFrame, locations, eachResourceKey, resourceType);
        
        //only set the description once for this specific resource or else an exception will be thrown
        resources.setDescription(loadDesc);
        
        everyResource.put(key, resources);
    }
    
    @Override
    public boolean isLoading()
    {
        return loading;
    }
    
    private Manager getResources(final Object key)
    {
        return everyResource.get(key);
    }
    
    public Font getFont(final Object key)
    {
        return getResources(Type.Fonts).getFont(key);
    }
    
    public Image getGameImage(final Object key)
    {
        return getResources(Type.GameImage).getImage(key);
    }
    
    public Image getMenuImage(final Object key)
    {
        return getResources(Type.MenuImage).getImage(key);
    }
    
    public void playMenuAudio(final Object key, final boolean loop)
    {
        getResources(Type.MenuAudio).playAudio(key, loop);
    }
    
    public Audio getMenuAudio(final Object key)
    {
        return getResources(Type.MenuAudio).getAudio(key);
    }
    
    /**
     * Stop all sound
     */
    public void stopAllSound()
    {
        getResources(Type.MenuAudio).stopAllAudio();
    }
    
    /**
     * Here we will load the resources one by one and then marking the process finished once done
     * @param source Class in root directory of project so we have a relative location so we know how to access resources
     * @throws Exception 
     */
    @Override
    public void update(final Class source) throws Exception
    {
        Object[] keys = everyResource.keySet().toArray();
        
        for (Object key : keys)
        {
            Manager resources = getResources(key);
            
            if (!resources.isComplete())
            {
                //load the resources
                resources.update(source);
                return;
            }
        }
        
        //if this line is reached we are done loading every resource
        loading = false;
    }
    
    /**
     * Checks to see if audio is turned on
     * @return 
     */
    public boolean isAudioEnabled()
    {
        //if the menu audio is not enabled the remaining audio collections should not be as well
        return getResources(Type.MenuAudio).isAudioEnabled();
    }
    
    /**
     * Set the audio enabled.
     * All existing audio collections here will have the audio enabled value set.
     * 
     * @param boolean Is the audio enabled 
     */
    @Override
    public void setAudioEnabled(final boolean enabled)
    {
        getResources(Type.MenuAudio).setAudioEnabled(enabled);
        //all other existing audio collections should be disabled here as well
        
    }
    
    @Override
    public void dispose()
    {
        for (Object key : everyResource.keySet().toArray())
        {
            Manager resources = getResources(key);
            
            if (resources != null)
                resources.dispose();
            
            resources = null;
            
            everyResource.put(key, null);
        }
        
        everyResource.clear();
        everyResource = null;
    }
    
    @Override
    public void render(final Graphics graphics, final Rectangle screen)
    {
        if (!isLoading())
            return;
        
        for (Object key : everyResource.keySet().toArray())
        {
            Manager resources = getResources(key);
            
            //if loading the resources is not complete yet, draw progress
            if (!resources.isComplete())
            {
                //display progress
                resources.render(graphics, screen);
                return;
            }
        }
    }
}