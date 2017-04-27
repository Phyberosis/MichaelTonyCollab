package runnables;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import gui.Log;

public class SearchQuery implements Runnable
{

	private ESearchManager mgr;		// manager to report to
	private String qry;				// search term
	private String googleRes;		// the google search result page
	private String[] results;		// the resulting webpages
	
	// only adds to mgr's query hist list if done
	public SearchQuery(String querry, ESearchManager SearchManager, int numOfRes)
	{
		mgr = SearchManager;
		qry = querry;
		results = new String[numOfRes];
		
		// puts operation on separate thread and starts thread
		Thread me = new Thread(this);
		me.start();
	}
	
	@Override
	public void run()
	{
		try
		{
			URL googleResURL = new URL("https://www.google.ca/search?q=" + qry);	// construct url
			googleRes = getPage(googleResURL);										// get page
			System.out.println(googleRes);
			
			//get results from googleRes page
			
			mgr.reportResult(this);
			
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
			Log.println(e.getMessage(), true);
		}
	}

	// manager must sync access
	public String getResult(int index)
	{
		return results[index];
	}
	
	private String getPage(URL url)
	{
		try
		{
	        URLConnection connection = url.openConnection();					// open connection
	        BufferedReader in = new BufferedReader(
	                                new InputStreamReader(
	                                    connection.getInputStream()));			// initiate stream reader

	        StringBuilder response = new StringBuilder();						// for stream to string conversion
	        String inputLine;													// line of google result webpage

	        while ((inputLine = in.readLine()) != null) 						// webpage to string
	            response.append(inputLine);
	        in.close();
	        
	        return response.toString();
	        
		}catch(Exception e)
		{
			e.printStackTrace();
			Log.println(e.getMessage(), true);
			return "<<failed>>";
		}
	}
}


