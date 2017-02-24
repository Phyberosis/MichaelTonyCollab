import java.io.IOException;

public class Main {
	
	//public static Object hubAnchor = new Object();
	//public static ArrayList<String> log = new ArrayList<String>();
	
	public static void main(String args[]) throws IOException{
		
		new log();
		new Interface();
		
		taskHandler TH = new taskHandler();
		TH.start();
		taskHandler.taskLst.add(tasks.ini);
		taskHandler.taskData.add(new Object());
		taskHandler.blnPause = false;
		/*synchronized (hubAnchor){
			hubAnchor.notifyAll();
		}*/
	}
}