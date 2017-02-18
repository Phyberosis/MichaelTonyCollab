import java.util.ArrayList;

class mimicData {
	public String strOne;
	public String strTwo;
	public ArrayList<actNeuron> actNeurons;
	public ArrayList<String> actNeuronIDs;
	
	public mimicData() {
		strOne = "";
		strTwo = "";
		actNeurons = new ArrayList<actNeuron>();
		actNeuronIDs = new ArrayList<String>();
	}
}

class memory {
	public String strNeuronRepresentingMe;
	public ArrayList<String> strRelatedNeurons;
	
	public memory() {
		strNeuronRepresentingMe = "";
		strRelatedNeurons = new  ArrayList<String>();
	}
}

class neuron {
	public ArrayList<String> strRequirements; //nrns that proc me
	public ArrayList<String> strNeuronsIPointTo;
	public boolean blnIsFunction;
	/*public int intPrecedence;
	public ArrayList<String> strFunctionPreReqs;*/
	
	public neuron() {
		strRequirements = new ArrayList<String>();
		strRequirements.trimToSize();
		strNeuronsIPointTo = new ArrayList<String>();
		strNeuronsIPointTo .trimToSize();
		/*strFunctionPreReqs = new ArrayList<String>();
		strFunctionPreReqs .trimToSize();*/
		blnIsFunction = false;
		/*intPrecedence = 0;*/
	}
}

class actNeuron {
	public ArrayList<String> strRequirements; //nrns that proc me
	public ArrayList<String> strNeuronsIPointTo;
	public int intPreReqs;
	public int intAmntPoked;
	public boolean blnFired;
	
	public boolean blnIsFunction;
	/*public int intPrecedence;
	public ArrayList<String> strFunctionPreReqs;*/
	
	public actNeuron(neuron nrn) {
		strRequirements = new ArrayList<String>(nrn.strRequirements);
		strNeuronsIPointTo = new ArrayList<String>(nrn.strNeuronsIPointTo);
		intPreReqs = strRequirements.size();
		intAmntPoked = 0;
		blnFired = false;
		
		blnIsFunction = new Boolean(nrn.blnIsFunction);
		/*intPrecedence = new Integer(nrn.intPrecedence);
		strFunctionPreReqs = new ArrayList<String> (nrn.strFunctionPreReqs);*/
	}
}

class checkDupData {
	public neuron nrn;
	public int intIdx;
	
	public checkDupData(neuron nrn, int intIdx) {
		this.nrn = nrn;
		this.intIdx = intIdx;
	}
	
	public checkDupData() {}
}

class resData {
	public String[] strQuote;
	public  ArrayList<String> strResponse;
	
	public resData() {
		strResponse = new ArrayList<String>();
	}
	
}

class neuralNet implements Runnable {

	public static ArrayList<actNeuron> actNeurons;
	public static ArrayList<String> actNeuronIDs;
	public static boolean blnPause, blnIsPaused, blnDone;
	public static short srtCertainty = 1000, srtCycle = 0;
	//public static tasks currentTask;
	
	private Thread thdMe;
	
	public neuralNet() {
		actNeurons = new ArrayList<actNeuron>();
		actNeuronIDs = new ArrayList<String>();
		blnPause = false;
		blnIsPaused = false;
		blnDone = false;
	}
	
