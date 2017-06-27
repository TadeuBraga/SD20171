package br.com.sddatabase2017.model;
import br.com.sddatabase2017.thrift.*;
public class ValidServer {
	private String nameserver;
	private int portNumber;
	DB.Client client;
	
	public ValidServer(String nameserver, int portNumber, DB.Client client) {
		this.nameserver = nameserver;
		this.portNumber = portNumber;
		this.client=client;
	}
	public String getNameserver() {
		return nameserver;
	}
	public int getPorta() {
		return portNumber;
	}
	public DB.Client getClient() {
		return client;
	}
}
