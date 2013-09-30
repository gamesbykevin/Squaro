package com.gamesbykevin.squaro.manager;

import com.gamesbykevin.framework.resources.Disposable;
import com.gamesbykevin.framework.util.Timer;
import com.gamesbykevin.framework.util.TimerCollection;

import com.gamesbykevin.squaro.board.Board.Difficulty;
import com.gamesbykevin.squaro.engine.Engine;
import com.gamesbykevin.squaro.menu.CustomMenu.LayerKey;
import com.gamesbykevin.squaro.menu.CustomMenu.OptionKey;

import com.gamesbykevin.squaro.player.*;
import java.awt.Color;

import java.awt.Graphics;
import java.awt.Rectangle;
/**
 * The parent class that contains all of the game elements
 * @author GOD
 */
public final class Manager implements Disposable
{
    //human 
    private Human human;
    
    //opponent
    private Agent agent;
    
    //the size of the board
    private int dimension = 3;
    
    //the difficulty level of the board
    private final Difficulty difficultyBoard;
    
    //the difficulty level of the cpu
    private final Difficulty difficultyCpu;
    
    //the different time delay depening on the difficulty level of the board and cpu
    private final long BOARD_EASY_CPU_EASY = TimerCollection.toNanoSeconds(10000L);
    private final long BOARD_EASY_CPU_MEDIUM = TimerCollection.toNanoSeconds(5000L);
    private final long BOARD_EASY_CPU_HARD = TimerCollection.toNanoSeconds(2000L);
    
    //the different time delay depening on the difficulty level of the board and cpu
    private final long BOARD_MEDIUM_CPU_EASY = TimerCollection.toNanoSeconds(25000L);
    private final long BOARD_MEDIUM_CPU_MEDIUM = TimerCollection.toNanoSeconds(6500L);
    private final long BOARD_MEDIUM_CPU_HARD = TimerCollection.toNanoSeconds(4000L);
    
    //the different time delay depening on the difficulty level of the board and cpu
    private final long BOARD_HARD_CPU_EASY = TimerCollection.toNanoSeconds(45000L);
    private final long BOARD_HARD_CPU_MEDIUM = TimerCollection.toNanoSeconds(15000L);
    private final long BOARD_HARD_CPU_HARD = TimerCollection.toNanoSeconds(7500L);
    
    private enum Mode
    {
        Single, Multiple
    }
    
    //the timer to countdown the next level
    private Timer timer;
    
    //delay until next level
    private final long NEXT_LEVEL_DELAY = TimerCollection.toNanoSeconds(5000L);
    
    //has the game ended, and did the human win
    private boolean gameOver, win;
    
    /**
     * Constructor for Manager, this is the point where we load any menu option configurations
     * @param engine
     * @throws Exception 
     */
    public Manager(final Engine engine) throws Exception
    {
        //create new timer
        this.timer = new Timer(NEXT_LEVEL_DELAY);
        
        //dimension size
        this.dimension = engine.getMenu().getOptionSelectionIndex(LayerKey.Options, OptionKey.Dimensions) + 3;
        
        //difficulty levels
        this.difficultyBoard = Difficulty.values()[engine.getMenu().getOptionSelectionIndex(LayerKey.Options, OptionKey.BoardDifficulty)];
        this.difficultyCpu = Difficulty.values()[engine.getMenu().getOptionSelectionIndex(LayerKey.Options, OptionKey.CpuDifficulty)];
        
        final int startX;
        
        //single player mode
        if (Mode.values()[engine.getMenu().getOptionSelectionIndex(LayerKey.Options, OptionKey.Mode)] == Mode.Single)
        {
            startX = 169;
        }
        else
        {
            //multi player mode
            startX = 12;
            
            switch(difficultyBoard)
            {
                case Easy:
                    switch(difficultyCpu)
                    {
                        case Easy:
                            agent = new Agent(BOARD_EASY_CPU_EASY);
                            break;

                        case Medium:
                            agent = new Agent(BOARD_EASY_CPU_MEDIUM);
                            break;

                        case Hard:
                            agent = new Agent(BOARD_EASY_CPU_HARD);
                            break;
                    }
                    break;
                    
                case Medium:
                    switch(difficultyCpu)
                    {
                        case Easy:
                            agent = new Agent(BOARD_MEDIUM_CPU_EASY);
                            break;

                        case Medium:
                            agent = new Agent(BOARD_MEDIUM_CPU_MEDIUM);
                            break;

                        case Hard:
                            agent = new Agent(BOARD_MEDIUM_CPU_HARD);
                            break;
                    }
                    break;
                    
                case Hard:
                    switch(difficultyCpu)
                    {
                        case Easy:
                            agent = new Agent(BOARD_HARD_CPU_EASY);
                            break;

                        case Medium:
                            agent = new Agent(BOARD_HARD_CPU_MEDIUM);
                            break;

                        case Hard:
                            agent = new Agent(BOARD_HARD_CPU_HARD);
                            break;
                    }
                    break;
            }
            
            setupPlayer(agent, new Rectangle(337, 0, 312, 400));
        }
        
        human = new Human();
        setupPlayer(human, new Rectangle(startX, 0, 312, 400));
    }
    
