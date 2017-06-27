package br.com.sddatabase2017.DAO;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import br.com.sddatabase2017.model.Aresta;
import br.com.sddatabase2017.model.ArestaID;

public class ArestaDAO {
	
		public static void persist(HashMap<ArestaID, Aresta> arestasDB) throws FileNotFoundException, IOException{
			ObjectOutputStream objectOut = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("Arestas.db")));			
			objectOut.writeObject(arestasDB);
			objectOut.close();
		}
		public static HashMap<ArestaID, Aresta> retrieve() throws ClassNotFoundException, IOException{
			ObjectInputStream objectIn = new ObjectInputStream(new BufferedInputStream(new FileInputStream("Arestas.db")));
			HashMap<ArestaID,Aresta> arestasDB = (HashMap<ArestaID, Aresta>)objectIn.readObject();
			objectIn.close();
			return arestasDB;
		}
}
