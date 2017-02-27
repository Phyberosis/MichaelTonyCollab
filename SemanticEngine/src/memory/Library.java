package memory;

import java.io.Serializable;

public class Library implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8989136629609959567L;
	
	public Node contents;
	
	public Library()
	{
		contents = new Node();
		contents.info = "this is the base node";
	}
}
