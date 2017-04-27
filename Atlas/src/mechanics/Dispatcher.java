package mechanics;

import gui.Interface;
import runnables.ESearchManager;

public class Dispatcher
{
	private Interface i;
	private ESearchManager esm;
	private TaskManager tm;
	private boolean checkingExit;
	private boolean isListening;
	public Dispatcher()
	{
		i = new Interface(this);
		checkingExit = false;
		isListening = false;
	}
	
	public void newInput(String[] s)
	{
		
		/**
		 * The following is temporary
		 */
		if(!isListening && s[0].equals("atlas"))
		{
			isListening = true;
			i.respond("yes?");
		}else if(isListening)
		{
			if (s[0].equals("exit"))
			{
				checkingExit = true;
				i.respond("Are you sure you want exit?");
			}else if(s[0].equals("yes") && checkingExit)
			{
				System.exit(0);
			}else if(s[0].contains("search"))
			{
				String query = s[0].replaceFirst("search", "");
				esm.newSearch(query);
				
			}else{
				
				i.respond("foreign command: \"" + s[0] + "\"");
				isListening = false;
			}
		}
		/**
		 * end temporary region
		 */
	}
	
	public Interface getInterface()
	{
		return i;
	}
}
