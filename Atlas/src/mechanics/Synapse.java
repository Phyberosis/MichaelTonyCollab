package mechanics;

public class Synapse
{
	private Neuron next;	// neuron this connects to
	private int weight;		// effect factor -> strength of connection
	
	public Synapse(int wght, Neuron nxt)
	{
		weight = wght;
		next = nxt;
	}
}
