package gui;

import java.awt.*;
import java.util.LinkedList;

import javax.swing.JPanel;

import entities.Entity;

public class GameScreen extends JPanel
{
	
	/**
	 * this was required by eclipse, dunno y
	 */
	private static final long serialVersionUID = 8467739566752896272L;
	private final Color BG= Color.BLACK;	//background color
	
	private LinkedList<Entity> entities;	//holds references to objects to render
	
	GameScreen()
	{
		entities = new LinkedList<>();
	}
	
	public void addToScreen(Entity e)
	{
		entities.add(e);
	}
	
	public void removeFromScreen(Entity e)
	{
		entities.remove(e);
	}
	
	// updates all entities' graphics, must be public -> default
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		//this.setBackground(BG);
		
		//debug test
		g.setColor(Color.red);
		g.fillRect(25,25,150,250);
		
		/*for(Entity o : entities) //for each entity in list, draw it
		{
			g.drawImage(o.getImg(), (int)o.getX(), (int)o.getY(), null);
		}*/
		
		g.dispose();
    }
	
}
