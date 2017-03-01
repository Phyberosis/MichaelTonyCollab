package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;

import entities.Player;

public class Game extends State
{
	private final int PLRHEALTH = 1000;

	private Base b;
	private Player plr;	//kept separate from entities
	private final double ARATE;

	//debug
	private String debugInfo;
	
	/**
	 * update as necessary
	 * 0 - 3, w a s d
	 */
	private boolean[] keystates;

	public Game(Base b)
	{
		super(StateID.GAME);

		this.b = b;

		plr = new Player(b.WIDTH/2, b.HEIGHT/2, PLRHEALTH, b.WIDTH, b.HEIGHT - 25);
		keystates = new boolean[4];
		ARATE = plr.getARate();
	}

	@Override
	public void tick(double dt)
	{
		
		synchronized(plr)
		{
			//update player
			plr.tick(dt);
		}


		//update other objects?

		//run collision?
	}

	@Override
	public void render(Graphics g)
	{
		synchronized(plr)
		{
			//render player objects, not including projectiles
			plr.render(g, b.getRes());
		}
		//render others?

	}

	public void setDebugInfo(String str)
	{
		setGetDebugInfo(true, str);
	}
	public String getDebugInfo()
	{
		return setGetDebugInfo(false, null);
	}
	// crossthread sync mechanism of debug info
	protected synchronized String setGetDebugInfo(boolean set, String str)
	{
		if(set)
		{
			debugInfo = str;
			return null;
		}else{
			return "\nEntities: " + (entities.size() + 1) + //+ 1 for plr
					"\n\nPlayer:\n" + plr.getDebugInfo() ;
		}
	}
	
	public void keyDown(KeyEvent e)
	{
		/* key events are on separate thread
		 * must sync with game since accessing same plr object
		 * */
		synchronized(plr)
		{
			//sets accel in direction of key
			switch(e.getKeyCode())
			{
			case KeyEvent.VK_W:
				keystates[0] = true;
				plr.setYAccel(-ARATE);
				break;
			case KeyEvent.VK_A:
				keystates[1] = true;
				plr.setXAccel(-ARATE);
				break;
			case KeyEvent.VK_S:
				keystates[2] = true;
				plr.setYAccel(ARATE);
				break;
			case KeyEvent.VK_D:
				keystates[3] = true;
				plr.setXAccel(ARATE);
				break;
			}
		}
	}

	public void keyUp(KeyEvent e)
	{
		/* key events are on separate thread
		 * must sync with game since accessing same plr object
		 * */
		synchronized(plr)
		{
			//resets accel of direction of key
			// returns to previous key cmd if key is still down
			switch(e.getKeyCode())
			{
			case KeyEvent.VK_W:
				keystates[0] = false;
				if(keystates[2])		// s down
				{
					plr.setYAccel(ARATE);
				}else{
					plr.setYAccel(0);;
				}
				break;
			case KeyEvent.VK_A:
				keystates[1] = false;
				if(keystates[3])		// d down
				{
					plr.setXAccel(ARATE);
				}else{
					plr.setXAccel(0);
				}
				break;
			case KeyEvent.VK_S:
				keystates[2] = false;
				if(keystates[0])		// w down
				{
					plr.setYAccel(-ARATE);
				}else{
					plr.setYAccel(0);;
				}
				break;
			case KeyEvent.VK_D:
				keystates[3] = false;
				if(keystates[1])		// a down
				{
					plr.setXAccel(-ARATE);
				}else{
					plr.setXAccel(0);;
				}
				break;
			}
		}
	}

}
