package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Interface extends JFrame implements ActionListener, KeyListener
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7983419209764773717L;
	
	final int WIDTH = 700;
	final int HEIGHT = 500;
	
	private GameState state;	//check enum
	private JPanel currentPanel;//reference to current panel in even of game state change
	
	Interface()
	{
		super("Wonhu Invaders");
		setSize(WIDTH,HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		setVisible(true);

		toMenu();
	}

	private void toGame()
	{
	
		GameScreen gs = new GameScreen(WIDTH, HEIGHT);
		this.add(gs);
		
		currentPanel = gs;
		state = GameState.GAME;
	}
	
	private void toMenu()
	{

		
		JPanel p = new JPanel(new GridLayout(2, 1, 20, 20));
		Menu m = new Menu(this, p, WIDTH, HEIGHT);
		p.setBorder(new EmptyBorder(WIDTH/4,50,50,50));
		m.add(p);
		this.add(m);
		
		currentPanel = m;
		state = GameState.MENU;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		
		switch(state)
		{
		case MENU:
			JButton source = (JButton)e.getSource();
			if(source.getText().equals("Play"))
			{
				this.remove(currentPanel);
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