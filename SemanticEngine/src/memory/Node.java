package memory;

import java.io.Serializable;

public class Node implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6865143412771614401L;
	
	public String info;
	public Node[] next;
	
	public Node()
	{
		next = new Node[10];
	}
}
