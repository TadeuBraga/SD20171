package br.com.sddatabase2017.manager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import br.com.sddatabase2017.model.*;
import br.com.sddatabase2017.thrift.DB;
import br.com.sddatabase2017.thrift.MasterDB;

public class ServerManager {
	private HashMap<Integer,ValidServer> servers=new HashMap<Integer,ValidServer>();
	
	public int myServerNumber;	
	public int getMyServerNumber() {
		return myServerNumber;
	}
	public String masterNameserver;
	public int masterPort;
	public MasterDB.Client masterClient;
	
	private static ServerManager instancia;

	private ServerManager() {
		try {
			masterClient = getMasterClient();			
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static synchronized ServerManager getInstancia() {
		if (instancia == null){
			instancia = new ServerManager();
		}	
		return instancia;
	}
	public void registerServer(String nameserver, int portNumber){
		try {
			this.myServerNumber=masterClient.registerServer(nameserver, portNumber);
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public synchronized void getNeighbors() throws TException, InterruptedException{
		String neighbors=masterClient.getNeighborsServers(myServerNumber);//Obter vizinhos do servidor
		while(neighbors.equals("")){
			neighbors=masterClient.getNeighborsServers(myServerNumber);
			this.wait(5000);
		}
		String linhas[]=neighbors.split("\n");
		String key,nameserver, port;
		for (int i=0; i<linhas.length; i++) {
			String partes[]=linhas[i].split(":");
			key=partes[0];
			nameserver=partes[1];
			port=partes[2];
			insertServerIfValid(Integer.valueOf(key),nameserver, Integer.valueOf(port));
		}
	}
	public boolean insertServerIfValid(int key, String nameserver, int portNumber){
		try {
			TTransport transport = new TFramedTransport(new TSocket(nameserver, portNumber));
			transport.open();
			TProtocol protocol = new TBinaryProtocol(transport);
			DB.Client client = new DB.Client( protocol );
		
	
			System.out.println("Vizinho: "+nameserver+":"+portNumber);
			servers.put(key, new ValidServer(nameserver, portNumber, client));
			return true;
		}catch (Exception e){
			
		}
		return false;
	}
	public MasterDB.Client getMasterClient() throws TTransportException{
		try {
		      FileReader arq = new FileReader("config.txt");
		      BufferedReader lerArq = new BufferedReader(arq);
			   
		      String linha;
		      String partes[], subpartes[];
		  
		      if ((linha = lerArq.readLine())!=null) {
		    	  if(linha.charAt(0)!='#'){
		    		  partes=linha.split("=");
		    		  if(partes[0].equals("master")){
		    			  subpartes=partes[1].split(":");
		    			  masterNameserver=subpartes[0];
		    			  masterPort=Integer.valueOf(subpartes[1]);
		    			  TTransport transport =new TFramedTransport(new TSocket(masterNameserver, masterPort));
		    			  transport.open();
		    			  TProtocol protocol = new TBinaryProtocol(transport);
		    			  MasterDB.Client client = new MasterDB.Client(protocol);
		    			  return client;
		    		  }
		    		     	 
		    	  }
		 
		      }
 
		      arq.close();
		    } catch (IOException e) {
		        System.err.printf("SDDB999: [STARTERROR] Falha ao obter servidor mestre!",
		          e.getMessage());
		    }
		    return null;
	}
	public int getTotalServers(){
		return servers.size()+1;
	}
	public int serverByKeyNumber(int nome){
		return nome%this.getTotalServers();
	}
	public DB.Client getNClient(int serverNumber){
		return servers.get(serverNumber).getClient();
	}
	public Set<Integer> getClientKeys(){
		return servers.keySet();
	}
}
