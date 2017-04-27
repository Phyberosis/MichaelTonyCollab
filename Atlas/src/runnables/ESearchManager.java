package runnables;

import java.util.LinkedList;

public class ESearchManager
{

	private LinkedList<SearchQuery> queryHist;			// results from past searches
	private int memoryLimit;							// the number of queries to keep
	
	private final int PAGESPERQUERY = 3;
	
	// initial setup
	public ESearchManager(int memLim)
	{
		queryHist = new LinkedList<>();
		memoryLimit = memLim;
		
		//makes sure memory has space
		if(memoryLimit < 1)
		{
			memoryLimit = 1;
		}
	}
	
	public void newSearch(String query)
	{
		new SearchQuery(query, this, PAGESPERQUERY);	// SearchQuery has this manager's ref. will report back directly
	}
	
	// sync between searches
	public synchronized void reportResult(SearchQuery res)
	{
		// keeps memory under cap
		if(queryHist.size() == memoryLimit)
		{
			queryHist.remove();
		}
		queryHist.add(res);
	}
}
