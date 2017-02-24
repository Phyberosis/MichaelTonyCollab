package gui;

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Menu extends JPanel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4631518459889002716L;
	private JButton[] buttons;
	
	Menu(Interface i, JPanel p, int w, int h)
	{
		
		buttons = new JButton[2];
		for(int ii = 0; ii < 2; ii++)
		{
			buttons[ii] = new JButton();
			buttons[ii].addActionListener(i);
			buttons[ii].setPreferredSize(new Dimension(w/5, h/7));

			p.add(buttons[ii]);
		}
		
		buttons[0].setText("Play");
		buttons[1].setText("Exit");
	}
}
