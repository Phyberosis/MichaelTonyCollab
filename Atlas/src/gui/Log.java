package gui;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import runnables.Typer;

public class Log extends JFrame{

	private static final long serialVersionUID = -7349031396240791631L;
	JPanel p = new JPanel();
	static JTextArea log;
	JScrollPane scroll;
	
	private static Typer lgr;
	//private static LogStream logOut;
	
	public Log(){
		super("Atlas - log");
		setSize(568,520);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		log  = new JTextArea(30,50);
		log.setLineWrap(true);
		log.setWrapStyleWord(true);
		
		scroll = new JScrollPane(
				log,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
			);
		
//		logOut = new LogStream(log);
//		PrintStream ps = new PrintStream(logOut);
//		System.setOut(ps);
		
		p.add(scroll);
		DefaultCaret caret = (DefaultCaret) log.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		p.setBackground(new Color(220,220,220));
		getContentPane().add(p);

		lgr = new Typer(log);
		lgr.begin();
		lgr.addDelay(500);
		println("Log active", true);
		
		log.setEditable(false);
		setVisible(true);
	}
	
	public static void println(String str, boolean breakup) {

		synchronized(lgr)
		{
			lgr.addMsg(str, breakup);
		}

	}
	
	public static void immediateMsg(String str) {

		// dumps all msgs in queue before adding text
		synchronized(lgr)
		{
			//System.out.print(lgr.dumpMsgs() + str);
		log.setText(log.getText() + lgr.dumpMsgs() + str);
		}
	}
}
