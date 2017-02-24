package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JPanel;

import entities.Entity;

public class GameScreen extends JPanel implements ActionListener
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7794823291817903400L;
	private final Color BG= Color.BLACK;	//background color
	
	private LinkedList<Entity> entities;	//holds references to objects to render
	
	GameScreen(int w, int h)
	{
		entities = new LinkedList<>();
		
		setSize(w, h);
		this.setVisible(true);
		
		this.setBackground(Color.black);

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
	public void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		//this.setBackground(BG);
		
		//debug test
		g.setColor(Color.red);
		g.fillRect(25,25,150,250);
		
		/*for(Entity o : entities) //for each entity in list, draw it
		{
			g.drawImage(o.getImg(), (int)o.getX(), (int)o.getY(), null);
		}*/
		
		//t.start();
		System.out.println("here");
    }

	@Override
	public void actionPerformed(ActionEvent e)
	{
		//repaint();
	}
	
}
