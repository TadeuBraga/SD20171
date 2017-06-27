package br.com.sddatabase2017.manager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import br.com.sddatabase2017.DAO.ArestaDAO;
import br.com.sddatabase2017.DAO.VerticeDAO;
import br.com.sddatabase2017.model.*;
import br.com.sddatabase2017.thrift.DBHandler;

public class ArestaManager {
	private HashMap<ArestaID, Aresta> arestasDB ;
	DBHandler dh;
	public ArestaManager(DBHandler dh) throws ClassNotFoundException, IOException{
		this.dh=dh;
		//try{
		//	this.arestasDB=ArestaDAO.retrieve();
		//}catch (Exception e){
			this.arestasDB= new HashMap<ArestaID, Aresta>();
		//}
	}
	public void create(Aresta a) throws Exception{
		createWithNotice(a, false, false);
	}
	public void createWithNotice(Aresta a, boolean existv1, boolean existv2) throws Exception{
		ArestaID aid=a.getArestaID();		
		if(!arestasDB.containsKey(aid)){
			if ((existv1 ? true : dh.readVertice(aid.getNomev1()).startsWith("Vertice ")) && (existv2 ? true : dh.readVertice(aid.getNomev2()).startsWith("Vertice ")) ) {
				arestasDB.put(aid,a);
			}else{
				throw new Exception("SDDB005: [VERTICEERROR] Vertices inexistentes no banco!");
			}
			
		}else{
			throw new Exception("SDDB006: [ARESTACREATEERROR] Aresta ja existe no banco de dados!");
		}
	}
	public Aresta delete(ArestaID aid) throws Exception{
			synchronized (aid) {
				if(arestasDB.containsKey(aid)){
					return arestasDB.remove(aid);
				}else{
					throw new Exception("SDDB007: [ARESTADELETEERROR] Aresta nao existe no banco de dados!");
				}
			}
			
	}
	public void update(Aresta a) throws Exception{
		synchronized (a) {
			if(arestasDB.containsKey(a.getArestaID())){
				arestasDB.replace(a.getArestaID(), a);
			}else{
				throw new Exception("SDDB007: [ARESTAUPDATEERROR] Aresta nao existe no banco de dados!");
			}
		}
	}
	public Aresta read(int nomev1, int nomev2) throws Exception{
		ArestaID aid=new ArestaID(nomev1, nomev2);		
		return read(aid);
	}
	public Aresta read(ArestaID aid) throws Exception{
		if(arestasDB.containsKey(aid)){
			return arestasDB.get(aid);
		}else{
			throw new Exception("SDDB007: [ARESTAREADERROR] Aresta nao existe no banco de dados!");
		}
	}
	public String deleteArestaByVertice(int nome){
		Set<ArestaID> aids= (Set<ArestaID>)this.arestasDB.keySet();
		String removed="";
		synchronized(this.arestasDB){
			for(ArestaID aid:aids){
				if(aid.getNomev1()==nome){
					arestasDB.remove(aid);
					removed+=aid.getNomev1()+"-"+aid.getNomev2()+",";
				}else if(aid.getNomev2()==nome){
					arestasDB.remove(aid);
					removed+=aid.getNomev1()+"-"+aid.getNomev2()+",";
				}
			}
		}
		
		return removed;
	}
	public String listArestaByVertice(int nome){
		Set<ArestaID> aids= (Set<ArestaID>)this.arestasDB.keySet();
		String retorno="";
		synchronized(this.arestasDB){
			for(ArestaID aid:aids){
				Aresta a=arestasDB.get(aid);
				if(aid.getNomev1()==nome){
					retorno+=aid.getNomev1()+"-"+aid.getNomev2()+"-"+a.getPeso()+"\n";
				}
				//se eh o nome v2 de uma aresta nao direcional ele vai ser citado na chamado do servidor remoto
			}
		}	
		return retorno+"\n";	
	}
	public String listNeighborVertices(int nome){
		Set<ArestaID> aids= (Set<ArestaID>)this.arestasDB.keySet();
		String retorno ="Listar vertices vizinhos: \n";
		synchronized(this.arestasDB){
			for(ArestaID aid:aids){
				if(aid.getNomev1()==nome){
					retorno+=aid.getNomev2()+", ";
				}
			}
		}
		return retorno+"\n";
		
	}
	public void commit() throws FileNotFoundException, IOException{
		synchronized(this.arestasDB){
			ArestaDAO.persist(arestasDB);
		}
		
	}
}
