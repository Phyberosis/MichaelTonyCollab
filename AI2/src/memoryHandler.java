import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;

public class memoryHandler {
	
	public static ArrayList<memory> memories;
	public static ArrayList<String> memoryNames;
	private static ArrayList<Integer> intFreeIdxs;
	
	public static void ini() {
		String strLoc = "";
		memories = new ArrayList<memory>();
		memoryNames = new ArrayList<String>();
		intFreeIdxs = new ArrayList<Integer>();
		
		try {
			strLoc = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath().toString();
			strLoc = strLoc.substring(0, strLoc.lastIndexOf("/") - 3) + "Cranium/";
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return;
		}
		
		File fleMems = new File(strLoc + "memories.txt");
		File fleFreeIdxs = new File(strLoc + "memFreeIdxs.txt");
		Scanner scn;
		String strToken = "";
		
		while (true) { // in case file does not exist
			try {
				scn = new Scanner(fleMems);
				while (scn.hasNext()) { //each memory
					
					memory mem = new memory();
					memoryNames.add(scn.next());
					mem.strNeuronRepresentingMe = scn.next();
					
					//mem.strRelatedNeurons.clear();
					scn.next(); // for _
					do {
						strToken = scn.next();
						
						if (!strToken.equals("|||")) { //marks end
							mem.strRelatedNeurons.add(strToken);
							
						} else {
							break;
						}
					} while (true);
					memories.add(mem);
				}
				
				scn.close();
				
				scn = new Scanner(fleFreeIdxs);
				while (scn.hasNext()) {
					intFreeIdxs.add(scn.nextInt());
				}
				
				break;
			} catch (FileNotFoundException e1) {
				sav(); //creates file if not exists
			}
		}
	}
	
	public static boolean addMem(String strName) {
		
		memory mem = new memory();
		neuron nrn = new neuron();
		
		int intIdxOfNrn = 0, intIdxOfMe = 0;
		
		if (!memoryNames.contains(strName)) {
			
			if (intFreeIdxs.isEmpty()) {
				intIdxOfMe = memories.size();
				
			} else {
				intIdxOfMe = intFreeIdxs.get(0);
			}
			
			nrn.strNeuronsIPointTo.add(Integer.toString(intIdxOfMe));
			intIdxOfNrn = neuronHandler.addNrn(nrn);
			mem.strNeuronRepresentingMe = neuronHandler.toHex(intIdxOfNrn);
			
			if (intFreeIdxs.isEmpty()) {
				memories.add(mem);
				memoryNames.add(strName);
				
			} else {
				memories.set(intFreeIdxs.get(0), mem);
				memoryNames.set(intFreeIdxs.get(0), strName);
				intFreeIdxs.remove(0);
			}
			return true;
			
		} else {
			return false;
		}
	}

	
	public static String getRelNrn(String strWord) {
		int intIdx = memoryNames.indexOf(strWord);
		
		if (intIdx != -1) {
			return memories.get(intIdx).strNeuronRepresentingMe;
		
		} else {
			return "";
		}
	}
	
	public static memory getMem(String strWord) {
		int intIdx = memoryNames.indexOf(strWord);
		if (intIdx != -1) {
			return memories.get(intIdx);
		} else {
			return null;
		}
	}
	
	public static void sav() {
		
		String strLoc = "";
		
		try {
			strLoc = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath().toString(); //address of bin
			strLoc = strLoc.substring(0, strLoc.lastIndexOf("/") - 3) + "Cranium/"; //out of bin, in to folder cranium
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return;
		}
		
		File fleMems = new File(strLoc + "memories.txt");
		File fleFreeIdxs = new File(strLoc + "memFreeIdxs.txt");
		
		try {
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(fleMems)));
			
			memory memTemp = new memory();
			String strNeurons = "";
			
			for (int i = 0; i < memories.size(); i++) {
				memTemp = memories.get(i);
				strNeurons = memTemp.strRelatedNeurons.toString();
				strNeurons = strNeurons.replace(",", ""); //removes commas from .toString of arraylist
				strNeurons = strNeurons.substring(1, strNeurons.length() -1); //removes the brackets
				
				writer.println(memoryNames.get(i) + " " + memTemp.strNeuronRepresentingMe +  " _ " + strNeurons + " |||");
				writer.flush();
			}
			
			writer.close();
			
			writer = new PrintWriter(new BufferedWriter(new FileWriter(fleFreeIdxs)));
			 
			for (int i = 0; i < intFreeIdxs.size(); i++) {
				writer.print(intFreeIdxs.get(i) + " ");
				writer.flush();
			}
			 
