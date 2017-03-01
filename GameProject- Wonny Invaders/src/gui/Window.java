package gui;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Window extends Canvas
{

	/**
	 *  a bare bones window
	 */
	private static final long serialVersionUID = -4810618286807932601L;
	private static JFrame f;
	
	public Window(int w, int h, String title, Base b)
	{
		f = new JFrame(title);
		
		Dimension d = new Dimension(w, h);
		f.setPreferredSize(d);
		f.setMaximumSize(d);
		f.setMinimumSize(d);
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.setLocationRelativeTo(null);
		f.add(b);
		f.setVisible(true);
	}
	
	public JFrame getJFrame()
	{
		return f;
	}
	
	public void exit()
	{
		System.exit(0);
	}
	
}
