package memory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;

public class Librarian
{

	private Library lib;
	
	public Librarian()
	{
		lib = new Library();
		load();
	}
	
	public void save()
	{
		String strLoc = "D:\\GitLocalRepo\\MichaelTonyCollab\\SemanticEngine\\src\\memory\\dictionary.ser";
		/*try
		{
			strLoc = Librarian.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath().toString();
			strLoc = strLoc.substring(0, strLoc.lastIndexOf("/") - 3) + "memory/dictionary.ser";
			System.out.println(strLoc);
		} catch (URISyntaxException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
*/
		
		try (ObjectOutputStream oos =
				new ObjectOutputStream(new FileOutputStream(strLoc))) {

			oos.writeObject(lib);
			System.out.println("Done");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void load()
	{
		String strLoc =  "D:\\GitLocalRepo\\MichaelTonyCollab\\SemanticEngine\\src\\memory\\dictionary.ser";
		/*try
		{
			strLoc = Librarian.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath().toString();
			strLoc = strLoc.substring(0, strLoc.lastIndexOf("/") - 3) + "memory/dictionary.ser";
		} catch (URISyntaxException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		try (ObjectInputStream ois
			= new ObjectInputStream(new FileInputStream(strLoc))) {

			lib = (Library) ois.readObject();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	// returns node with of string -> creates node if not exist
	public Node getStoreString(String s)
	{
		byte[] bytes = s.getBytes();
		int val = 0;
		for(byte b : bytes)//string to int
		{
			val += b;
		}
		
		return getMakeNode(val);
	}
	
	//only called by getStoreString
	private Node getMakeNode(int a)
	{
		int address = a;
		Node n = lib.contents;
		
		int[] unzippedAddress = new int[15];
		int index = 0;
		while(address != 0) // retrieves value at each power of 10
		{
			unzippedAddress[index] = address % 10;
			index ++;
			address = (address - (address % 10)) / 10;
		}
		unzippedAddress[index] = -1;
		
		index = 0;
		while(unzippedAddress[index] != -1)//to next node using address
		{
			if(n.next[unzippedAddress[index]] == null)
			{
				n.next[unzippedAddress[index]] = new Node(); //make node
			}
			n = n.next[unzippedAddress[index]];
			index++;
		}
		
		return n;
	}
}
