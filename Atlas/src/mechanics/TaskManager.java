package mechanics;

import java.util.LinkedList;

import gui.Log;

public class TaskManager implements Runnable
{

	private LinkedList<Task> tasks;
	private Thread thdMe;
	
	public TaskManager()
	{
		
	}
	@Override
	public void run()
	{
		while(true)
		{
			if(tasks.isEmpty())
			{
				try
				{
					synchronized(this)
					{
						this.wait();
					}
				} catch (InterruptedException e)
				{}
			}else{
				if(!tryTasks())
				{
					try
					{
						synchronized(this)
						{
							Thread.sleep(250);
						}
					} catch (InterruptedException e)
					{}
				}
			}
		}
	}
	
	public void begin()
	{
		if (thdMe == null) {
			thdMe = new Thread(this);
			thdMe.start();			
		}else{
			Log.immediateMsg("TaskManager is already running, but was asked to run again");
		}
	}
	
	private boolean tryTasks()
	{
		boolean workDone = false;	// if no tasks can be done, prevents immediate rechecks by loop
		for(Task t : tasks)
		{
			
		}
		
		return workDone;
	}
}
