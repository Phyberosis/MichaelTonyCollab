package gui;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;

import mechanics.GameEngine;
import mechanics.Resources;

public class Base extends Canvas implements Runnable, MouseListener, KeyListener
{

	/**
	 * "main" class that holds references to all essential objects
	 * contains game loop
	 */
	private static final long serialVersionUID = 8499272734587456621L;
	
	public final int WIDTH = 960, HEIGHT = WIDTH / 12*9; //12 by 9 aspect ratio
	
	private Thread gameThread;			// the game process -> separate from base thread that runs the window etc
	private boolean running;
	public  Window w;
	
	private GameEngine ge;	
	private Resources res;
	
	private boolean[] keystates;
	
	//debug section
	private String debugFPS;
	private String debugTickTime;
	//end debug
	
	public Base()
	{
		running = false;
		w = new Window(WIDTH, HEIGHT, "Wonhu Invaders", this);	//this begins the game loop -> calls begin
		this.begin();
		this.setFocusable(true);
		
		/**
		 * NOTHING ELSE CAN GO HERE, PUT IN SETUP INSTEAD
		 */
	}
	
	private void setup()
	{
		this.createBufferStrategy(3);
		
		ge = new GameEngine(this);
		this.addMouseListener(this);
		this.addKeyListener(this);
		
		res = new Resources();
		keystates = new boolean[4];
	}
	
	public static void main(String args[])
	{
		new Base();
	}
	
	//begin game
	public void begin()
	{
		
		gameThread = new Thread(this);
		gameThread.start();
		
		running = true;
	}
	
	//end game
	public void end()
	{
		//fh.stop();
		
		try
		{
			gameThread.join();
			running = false;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	private void render()
	{
		BufferStrategy bs = this.getBufferStrategy();
		
		Graphics g = bs.getDrawGraphics();
		
		//g.drawImage(res.getImg(1), 0, 0, null); // debug resource test
		ge.render(g);
		//handler.render(g);
		
		g.dispose();
		bs.show();
	}
	
	//the game loop -> tick and render
	@Override
	public void run()
	{
		long lastTime = System.nanoTime();
		long now;
		long lastFrame = System.currentTimeMillis();
		long dt = 0;
		int frames = 0;
		double dSec = 0;

		setup();
		
		while(running)
		{
			now = System.nanoTime();
			dt = now - lastTime;
			lastTime = now;
			
			dSec = dt/1000000.0;// dt in ms
			ge.tick(dSec);		// computation update cycle for game mechanics
			
			if(running)
			{
				render();		// graphics update cycle
				frames++;
			}

			//debug info
			if(System.currentTimeMillis() - lastFrame > 1000) // one second has passed
			{
				lastFrame += 1000;
				//System.out.println("FPS: " + frames);
				//System.out.println("Last tick time: " + Math.floor((dSec) * 1000) / 1000 + "ms");
				
				setDebugInfo(Integer.toString(frames), Double.toString(Math.floor(dSec * 1000) / 1000));
				
				frames = 0;
			}
			
			//debug -> artificial lag
			//while(System.nanoTime() - lastTime < 20000000){}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{}

	@Override
	public void mousePressed(MouseEvent e)
	{
		ge.mouseDown(e);
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		ge.mouseUp(e);
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{}

	@Override
	public void mouseExited(MouseEvent e)
	{}
	
	@Override
	public void keyTyped(KeyEvent e)
	{}

	@Override
	public void keyPressed(KeyEvent e)
	{
		//this prevents spam from holding key down
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_W:
			if(keystates[0])
			{
				return;
			}else{
				keystates[0] = true;
				break;
			}
		case KeyEvent.VK_A:
			if(keystates[1])
			{
				return;
			}else{
				keystates[1] = true;
				break;
			}
		case KeyEvent.VK_S:
			if(keystates[2])
			{
				return;
			}else{
				keystates[2] = true;
				break;
			}
		case KeyEvent.VK_D:
			if(keystates[3])
			{
				return;
			}else{
				keystates[3] = true;
				break;
			}
		}
		
		ge.keyDown(e);
		
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_W:
			keystates[0] = false;
			break;
		case KeyEvent.VK_A:
			keystates[1] = false;
			break;
		case KeyEvent.VK_S:
			keystates[2] = false;
			break;
		case KeyEvent.VK_D:
			keystates[3] = false;
			break;
		}
		
		ge.keyUp(e);
	}
	
	public synchronized Resources getRes()
	{
		return res;
	}
	
	public void setDebugInfo(String fps, String TickT)
	{
		setGetDebugInfo(true, fps, TickT);
	}
	public String getDebugInfo()
	{
		return setGetDebugInfo(false, null, null);
	}
	// crossthread sync mechanism of debug info
	private synchronized String setGetDebugInfo(boolean set, String fps, String tickT)
	{
		if(set)
		{
			debugFPS = fps;
			debugTickTime = tickT;
			return null;
		}else{
			return 	"FPS:   " + debugFPS + "\n" + 
					"Tick:  " + debugTickTime + "ms";
		}
	}

	
	/*
	//synchronized so that this thread or the rendering thread have to take turns accessing this variable
	public synchronized boolean doneRendering()
	{
		return doneRendering;
	}
	
	public synchronized void setDoneRendering(boolean b)
	{
		doneRendering = b;
	}
	*/
	
	//run
	/*
	long lastTime = System.nanoTime();
	double amountOfTicks = 60;
	double ns = 1000000000 / amountOfTicks;
	double delta = 0;
	long timer = System.currentTimeMillis();
	int frames = 0;
	
	long now;
	while(running)
	{
		now = System.nanoTime();
		delta += (now - lastTime) / ns;
		lastTime = now;
		while(delta > 0)
		{
			tick();
			delta --;
		}
		
		if(running)
			render();
		frames++;
		
		if(System.currentTimeMillis() - timer > 1000)
		{
			timer += 1000;
			System.out.println("FPS: " + frames);
			frames = 0;
		}
	}
	*/
	
	
}
