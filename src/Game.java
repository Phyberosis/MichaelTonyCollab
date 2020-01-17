

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8499272734587456621L;
	
	public static final int WIDTH = 640, HEIGHT = WIDTH / 12*9; //12 by 9 aspect ratio
	
	private Thread gameThread;			// the game process -> separate from base thread that runs the window etc
	private boolean running = false;
	
	//private Handler handler;			//stand in game engine type class
	//private boolean doneRendering = true;
	
	//private FrameHandler fh;
	
	public Game()
	{
		new Window(WIDTH, HEIGHT, "Wonhu Invaders", this);
		
		/**
		 * NOTHING ELSE CAN GO HERE, PUT IN SETUP INSTEAD
		 */
		
		/*
		fh = new FrameHandler(this);
		fh.start();*/
	}
	
	private void setup()
	{
		//handler = new Handler();
		
		//handler.addObj(new Player(100, 100, ID.Player));
	}
	
	public static void main(String args[])
	{
		new Game();
	}
	
	//begin game
	public synchronized void start()
	{
		setup();
		
		gameThread = new Thread(this);
		gameThread.start();
		
		running = true;
	}
	
	//end game
	public synchronized void stop()
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
	
	private void tick(Double dt)
	{
		//handler.tick(dt);
	}
	
	private void render()
	{
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null)
		{
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0,  0,  WIDTH,  HEIGHT);
		
		//handler.render(g);
		
		g.dispose();
		bs.show();
	}
	
	@Override
	public void run()
	{
		long lastTime = System.nanoTime();
		long now;
		long lastFrame = System.currentTimeMillis();
		long dt = 0;
		int frames = 0;
		double dSec = 0;
		
		while(running)
		{
			now = System.nanoTime();
			dt = now - lastTime;
			lastTime = now;
			
			dSec = dt/1000000000.0;
			tick(dSec);		// computation update cycle for game mechanics
			
			if(running)
			{
				render();	// graphics update cycle
				frames++;
			}
			

			if(System.currentTimeMillis() - lastFrame > 1000) // one second has passed
			{
				lastFrame += 1000;
				System.out.println("FPS: " + frames);
				System.out.println("Last tick time: " + dSec + "s");
				frames = 0;
			}
			
		}
		stop();
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
