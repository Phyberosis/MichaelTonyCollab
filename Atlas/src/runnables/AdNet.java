package runnables;

import java.util.LinkedList;

import mechanics.Neuron;
import mechanics.Synapse;

public class AdNet implements Runnable
{
	
	private int fidelity; // the number of divisions between integers of calculations; the precision of calculations
	private LinkedList<Synapse> Input;
	private int DEFWEIGHT = 1;
	
	public AdNet (int setFidelity, int layers, int[] NeuronsPerLayer)
	{
		fidelity = setFidelity;
		
		// setup layer one; only layer we can access
		Input = new LinkedList<>();
		for(int i = 0; i < NeuronsPerLayer[0]; i++)
		{
			Synapse s = new Synapse(DEFWEIGHT, new Neuron());
			Input.add(s);
		}
		
//		for(int layer = 1; layer < layers; layer++)
//		{
//			for(Synapse s : )
//		}
	}
	
	@Override
	public void run()
	{
		
	}
	
}