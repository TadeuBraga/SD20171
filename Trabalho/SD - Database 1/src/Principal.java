
import org.apache.thrift.server.TServer ;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TNonblockingServer ;
import org.apache.thrift.transport.TServerSocket ;
import org.apache.thrift.transport.TServerTransport ;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.server.TServer.Args ;
import org.apache.thrift.server.TSimpleServer ;
import org.apache.thrift.server.TThreadPoolServer ;

import br.com.sddatabase2017.*;
import br.com.sddatabase2017.manager.*;
import br.com.sddatabase2017.thrift.*;

import java.io.*;
import java.util.*;


public class Principal {
	public static void main(String [] args){
		ServerManager sm=ServerManager.getInstancia();
		int portaASerInstanciada;
		/*Scanner sc = new Scanner(System.in);
		System.out.println("Digite a porta:");*/
		portaASerInstanciada=9090;
		//sc.nextLine();
		TServer server = null;
		while(server==null){
			try {
				TServerTransport serverTransport=new TServerSocket(portaASerInstanciada);
				TThreadPoolServer.Args serverArgs=new TThreadPoolServer.Args(serverTransport);
				serverArgs.processor(new DB.Processor(new DBHandler()));
				serverArgs.transportFactory(new TFramedTransport.Factory());
				serverArgs.protocolFactory(new TBinaryProtocol.Factory(true,true));
				server=new TThreadPoolServer(serverArgs);
				sm.registerServer("localhost", portaASerInstanciada);//Registra o servidor no mestre
				System.out.println("SDDB Iniciado com sucesso!\n Porta: "+portaASerInstanciada);
				sm.getNeighbors();//aguarda o número de vizinhos suficientes e monta a lista
				server.serve();
				
				/*TServerSocket serverTransport = new TServerSocket(portaASerInstanciada);
				DBHandler handler = new DBHandler();//Instanciar o tratador
				DB.Processor processor = new DB.Processor(handler);//Associando o serviÃ§o criado pelo Thrift
				TProtocolFactory tProtocolFactory = new TBinaryProtocol.Factory();
				server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor).protocolFactory(tProtocolFactory));
				sm.registerServer("localhost", portaASerInstanciada);//Registra o servidor no mestre
				System.out.println("SDDB Iniciado com sucesso!\n Porta: "+portaASerInstanciada);
				sm.getNeighbors();//aguarda o número de vizinhos suficientes e monta a lista
				server.serve();*/
			} catch(Exception e) {
				System.out.println("Falha ao iniciar na porta: "+portaASerInstanciada);
				server = null;
				e.printStackTrace();
				portaASerInstanciada++;
			}

		}
	}

}