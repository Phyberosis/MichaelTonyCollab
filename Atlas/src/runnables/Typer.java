package runnables;

import java.util.LinkedList;

import javax.swing.JTextArea;

import gui.Log;

public class Typer implements Runnable
{
	private LinkedList<String> mMsgList;
	private Thread thdMe;
	//private boolean mLogging;
	private JTextArea mText;
	
	public Typer(JTextArea ta)
	{
		mMsgList = new LinkedList<>();
		//mLogging = logging; // initiate with true to print to log false is not implemented
		mText = ta;
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			if(mMsgList.isEmpty())
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
				if(mMsgList.size() > 40)
				{
					String dump = "";
					while(mMsgList.size() > 40)
					{
						dump += mMsgList.remove();
					}
					//System.out.print(dump);
					addText(dump);
					continue;
				}else
				{
					try
					{
						Thread.sleep(17);
					} catch (InterruptedException e)
					{}
				}

				if(mMsgList.getFirst().contains("#DELAY"))
					delay();
				
				addText(mMsgList.remove());
//				if(mLogging){
//					//System.out.print(mMsgList.remove());
//					
//				}else{
//					// it could call other methods here to type on other text fields
//				}
			}
		}
	}
	
	public synchronized void addMsg(String str, boolean breakup)
	{
		if(breakup)
		{
			char[] msg = str.toCharArray();
			for(int i = 0; i < msg.length - 1; i++)
			{
				mMsgList.addLast(Character.toString(msg[i]));
			}
			mMsgList.addLast(Character.toString(msg[msg.length - 1]) + "\n");
		}else{
			mMsgList.addLast(str + "\n");
		}
		
		synchronized(this)
		{
			this.notify();
		}
		
	}
	
	public void addDelay(int ms)
	{
		mMsgList.addLast("#DELAY" + Integer.toString(ms));
	}
	
	private void addText(String str)
	{
		mText.setText(mText.getText() + str);
	}
	
	public String dumpMsgs()
	{
		String all = "";
		while(!mMsgList.isEmpty())
		{
			all += mMsgList.remove();
		}
		
		return all;
	}
	
	private void delay()
	{
		long delay = Long.valueOf(mMsgList.remove().substring(6));//System.currentTimeMillis();
		long start = System.currentTimeMillis();
		while(delay > 0)
		{
			try
			{
				Thread.sleep(delay);
				delay = 0;
			} catch (InterruptedException e1)
			{
				delay -= (System.currentTimeMillis() - start);
			}
		}
	}
	
	public void begin()
	{
		if (thdMe == null) {
			thdMe = new Thread(this);
			thdMe.start();			
		}else{
			Log.immediateMsg("Logger is already running, but was asked to run again");
		}
	}

}
