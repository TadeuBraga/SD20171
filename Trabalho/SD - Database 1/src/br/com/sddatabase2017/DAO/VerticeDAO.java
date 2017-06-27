package br.com.sddatabase2017.DAO;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import br.com.sddatabase2017.model.Aresta;
import br.com.sddatabase2017.model.ArestaID;
import br.com.sddatabase2017.model.Vertice;

public class VerticeDAO {
	public static void persist(HashMap<Integer, Vertice> verticesDB) throws FileNotFoundException, IOException{
		ObjectOutputStream objectOut = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("Vertices.db")));			
		objectOut.writeObject(verticesDB);
		objectOut.close();
	}
	public static HashMap<Integer, Vertice> retrieve() throws ClassNotFoundException, IOException{
		ObjectInputStream objectIn = new ObjectInputStream(new BufferedInputStream(new FileInputStream("Vertices.db")));
		HashMap<Integer, Vertice> verticesDB = (HashMap<Integer, Vertice>)objectIn.readObject();
		objectIn.close();
		return verticesDB;
	}
}
