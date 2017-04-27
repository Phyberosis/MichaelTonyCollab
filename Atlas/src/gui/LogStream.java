package gui;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;

public class LogStream extends OutputStream
{
	private JTextArea mText;
	public LogStream(JTextArea ta)
	{
		mText = ta;
	}
	
	@Override
	public void write(int b) throws IOException
	{
		mText.append(String.valueOf((char)b));						//print
		mText.setCaretPosition(mText.getDocument().getLength());	//scroll
	}
}