			 writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

/*import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;

public class memory {
	
	public static ArrayList<String> strMemInfo = new ArrayList<String>();
	public static ArrayList<String> strMemName = new ArrayList<String>();
	public static ArrayList<Integer> intFreeIdx = new ArrayList<Integer>();
	public static ArrayList<String> strLog = new ArrayList<String>();
	
	//loads ityral net and related variables
	public static void ini() {
		String strLoc = "";
		
		try {
			strLoc = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath().toString();
			strLoc = strLoc.substring(0, strLoc.lastIndexOf("/") - 3) + "cranium/";
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return;
		}

		File a = new File(strLoc + "shortTerm.txt");
		Scanner scn = null;
		String name, syn, str = "";

		int i = 0;
		while (true) {
			try {
				scn = new Scanner(a);
				while (scn.hasNextLine()) {
					scn.nextLine();
					i++;
				}
				scn.close();
				break;
				
			} catch (FileNotFoundException e1) {
				sav();
			}
		}
		
		try {
			scn = new Scanner(a);
			while (i>0) {
				str = scn.nextLine();
				name = str.substring(0, str.indexOf(32)); // 32 is space
				syn = str.substring(str.indexOf(32)+1);
				strMemInfo.add(syn);
				strMemName.add(name);
				if (str.contains("_ _")) {
					intFreeIdx.add(i-1);
				}
				i--;
			}
			scn.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		scn.close();
		
		//ini log
		File b = new File(strLoc + "Qlog.txt");
		try {
			scn = new Scanner(b);
			while (scn.hasNextLine()){
				scn.nextLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public static boolean addSyn(int idx, String syn) {
		
		boolean ret = false;
		int added = 0;
		String ity, s = "";
		Scanner scn;

		scn = new Scanner(syn);
		while (scn.hasNext()) {
			ity = strMemInfo.get(idx);
			s = scn.next();
			if (!ity.contains(s)) {
				if (ity.contains("_")) {
					ity = "";
				}
				strMemInfo.set(idx, ity.concat(s+" "));
				added++;
			}
		}
		
		if (added > 0){
			ret = true;
		}else{
			ret = false;
		}
		scn.close();
		return ret;
	}
	
	//removes synapse syn
	public static boolean rmvSyn(int idx, String syn) {
		
		boolean ret = false;
		int start;
		String first, last;
		String syns, s;
		
		syn = syn.trim() + " ";
		Scanner scn = new Scanner(syn);
		
		while(scn.hasNext()){
			s = scn.next();
			syns = strMemInfo.get(idx);
			if(syns.contains(s)) {
				start = syns.indexOf(s);
				last = syns.substring(start);
				first = syns.substring(0, start);
				last = last.substring(s.length() + 1);
				strMemInfo.set(idx, first.concat(last));
				ret = true;
			}
		}
		
		scn.close();
		
		//if last syn;
		if (strMemInfo.get(idx).equals("")) {
			strMemInfo.set(idx, "_");
			intFreeIdx.add(idx);
		}
		
		return ret;
	}
	
	//adds entity with synapses, if synapses = "", adds "_" 
	public static int addEntity(String Name, String synapses) {
		int ret = -1;
		int l = intFreeIdx.size();
		int idx = 0;
		
		if (strMemName.contains(Name)) {
			return ret;
		}
		
		if (l != 0) { //free idx avail
			ret = intFreeIdx.get(l-1);
			strMemName.set(ret, Name);
			idx = cerebrum.addNeu(Integer.toString(ret));
			strMemInfo.set(ret, "-" + cerebrum.toHex(idx) + " ");
		}else{
			ret = strMemInfo.size();
			strMemName.add(Name);
			idx = cerebrum.addNeu(Integer.toString(ret));
			strMemInfo.add("-" + cerebrum.toHex(idx) + " ");
		}
		return ret;
	}
	
	//true = done, false = entity is not used
	public static boolean rmvEntity(int idx) {
		
		boolean bol;
		
		if (!strMemName.get(idx).equals("_")) {
			intFreeIdx.add(idx);
			strMemInfo.set(idx, "_");
			strMemName.set(idx, "_");
			bol = true;
		}else{
			bol = false;
		}
		
		return bol;
	}

	public static void sav() {
		
		String strLoc = "";
		
		try {
			strLoc = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath().toString();
			strLoc = strLoc.substring(0, strLoc.lastIndexOf("/") - 3) + "cranium/";
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return;
		}
		
		File f = new File(strLoc + "shortTerm.txt");
		String str, name;
		
		try {
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(f)));
			int i = 0, l = strMemInfo.size();
			while (i<l) {
				str = strMemInfo.get(i);
				name = strMemName.get(i);
				writer.println(name+" "+str);
				i++;
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//sav log
		File b = new File(strLoc + "Qlog.txt");
		try {
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(b)));
			int l = strLog.size()-1;
			for (int i = 0; i <= l; i++) {
				writer.println(strLog.get(l-i));
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}*/