    private void setupPlayer(final Player player, final Rectangle area)
    {
        //setup board
        setupNextLevel(player);
        
        //set the location of the player
        player.setLocation(area.x, area.y);
        
        //set the size of the player window
        player.setDimensions(area.width, area.height);
    }
    
    private void setupNextLevel(final Player player)
    {
        //create a board with the specific dimension and difficulty
        player.createBoard(dimension, difficultyBoard);
    }
    
    /**
     * Free up resources
     */
    @Override
    public void dispose()
    {
        this.timer = null;
        
        if (human != null)
            human.dispose();
        
        human = null;
        
        if (agent != null)
            agent.dispose();
        
        agent = null;
    }
    
    /**
     * Update all application elements
     * 
     * @param engine Our main game engine
     * @throws Exception 
     */
    public void update(final Engine engine) throws Exception
    {
        if (!gameOver)
        {
            if (human != null)
                human.update(engine);

            if (agent != null)
                agent.update(engine);
            
            //if the opponent exists
            if (agent != null)
            {
                //if either has solved then the game is over
                gameOver = (agent.hasSolved() || human.hasSolved());

                //there is a win if the human solved the board
                win = (human.hasSolved());
            }
            else
            {
                //if solved, then game over
                gameOver = (human.hasSolved());
                
                //human has won
                win = true;
            }
            
            if (gameOver)
                timer.reset();
        }
        
        //if the game is over update timer till next board
        if (gameOver)
        {
            timer.update(engine.getMain().getTime());
        
            if (timer.hasTimePassed())
            {
                timer.setRemaining(0);

                //next level is starting so game is no longer over
                gameOver = false;

                //if we are a winner increase the board size
                if (win)
                {
                    dimension++;

                    if (dimension > 9)
                        dimension = 9;
                }

                if (human != null)
                    setupNextLevel(human);

                if (agent != null)
                    setupNextLevel(agent);
            }
        }
    }
    
    /**
     * Draw all of our application elements
     * @param graphics Graphics object used for drawing
     */
    public void render(final Graphics graphics)
    {
        if (human != null)
        {
            human.render(graphics);
        }
        
        if (agent != null)
        {
            agent.render(graphics);
        }
        
        if (gameOver)
        {
            graphics.setColor(Color.WHITE);
            graphics.setFont(graphics.getFont().deriveFont(12f));
            
            if (human != null)
            {
                if (win)
                    graphics.drawString("You win, next in " + timer.getDescRemaining(TimerCollection.FORMAT_7), (int)human.getX(), (int)human.getY() + 30);
                else
                    graphics.drawString("You lose, next in " + timer.getDescRemaining(TimerCollection.FORMAT_7), (int)human.getX(), (int)human.getY() + 30);
            }
            
            if (agent != null)
            {
                if (!win)
                    graphics.drawString("I win, next in " + timer.getDescRemaining(TimerCollection.FORMAT_7), (int)agent.getX(), (int)agent.getY() + 30);
                else
                    graphics.drawString("I lose, next in " + timer.getDescRemaining(TimerCollection.FORMAT_7), (int)agent.getX(), (int)agent.getY() + 30);
            }
        }
    }
}