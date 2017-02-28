package mechanics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import gui.Base;
import gui.Menu;
import gui.State;

public class GameEngine {

	private Base b;
	private State gameState;

	public GameEngine(Base b)
	{
		this.b = b;
		gameState = new Menu(b);
	}

	
	public void toMenu()
	{
		gameState = new Menu(b);
	}

	public void tick(double dt)
	{
		gameState.tick(dt);

	}

	public void render(Graphics g)
	{

		g.setColor(Color.black);
		g.fillRect(0,  0,  b.WIDTH,  b.HEIGHT);

		gameState.render(g);

	}

	public void mouseDown(MouseEvent e)
	{
		switch(gameState.id)
		{
		case MENU:
			((Menu)gameState).mouseDown(e);
		}
	}

	public void mouseUp(MouseEvent e)
	{
		switch(gameState.id)
		{
		case MENU:
			((Menu)gameState).mouseUp(e);
		}
	}
}
