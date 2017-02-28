package gui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import entities.EntID;
import entities.Entity;
import entities.RoundButton;

public class Menu extends State
{
	private Base b;

	public Menu(Base b)
	{
		super(StateID.MENU);

		this.b = b;

		entities.add(new RoundButton("Play", b.WIDTH /2, b.HEIGHT /3 + 125, 125));
		entities.add(new RoundButton("Exit", b.WIDTH /2, b.HEIGHT *2 /3 + 100, 75));
	}

	public void mouseDown(MouseEvent e)
	{

		for(Entity ent : entities)
		{

			if(ent.imageIndex == ((RoundButton)ent).MOUSEON)
			{
				((RoundButton)ent).imageIndex = ((RoundButton)ent).MOUSECLICKED;
			}
		}
	}

	public void mouseUp(MouseEvent e)
	{
		for(Entity ent : entities)
		{
			if(ent.imageIndex == ((RoundButton)ent).MOUSECLICKED)
			{
				if(((RoundButton)ent).text.equals("Exit"))
				{
					System.exit(0);
				}
			}
		}
	}

	@Override
	public void tick(double dt)
	{
		//updates all objects
		for (Entity ent : entities)
		{
			if(ent.ID.equals(EntID.ROUNDBUTTON))
			{
				((RoundButton)ent).updateWindowPosition(b.getLocationOnScreen());

				ent.tick(dt);
			}
		}
	}

	@Override
	public void render(Graphics g)
	{
		//render all objects
		for (Entity ent : entities)
		{
			ent.render(g);
		}

	}

}
