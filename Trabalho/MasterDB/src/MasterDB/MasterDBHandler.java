package MasterDB;

import java.util.ArrayList;
import java.util.Scanner;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;


public class MasterDBHandler implements MasterDB.Iface {
	private ArrayList<Server> servers = new ArrayList<Server>();
	private int qtdServers;
	
	public MasterDBHandler(){
		Scanner sc = new Scanner(System.in);
		System.out.println("Quantidade total de servidores:");
		qtdServers = sc.nextInt();
		sc.nextLine();
	}
	public int registerServer(String nameserver, int portNumber) throws TException {
		int serverNumber = servers.size();
		if (servers.size()<qtdServers) {
			if (this.insertServerIfValid(nameserver, portNumber)) {
				return serverNumber;
			}
		}
		return -1;	
	}

	@Override
	public String getNeighborsServers(int myNumber) throws TException {
		String neighbors="";
		Server s;
		if(servers.size()<qtdServers){
			return "";
		}
		for(int i=0; i<servers.size(); i++){
			if(i!=myNumber){
				s=servers.get(i);
				neighbors+=i+":"+s.getNameserver()+":"+s.getPorta()+"\n";
			}
		}
		return neighbors;
	}

	@Override
	public boolean removeServer(int Number) throws TException {
		// NAO IMPLEMENTADO NA SEGUNDA ENTREGA
		return false;
	}
	
	public boolean insertServerIfValid(String nameserver, int portNumber){
		try {//se conseguir conectar ao servidor insere na lista
			TTransport transport =new TFramedTransport(new TSocket(nameserver, portNumber));
			transport.open();
			TProtocol protocol = new TBinaryProtocol(transport);
			DB.Client client = new DB.Client( protocol );
			System.out.println("Novo Server: "+nameserver+":"+portNumber);
			servers.add(new Server(nameserver, portNumber));
			return true;
		}catch (Exception e){
			
		}
		return false;
	}
}
