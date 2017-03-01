package mechanics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Scanner;

import gui.Base;
import gui.Game;
import gui.Menu;
import gui.State;

public class GameEngine {

	private Base b;
	private State gameState;

	//debug section
	private final Font font = new Font("TimesRoman", Font.PLAIN, 12);
	private String debugInfo = "";
	private long lastUpdate;

	public GameEngine(Base b)
	{
		this.b = b;
		gameState = new Menu(b);
		lastUpdate = System.currentTimeMillis();
	}

	public void toMenu()
	{
		gameState = new Menu(b);
	}

	public void toGame()
	{
		gameState = new Game(b);
	}

	public void tick(double dt)
	{
		gameState.tick(dt);

		//debug
		if(System.currentTimeMillis() - lastUpdate > 150)
		{
			debugInfo = b.getDebugInfo() + "\n"
					+ gameState.getDebugInfo();
			lastUpdate = System.currentTimeMillis();
		}
	}

	public void render(Graphics g)
	{

		//bg
		g.setColor(Color.black);
		g.fillRect(0,  0,  b.WIDTH,  b.HEIGHT);

		gameState.render(g);

		//debug

		g.setColor(Color.red);
		g.setFont(font);
		Scanner s = new Scanner(debugInfo);
		int line = 12;
		while(s.hasNext())
		{
			g.drawString(s.nextLine(), 0, line);
			line += 10;

		}
		s.close();

	}

	public void mouseDown(MouseEvent e)
	{
		switch(gameState.id)
		{
		case MENU:
			((Menu)gameState).mouseDown(e);
			break;
		case GAME:

		}
	}

	public void mouseUp(MouseEvent e)
	{
		//System.out.println(gameState.id);
		switch(gameState.id)
		{
		case MENU:
			String returnMsg = ((Menu)gameState).mouseUp(e);
			if (returnMsg == null)
			{
				return;

			}else if (returnMsg.equals("toGame"))
			{
				toGame();
			}
			break;
		case GAME:

		}

		b.requestFocus();
	}

	public void keyDown(KeyEvent e)
	{
		//System.out.println(e.getKeyChar() + " ge");
		switch(gameState.id)
		{
		case MENU:
			break;
		case GAME:
			((Game)gameState).keyDown(e);
		}
	}

	public void keyUp(KeyEvent e)
	{
		switch(gameState.id)
		{
		case MENU:
			break;
		case GAME:
			((Game)gameState).keyUp(e);
		}
	}
}
