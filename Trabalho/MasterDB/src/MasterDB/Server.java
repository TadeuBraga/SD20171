package MasterDB;
public class Server {
	private String nameserver;
	private int portNumber;
	
	public Server(String nameserver, int portNumber) {
		this.nameserver = nameserver;
		this.portNumber = portNumber;
	}
	public String getNameserver() {
		return nameserver;
	}
	public int getPorta() {
		return portNumber;
	}
}
