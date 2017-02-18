/*import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;

public class cerebrum {
	
	public static ArrayList<String> nNetTo = new ArrayList<String>(); // index is in letters, a 0 is ` as in 16 = a`, aa = 17
	public static ArrayList<String> nNetPre = new ArrayList<String>();
	public static ArrayList<Integer> freeNrn = new ArrayList<Integer>(); //erased indexes, these are ind # that are free and can be re-added (reused)
	
	//loads neural net and related variables
	public static void ini() {
		
		String strLoc = "";
		
		try {
			strLoc = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath().toString();
			strLoc = strLoc.substring(0, strLoc.lastIndexOf("/") - 3) + "Cranium/";
		} catch (URISyntaxException e2) {
			e2.printStackTrace();
		}
		
		File a = new File(strLoc + "neuralNet.txt");
		Scanner scn = null;
		String str = "";

		while (true) {
			try {
				scn = new Scanner(a);
				while (scn.hasNextLine()) {
					str = scn.nextLine();
					str = str.substring(str.indexOf(32)+1); //32 is adcii for space
					nNetTo.add((str.substring(0, str.indexOf("||| "))).replace("_", ""));
					nNetPre.add((str.substring(str.indexOf(" ||| ")+5)).replace("_", ""));
				}
				scn.close();
				break;
				
			} catch (FileNotFoundException e1) {
				sav();
			}	
		}
		scn.close();
	}

	
	public static int toNum(String str) {
		
		int ret = 0;
		char ch;
		str = str.replace("-", "");
		str = str.trim();
		int l = str.length(), i = 0;
		
		while (i < l) {
			ch = str.charAt(i);
			ret = (int) (ret + (ch-96)*Math.pow(16, l - i - 1));//System.out.println("ch" + (ch-96));System.out.println((ch-96)*Math.pow(16,l - i - 1));
			i++;
		}

		return ret;
	}
	
	//not actually hex, its just to letters ex a = 1, ` = 0
	public static String toHex(int i) {
		String ret = "";
		char ch;
		boolean fnd = false;
		
		if (i == 0) {
			return "`";
		}
		
		ch = (char) (i/65536 + 96);
		if (!String.valueOf(ch).equals("`")) {
			ret = ret.concat(Character.toString(ch));
			i = i%65536;
			fnd = true;
		}

		ch = (char) (i/4096 + 96);
		if (!String.valueOf(ch).equals("`") || fnd) {
			ret = ret.concat(Character.toString(ch));
			i = i%4096;
			fnd = true;
		}

		ch = (char) (i/256 + 96);
		if (!String.valueOf(ch).equals("`") || fnd) {
			ret = ret.concat(Character.toString(ch));
			i = i%256;
			fnd = true;
		}

		ch = (char) (i/16 + 96);
		if (!String.valueOf(ch).equals("`") || fnd) {
			ret = ret.concat(Character.toString(ch));
			i = i%16;
			fnd = true;
		}

		ch = (char) (i + 96);
		if (!String.valueOf(ch).equals("`") || fnd) {
			ret = ret.concat(Character.toString(ch));
		}

		return ret;
	}
	
	//removes synapse syn, only one synapse at a time!
	public static boolean rmvSynTo(int idx, String syn) {
		
		boolean ret = false;
		int start, end;
		String first, last;
		String neu;
		if (syn.contains("-")) {
			neu = nNetPre.get(idx);
		}else{
			neu = nNetTo.get(idx);
		}
		
		if(neu.contains(syn)) {
			start = neu.indexOf(syn);
			last = neu.substring(start);
			first = neu.substring(0, start);
			end = neu.indexOf(" ");
			last = last.substring(end);
			
			if (first.concat(last).equals("")) {		//if last syn;
				if (syn.contains("-")) {
					nNetTo.set(idx, "_");
				}else{
					nNetPre.set(idx, "_");
				}
				freeNrn.add(idx);
				
			}else{
				if (syn.contains("-")) {
					nNetPre.set(idx, first.concat(last));
				}else{
					nNetTo.set(idx, first.concat(last));
				}
			}
			ret = true;
		}else{
			ret = false;
		}
		
		return ret;
	}
	
	//adds Synapse as String, so can add multiple, ex add adsf and fdsd -> syn = "adsf fdsd"
	public static boolean addSyn(int idx, String syn) {
		
		boolean ret = false;
		int added = 0;
		String to, pre, s = "";
		Scanner scn;

		to = nNetTo.get(idx);
		pre = nNetPre.get(idx);
		scn = new Scanner(syn);
		while (scn.hasNext()) {
			s = scn.next();
			if (!to.contains(s) && !s.contains("-")) { //for next syns
				if (to.contains("_")) {
					to = "";
				}
				to = to.concat(s+" ");
				nNetTo.set(idx, to);
				added++;
			}else if(!pre.contains(s) && s.contains("-")) { //for previous syns
				if (pre.contains("_")) {
					pre = "";
				}
				pre = pre.concat(s+" ");
				nNetPre.set(idx, pre);
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
	
	//true = done, false = neuron is not used
	public static boolean rmvNeu(int idx) {
		
		boolean bol;
		
		if (!nNetPre.get(idx).equals("_") && !nNetTo.get(idx).equals("_")) {
			freeNrn.add(idx);
			nNetPre.set(idx, "_");
			nNetTo.set(idx, "_");
			bol = true;
		}else{
			bol = false;
		}
		
		return bol;
	}
	
	//adds neuron with synapses, if synapses = "", adds "_" 
	public static int addNeu(String synapses) {
		int ret = -1;
		int l = freeNrn.size() - 1;

		if (l != -1) { //free space avail
			ret = freeNrn.get(l);
			freeNrn.remove(l);
			if (!synapses.equals("")) {
				addSyn(ret, synapses);
			}
			
		}else{
			nNetTo.add("_");
			nNetPre.add("_");
			if (!synapses.equals("")) {
				addSyn(nNetTo.size()-1, synapses);
			}

			ret = nNetTo.size()-1;
		}
		return ret;
	}
	
	public static String getPre(int idx) {
		
		String ret = "";
		
		ret = nNetPre.get(idx).replace("-", "");
		if (ret.equals("_")) {
			ret = "";
		}
		return ret;
	}
	
	public static String getTo(int idx) {
		
		String ret = "";

		ret = nNetTo.get(idx);
		if (ret.equals("_")) {
			ret = "";
		}
		return ret;
		
	}
	
	public static void sav() {
		
		String strLoc = "";
		
		File f = new File(strLoc + "neuralNet.txt");
		String str;
		int idx;
		
		try {
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(f)));
			int i = 0, l = nNetTo.size();
			while (i<l) {
				str = nNetTo.get(i);
				if (str.equals("")) {
					str = "_";
				}
				writer.print(toHex(i)+" "+str + "||| ");
				
				str = nNetPre.get(i);
				if (str.equals("")) {
					str = "_";
				}
				writer.println(str);
				i++;
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		f = new File(strLoc + "freeIndexes.txt");
		try {
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(f)));
			int i = 0, l = freeNrn.size();
			while (i<l) {
				idx = freeNrn.get(i);
				writer.println(toHex(i)+" "+idx);
				i++;
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}*/