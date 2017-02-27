package gui;
import java.awt.Button;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import language.*;
import memory.*;

public class Interface extends JFrame implements KeyListener, ActionListener{

	public static boolean blnMimicing = false;
	
	private static final long serialVersionUID = 2007645652554426099L;
	private static String[] quote = new String[2]; //string and timestamp
	private final Button btnClose, btnConfused;
	
	JPanel p=new JPanel();
	static JTextArea dialog=new JTextArea(24,60);
	static JTextArea input=new JTextArea(1,60);
	public static JLabel label = new JLabel();

	public static JLabel label2 = new JLabel();
	JScrollPane scroll=new JScrollPane(
		dialog,
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
	);
	
	private Log log;
	private Parser parser;
	private Librarian librarian;
	
	//makes window
	public Interface(){
		super("Brain");
		setSize(700,500);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		btnClose = new Button("close");
		btnConfused = new Button("I'm confused");
	
		p.add(scroll);
		label.setText("       Speak to me                                           ");
		label2.setText("*");
		p.add(label);
		p.add(label2);
		p.add(input);
		p.add(btnClose);
		p.add(btnConfused);
		DefaultCaret caret = (DefaultCaret) dialog.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		p.setBackground(new Color(220,220,220));
		getContentPane().add(p);
		
		dialog.setEditable(false);
		input.addKeyListener(this);
		btnClose.addActionListener(this);
		btnConfused.addActionListener(this);
		
		parser = new Parser();
		log = new Log();
		librarian = new Librarian();
		
		Node n = librarian.getStoreString("test");
		System.out.println(n.info);
		
		setVisible(true);
		
		input.requestFocus();
		
		//addTextM("memBnk location:\n" + memBnk.getLoc());
		//addTextM("\nchange by adding to input then press \"Home\"\n");
	}
	
	public static void main(String args[]) throws IOException{
		
		new Interface();
	}
	
	// adds to text area
	public static void addText(String str) {
		dialog.setText(dialog.getText()+str);
	}
	
	public static void respond(String str){
		dialog.setText(dialog.getText()+"\n--->Me: " + str);
	}
	
	public static String getquote(){
		return quote[0];
	}
	
	public void keyPressed(KeyEvent e) {

		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			input.setEditable(false);
			librarian.getStoreString(input.getText()).info = input.getText() + " apparent info";
			
			input.setText("");
			/*input.setEditable(false);
			LinkedList<String> text = parser.getSentences(input.getText());
			
			for(String s : text)
			{
				log.println(s);
			}*/
		}else if(e.getKeyCode() == KeyEvent.VK_CAPS_LOCK)
		{
			log.println(librarian.getStoreString(input.getText()).info);
		}
	}
	
	public void keyTyped(KeyEvent e){}

	public void keyReleased(KeyEvent e){
		if(e.getKeyCode()==KeyEvent.VK_ENTER){
			input.setEditable(true);
		}
	}
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == btnConfused) {
			Node n = librarian.getStoreString("test");
			n.info = "this is a test";
		} else {
			
			librarian.save();
			System.exit(0);
		}
	}
}
