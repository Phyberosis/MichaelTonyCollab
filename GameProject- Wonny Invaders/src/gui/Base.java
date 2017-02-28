package gui;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;

import mechanics.GameEngine;

public class Base extends Canvas implements Runnable, MouseListener
{

	/**
	 * "main" class that holds references to all essential objects
	 * contains game loop
	 */
	private static final long serialVersionUID = 8499272734587456621L;
	
	public final int WIDTH = 960, HEIGHT = WIDTH / 12*9; //12 by 9 aspect ratio
	
	private Thread gameThread;			// the game process -> separate from base thread that runs the window etc
	boolean running;
	
	private GameEngine ge;	
	
	public Base()
	{
		running = false;
		new Window(WIDTH, HEIGHT, "Wonhu Invaders", this);	//this begins the game loop -> calls begin

		/**
		 * NOTHING ELSE CAN GO HERE, PUT IN SETUP INSTEAD
		 */
	}
	
	private void setup()
	{
		ge = new GameEngine(this);
		this.addMouseListener(this);
	}
	
	public static void main(String args[])
	{
		new Base();
	}
	
	//begin game
	public void begin()
	{
		setup();
		
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
		this.createBufferStrategy(3);
		
		while(running)
		{
			now = System.nanoTime();
			dt = now - lastTime;
			lastTime = now;
			
			dSec = dt/1000000000.0;
			ge.tick(dSec);		// computation update cycle for game mechanics
			
			if(running)
			{
				render();		// graphics update cycle
				frames++;
			}

			if(System.currentTimeMillis() - lastFrame > 1000) // one second has passed
			{
				lastFrame += 1000;
				System.out.println("FPS: " + frames);
				System.out.println("Last tick time: " + Math.floor((dSec*1000) * 10000) / 10000 + "ms");
				frames = 0;
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
	
		
	}

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
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
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
