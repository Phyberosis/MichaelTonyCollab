package gui;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class Log extends JFrame{

	private static final long serialVersionUID = -7349031396240791631L;
	JPanel p = new JPanel();
	static JTextArea log = new JTextArea(30,50);
	JScrollPane scroll = new JScrollPane(
			log,
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
		);
	
	public Log(){
		super("Engine - log");
		setSize(568,520);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		p.add(scroll);
		DefaultCaret caret = (DefaultCaret) log.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		p.setBackground(new Color(220,220,220));
		getContentPane().add(p);
		
		log.setText("Log active");
		log.setEditable(false);
		setVisible(true);
	}
	
	public synchronized void println(String str) {
		
		if(str.length() > 90)
		{
			log.setText(log.getText() + "\n" + str.substring(0, 90)); //first line
			
			int end = str.length() - 1;
			int sectionEnd = 90 + 82;
			while(sectionEnd < end)						// all lines after start with spaces and a ">"
			{
				log.setText(log.getText() + "\n   >" + str.substring(sectionEnd - 82, sectionEnd));
				sectionEnd += 82;
			}
			log.setText(log.getText() + "\n   >" + str.substring(sectionEnd - 82)); //last line
		}else{
			log.setText(log.getText() + "\n" + str);
		}
	}
	
	public static void addText(String str) {
		log.setText(log.getText() + str);
	}
}