	public void run() {
		
		int intItrtr = 0;
		int intIdxOfActNrn = 0;
		actNeuron actTemp = new actNeuron(new neuron());
		boolean blnChanged;
		float fltPercentCert, fltHighestCert = 0;
		
		while (true) {
			
			blnIsPaused = true;
			while (blnPause || actNeurons.isEmpty()) { // in case unpause with empty array
				//pause
			}
			blnIsPaused = false;
			
			log.println(" * * * neural net cycle runs with neurons: " + actNeuronIDs.toString() + ", certainty: " + srtCertainty);
			
			/*@@@@@*/
			/** BEGINNING OF LOOP**/
			/*@@@@@*/
			intItrtr = 0;
			blnChanged = false;
			while (intItrtr < actNeurons.size() && !blnPause) { //if pause, reset
				
				actTemp = actNeurons.get(intItrtr); //get nrn to work with
				
				//if requirements met, fire nrn
				fltPercentCert = ((float)1 - ((float)(actTemp.strRequirements.size())/(float)actTemp.intPreReqs))*(float)1000; //log.println(Float.toString(fltPercentCert)+  actNeuronIDs.get(intItrtr));
				if ((actTemp.intPreReqs == 0 ||fltPercentCert >= srtCertainty) && !actTemp.blnFired) {
					
					blnChanged = true;
					
					for (int i = 0; i < actTemp.strNeuronsIPointTo.size(); i++) { //nrns I point to
						
						intIdxOfActNrn = actNeuronIDs.indexOf(actTemp.strNeuronsIPointTo.get(i));
						if(intIdxOfActNrn != -1) {
							actNeurons.get(intIdxOfActNrn).intAmntPoked ++; //if nrn I pointed to already fired, increments fired amnt
							
						} else {
							for (int ii = 0; ii <= actTemp.intAmntPoked; ii ++ ) {
								neuronHandler.fire(actTemp.strNeuronsIPointTo.get(i), actNeuronIDs.get(intItrtr));
							}
						}
					}
					
					actTemp.blnFired = true;
					actNeurons.set(intItrtr, actTemp); //set changes
					
				} else if (fltPercentCert > fltHighestCert && !actTemp.blnFired){
					fltHighestCert = fltPercentCert;//System.out.println(fltHighestCert);
				}
				
				intItrtr ++;
			}
			/*@@@@@*/
			/**@@ END OF LOOP @@**/
			/*@@@@@*/
			srtCycle ++;
			
			if (!blnPause && !blnChanged) {// if break not from pause event, ie no more fires possible from current params
				
				if (srtCycle <= 2) {
					srtCertainty = (short) Math.floor(fltHighestCert - 1);//System.out.println(fltHighestCert);
					
				} else {
					log.println(" * * * neural net cycle ended with neurons: " + actNeuronIDs.toString());
					
					srtCycle = 0;
					srtCertainty = 1000;
					fltHighestCert = 0;
					
					blnDone = true;
					blnPause = true;
				}
			}
		}
	}
	
	public void start() {
		if (thdMe == null) {
			thdMe = new Thread(this);
			thdMe.start();
			log.println( "The neuralNet thread has started.");
			
		} else {
			log.println("The neuralNet thread is already running.");
			//taskHandler.pause = false;
		}	
	}
}

class checkDuplicate implements Runnable {

	public static neuron nrn;
	public static int intIdx;
	public static boolean blnRun= false;
	
	private Thread thdMe;
	
	public checkDuplicate() {
	}
	
	public void run() {

		String strTemp = "";
		while (true) {
			
			while (!blnRun) {
				//wait
			}
			
			/*@@@@@*/
			/** BEGINNING OF "LOOP"**/
			/*@@@@@*/
			if (!neuronHandler.intFreeIdxs.contains(intIdx)) {
				
				for (int i = 0; i < neuronHandler.neurons.size(); i++) {
					
					if (nrn.strRequirements.equals(neuronHandler.neurons.get(i).strRequirements) && i != intIdx) {
						
						if (nrn.strNeuronsIPointTo.equals(neuronHandler.neurons.get(i).strNeuronsIPointTo)) {
							
							if (!neuronHandler.intFreeIdxs.contains(intIdx)) {
								neuronHandler.rmvNrn(intIdx);

								log.println("---Neuron (" + neuronHandler.toHex(intIdx) + ") was duplicate - removed.");
								
								for (int ii = 0; ii < nrn.strNeuronsIPointTo.size(); ii++) {
									strTemp = nrn.strRequirements.get(ii);

									if (Character.valueOf(strTemp.charAt(0)) > 47 && Character.valueOf(strTemp.charAt(0)) < 58) { //is word
										memoryHandler.memories.get(Integer.valueOf(strTemp)).strRelatedNeurons.remove(neuronHandler.toHex(intIdx));

									} else {
										neuronHandler.neurons.get(neuronHandler.toNum(strTemp)).strRequirements.remove(neuronHandler.toHex(intIdx));
									}
								}
							}
							
							blnRun = false;
							break;
						}
					}
				}
			}
			/*@@@@@*/
			/**@@ END OF "LOOP" @@**/
			/*@@@@@*/
			
			blnRun = false;
		}
	}
	
	public void start() {
		if (thdMe == null) {
			thdMe = new Thread(this);
			thdMe.start();
		}
	}
}