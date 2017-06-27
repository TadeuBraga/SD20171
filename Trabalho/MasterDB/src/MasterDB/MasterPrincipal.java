package MasterDB;
import org.apache.thrift.server.TServer ;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.server.TNonblockingServer ;
import org.apache.thrift.transport.TServerSocket ;
import org.apache.thrift.transport.TServerTransport ;
import org.apache.thrift.server.TServer.Args ;
import org.apache.thrift.server.TSimpleServer ;
import org.apache.thrift.server.TThreadPoolServer ;

//import java.io.*;
import java.util.*;
public class MasterPrincipal {
	public static void main(String [] args){		
		try {
			TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(8989);
			MasterDBHandler handler = new MasterDBHandler();
			MasterDB.Processor processor = new MasterDB.Processor(handler);//Associando o serviço criado pelo Thrift
			TServer server = new TNonblockingServer(new TNonblockingServer.Args(serverTransport).processor(processor));
			System.out.println("MasterDB> Iniciado com sucesso!\n Porta: "+8989);
			server.serve();
		} catch(Exception x) {
				x.printStackTrace() ;
		}
		
	}

}
