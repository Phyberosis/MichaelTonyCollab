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

public class Interface extends JFrame implements KeyListener, ActionListener{

	public static boolean blnMimicing = false;
	
	private static final long serialVersionUID = 2007645652554426099L;
	private static String[] quote = new String[2];
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
		
		setVisible(true);
		
		input.requestFocus();
		
		//addTextM("memBnk location:\n" + memBnk.getLoc());
		//addTextM("\nchange by adding to input then press \"Home\"\n");
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
	
	//adds input on ENTER
	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode()==KeyEvent.VK_ENTER){
			System.out.println("keypressed: Enter");
			if (!input.getText().equals("")) {
				
				if (!blnMimicing) {
					input.setEditable(false);
					quote[1] = Long.toString(System.currentTimeMillis());
					quote[0]=input.getText();
					
					taskHandler.taskLst.add(tasks.newInput);
					taskHandler.taskData.add(quote);
					taskHandler.blnPause = false;
				} else {
					
					mimicData mimicData = new mimicData();
					
					mimicData.strOne = quote[0]; // from before
					
					input.setEditable(false);
					quote[1] = Long.toString(System.currentTimeMillis());
					quote[0]=input.getText();
					
					mimicData.strTwo = quote[0];
					
					taskHandler.taskLst.add(tasks.learnFromMimic1);
					taskHandler.taskData.add(mimicData);
					taskHandler.blnPause = false;
				}
				
				input.setText("");
				quote[0] = quote[0].trim();
				addText("\n-->You: "+quote[0]);

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
		
		if (e.getSource() == btnConfused) {
			blnMimicing = true;
			taskHandler.taskLst.add(tasks.mimic);
			taskHandler.taskData.add(quote[0]);
			taskHandler.blnPause = false;
			
		} else {
			neuronHandler.sav();
			memoryHandler.sav();
			System.exit(0);
		}
	}
}
