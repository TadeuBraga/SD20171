package br.com.sddatabase2017.manager;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import br.com.sddatabase2017.DAO.VerticeDAO;
import br.com.sddatabase2017.model.*;
import br.com.sddatabase2017.thrift.DBHandler;

public class VerticeManager {
	private HashMap<Integer, Vertice> verticesDB;
	DBHandler dh;//usada para remover as arestas quando um dado vertice é removido;
	public VerticeManager(DBHandler dh) throws ClassNotFoundException, IOException{
		this.dh=dh;
		//try{
		//	this.verticesDB=VerticeDAO.retrieve();
		//}catch(Exception e){
			verticesDB = new HashMap<Integer, Vertice>();
		//}
	}
	public void create(Vertice v) throws Exception{
		if(!verticesDB.containsKey(v.getNome())){
			verticesDB.put(v.getNome(),v);
		}else{
			throw new Exception("SDDB008: [VERTICECREATEERROR] Vertice ja existe no banco de dados!");
		}
	}
	public Vertice delete(int nome) throws Exception{
		if(verticesDB.containsKey(nome)){
			Vertice v=verticesDB.get(nome);
			synchronized (v) {
				if(dh!=null){//se tem ArestaManager deleta todas arestas que "passem" por este vertice
					dh.deleteArestaByVertice(nome);
				}
				return verticesDB.remove(nome);
			}
		}else{
			throw new Exception("SDDB009: [VERTICEDELETEERROR] Vertice nao existe no banco de dados!");
		}
	}
	public void update(Vertice v) throws Exception{
		synchronized (v) {
			if(verticesDB.containsKey(v.getNome())){
				verticesDB.replace(v.getNome(), v);
			}else{
				throw new Exception("SDDB009: [VERTICEDELETEERROR] Vertice nao existe no banco de dados!");
			}	
		}
				
	}
	public Vertice read(int nome) throws Exception{
		if(verticesDB.containsKey(nome)){
			return verticesDB.get(nome);
		}else{
			throw new Exception("SDDB009: [VERTICEDELETEERROR] Vertice nao existe no banco de dados!");
		}
	}

	public void commit() throws FileNotFoundException, IOException{
		synchronized(verticesDB){
			VerticeDAO.persist(verticesDB);
		}
	}
}
