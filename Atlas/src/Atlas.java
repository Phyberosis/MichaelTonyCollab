import mechanics.Dispatcher;
import semantics.SpeechHandler;

public class Atlas {
	
	//public static Object hubAnchor = new Object();
	//public static ArrayList<String> log = new ArrayList<String>();
	
	public static void main(String args[])
	{
		Dispatcher d = new Dispatcher();
		new SpeechHandler(d.getInterface());
		for(String s : args)
		{
			System.out.println(s);
		}

		/*synchronized (hubAnchor){
			hubAnchor.notifyAll();
		}*/
	}
}