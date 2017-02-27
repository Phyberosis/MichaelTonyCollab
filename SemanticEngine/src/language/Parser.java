package language;

import java.util.LinkedList;
import java.util.Scanner;

public class Parser
{
	
	public Parser()
	{
	}
	
	// separates a body of text into sentences -> returned in a list
	/**broken, -> abbreviations with periods cause problems*/
	public LinkedList<String> getSentences(String s)
	{
		LinkedList<String> ret = new LinkedList<>();
		Scanner reader = new Scanner(s);
		
		String sentence = "";
		while(reader.hasNext())
		{
			while(!hasEndmark(sentence)) //add words until end of sentence reached
			{
				sentence += reader.next() + " ";
			}
			
			ret.add(sentence);
			sentence = "";
		}
		
		reader.close();
		
		return ret;
	}
	
	// determines if string has a punctuation signifying end of sentence
	private boolean hasEndmark(String s)
	{
		return s.contains(".") || s.contains("?") || s.contains("!");
	}
}
