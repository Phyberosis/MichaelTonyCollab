package gui;
import java.awt.Button;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import mechanics.Dispatcher;
import runnables.Typer;

public class Interface extends JFrame implements KeyListener, ActionListener{
	
	private static final long serialVersionUID = 2007645652554426099L;
	private static String[] quote = new String[2];
	private final Button btnClose;
	
	private JPanel p=new JPanel();
	private JTextArea dialog=new JTextArea(24,60);
	private JTextArea input=new JTextArea(1,60);
	private JLabel label = new JLabel();

	private JLabel label2 = new JLabel();
	private JScrollPane scroll=new JScrollPane(
		dialog,
		JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
	);
	
	private Typer typer; 
	private Dispatcher mDispatcher;
	
	//makes window
	public Interface(Dispatcher d){
		super("Atlas");
		setSize(700,500);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		btnClose = new Button("close");
	
		typer = new Typer(dialog);
		typer.begin();
		
		new Log();
		mDispatcher = d;
		
		/* not working -> use unity instead
		dis = new Display();
		Display.launch();
		*/
		
		p.add(scroll);
		label.setText("                                                 ");
		label2.setText("*");
		p.add(label);
		p.add(label2);
		p.add(input);
		p.add(btnClose);
		DefaultCaret caret = (DefaultCaret) dialog.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		p.setBackground(new Color(220,220,220));
		getContentPane().add(p);
		
		dialog.setEditable(false);
		input.addKeyListener(this);
		btnClose.addActionListener(this);
		
		setVisible(true);
		Log.println("Interface active", true);
		
		input.requestFocus();
		
		//addTextM("memBnk location:\n" + memBnk.getLoc());
		//addTextM("\nchange by adding to input then press \"Home\"\n");
	}
		
	// adds to text area
	public void addText(String str) {
		dialog.setText(dialog.getText()+str);
	}
	
	public void respond(String str){
		typer.addMsg("@ " + str, true);
		//dialog.setText(dialog.getText()+"@ " + str + "\n");
	}
	
	public static String getquote(){
		return quote[0];
	}
	
	public void voiceInput(String str)
	{
		input.setEditable(false);
		input.setText(str);
		keyPressed(null);
	}
	
	//adds input on ENTER
	public void keyPressed(KeyEvent e) {

		if(e == null || e.getKeyCode()==KeyEvent.VK_ENTER){
			//System.out.println("keypressed: Enter");
			if (!input.getText().equals("")) {
				input.setEditable(false);
				quote[1] = Long.toString(System.currentTimeMillis());
				quote[0]=input.getText();

				input.setText("");
				quote[0] = quote[0].trim();
				addText("> "+quote[0]+"\n");
				
				mDispatcher.newInput(quote);
			}else{
				input.setEditable(false);
				input.setText("");
			}
		}
	}
	
	public void keyTyped(KeyEvent e){}

	public void keyReleased(KeyEvent e){
		if(e.getKeyCode()==KeyEvent.VK_ENTER){
			input.setEditable(true);
		}
	}
	public void actionPerformed(ActionEvent e) {
		
		//neuronHandler.sav();
		//memoryHandler.sav();
		System.exit(0);
	}
}
