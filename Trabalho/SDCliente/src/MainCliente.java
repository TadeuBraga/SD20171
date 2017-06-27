
import java.util.Scanner;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.protocol.TProtocol;

import org.apache.thrift.protocol.TBinaryProtocol;

public class MainCliente {
	
	public static int obterMenuPrincipal(Scanner in){
		int opPrincipal;
		System.out.println("1- Vertice 2-Aresta 9- Commitar Modificacoes 10- Sair\n");
		opPrincipal=in.nextInt();
		return opPrincipal;
	}
	public static int obterMenuSecundarioVertice(Scanner in){
		int opSecundario;
		System.out.println("1-Criar 2-Deletar 3-Atualizar 4-Ler 5-Listar Vizinhos  10- Sair\n");
		opSecundario=in.nextInt();
		return opSecundario;
	}
	public static int obterMenuSecundarioAresta(Scanner in){
		int opSecundario;
		System.out.println("1-Criar 2-Deletar 3-Atualizar 4-Ler 5-Listar Arestas por Vertice 6-Deletar Arestas por Vertice  10- Sair\n");
		opSecundario=in.nextInt();
		return opSecundario;
	}
	
	public static void main( String [] args ){

		try {

			TTransport transport = new TSocket("localhost", 9090);
			transport.open();
			TProtocol protocol = new TBinaryProtocol(new TFramedTransport(transport));

			DB.Client client = new DB.Client( protocol );
			
			Scanner in = new Scanner(System.in);
			int nome, nomev2, cor;
			double peso;
			String descr;
			boolean direcional=false;
			int opPrincipal=0, opSecundario=0;
			opPrincipal=obterMenuPrincipal(in);
			in.nextLine();
			while(opPrincipal!=10){
				if(opPrincipal==1){
					opSecundario=obterMenuSecundarioVertice(in);
					in.nextLine();
					while(opSecundario!=10){
						if(opSecundario==1){
							System.out.println("Nome:");
							nome=in.nextInt();
							in.nextLine();
							System.out.println("Cor:");
							cor=in.nextInt();
							in.nextLine();
							System.out.println("Peso:");
							peso=in.nextDouble();
							in.nextLine();
							System.out.println("Descricao:");
							descr=in.nextLine();
							System.out.println(client.createVertice(nome, cor, peso, descr));
							
						}else if(opSecundario==2){
							System.out.println("Nome:");
							nome=in.nextInt();
							in.nextLine();
							System.out.println(client.deleteVertice(nome));	
						}else if(opSecundario==3){
							System.out.println("Nome:");
							nome=in.nextInt();
							in.nextLine();
							System.out.println("Cor:");
							cor=in.nextInt();
							in.nextLine();
							System.out.println("Peso:");
							peso=in.nextDouble();
							in.nextLine();
							System.out.println("Descricao:");
							descr=in.nextLine();
							System.out.println(client.updateVertice(nome, cor, peso, descr));
						}else if(opSecundario==4){
							System.out.println("Nome:");
							nome=in.nextInt();
							in.nextLine();
							System.out.println(client.readVertice(nome));
						}else if(opSecundario==5){
							System.out.println("Nome:");
							nome=in.nextInt();
							in.nextLine();
							System.out.println(client.listNeighborVertices(nome));
						}						
						
						opSecundario=obterMenuSecundarioVertice(in);
					}
				}else if(opPrincipal==2){
					opSecundario=obterMenuSecundarioAresta(in);
					while(opSecundario!=10){
						if(opSecundario==1){
							System.out.println("Nome Vertice 1:");
							nome=in.nextInt();
							in.nextLine();
							System.out.println("Nome Vertice 2:");
							nomev2=in.nextInt();
							in.nextLine();
							System.out.println("Peso:");
							peso=in.nextDouble();
							in.nextLine();
							System.out.println("Direcional:");
							descr=in.nextLine();
							if(descr.equals("Sim")){
								direcional=true;
							}else if(descr.equals("Nao")){
								direcional=false;
							}
							System.out.println("teste");
							System.out.println(client.createAresta(nome, nomev2, peso, direcional));
							
						}else if(opSecundario==2){
							System.out.println("Nome Vertice 1:");
							nome=in.nextInt();
							in.nextLine();
							System.out.println("Nome Vertice 2:");
							nomev2=in.nextInt();
							in.nextLine();
							System.out.println(client.deleteAresta(nome,nomev2));	
						}else if(opSecundario==3){
							System.out.println("Nome Vertice 1:");
							nome=in.nextInt();
							in.nextLine();
							System.out.println("Nome Vertice 2:");
							nomev2=in.nextInt();
							in.nextLine();
							System.out.println("Peso:");
							peso=in.nextDouble();
							in.nextLine();
							System.out.println("Direcional:");
							descr=in.nextLine();
							if(descr.equals("Sim")){
								direcional=true;
							}else if(descr.equals("Nao")){
								direcional=false;
							}
							System.out.println(client.updateAresta(nome, nomev2, peso, direcional));
						}else if(opSecundario==4){
							System.out.println("Nome Vertice 1:");
							nome=in.nextInt();
							in.nextLine();
							System.out.println("Nome Vertice 2:");
							nomev2=in.nextInt();
							in.nextLine();
							System.out.println(client.readAresta(nome,nomev2));
						}else if(opSecundario==5){
							System.out.println("Nome:");
							nome=in.nextInt();	
							in.nextLine();
							System.out.println(client.listArestaByVertice(nome));
						}else if(opSecundario==6){
							System.out.println("Nome:");
							nome=in.nextInt();	
							in.nextLine();
							System.out.println(client.deleteArestaByVertice(nome));
						}
						opSecundario=obterMenuSecundarioAresta(in);
					}
				}else if(opPrincipal==9){
					System.out.println(client.commit());
				}
				
				
				opPrincipal=obterMenuPrincipal(in);
			}
			System.out.println(client.commit());
			
			transport.close();

		}catch(TException x) {

			x. printStackTrace () ;

		}

	}
}