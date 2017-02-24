package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Interface extends JFrame implements ActionListener, KeyListener
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7983419209764773717L;
	
	final int WIDTH = 700;
	final int HEIGHT = 500;
	
	private GameState state;
	
	Interface()
	{
		super("Wonhu Invaders");
		setSize(WIDTH,HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		toGame();
		
		/*JPanel p = new JPanel(new GridLayout(2, 1, 20, 20));
		Menu m = new Menu(this, p, WIDTH, HEIGHT);
		p.setBorder(new EmptyBorder(WIDTH/4,50,50,50));
		m.add(p);
		this.add(m);
		
		state = GameState.MENU;*/
		
		setVisible(true);
	}

	private void toGame()
	{
		this.removeAll();
		
		GameScreen gs = new GameScreen();
		this.add(gs);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		
		switch(state)
		{
		case MENU:
			JButton source = (JButton)e.getSource();
			if(source.getText().equals("Play"))
			{
				toGame();
			}
			break;
		case GAME:
			break;
		}

		
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		// TODO Auto-generated method stub
		
	}
}