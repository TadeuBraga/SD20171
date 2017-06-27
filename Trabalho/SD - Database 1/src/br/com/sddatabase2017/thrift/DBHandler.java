package br.com.sddatabase2017.thrift;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import org.apache.thrift.TException;

import br.com.sddatabase2017.manager.*;
import br.com.sddatabase2017.model.*;
import br.com.sddatabase2017.thrift.DB.Client;
public class DBHandler implements DB.Iface {
	private ArestaManager am;
	private VerticeManager vm;
	private ServerManager sm;
	public DBHandler(){
		try {
			am = new ArestaManager(this);
			vm=new VerticeManager(this);
			sm= ServerManager.getInstancia();
		} catch (ClassNotFoundException e1) {
			System.out.println("SDDB001:Impossivel recuperar dados do banco!");
		} catch (IOException e1) {
			System.out.println("SDDB002:Impossivel recuperar dados do banco!");
		}
	}
	public String commit(){
		/*try {
			vm.commit();
			am.commit();
			return "Commit efetuado com sucesso!";
		} catch (FileNotFoundException e) {
			return "SDDB003: [COMMITERROR] Arquivo nao encontrado!";
		} catch (IOException e) {
			return "SDDB004: [COMMITERROR] Erro de entrada e saida!";
		}*/
		return "SDDB004: [CODEERROR] Ainda não implementado!";
	}
	public String createAresta(int nomev1, int nomev2 , double peso, boolean direcional){
		int responsible=sm.serverByKeyNumber(nomev1); 
		int responsible2=sm.serverByKeyNumber(nomev2);
		int me=sm.getMyServerNumber();
		if(responsible==me){//sou responsável pelo vertice 1
			try {
				Aresta a = new Aresta(nomev1,nomev2, peso,direcional);
				am.create(a);//se executar com sucesso v1 e v2 existe
				if(direcional==false){//cria aresta "equivalente" no outro servidor
					responsible2=sm.serverByKeyNumber(nomev2); 
					if(responsible2==me){
						if (am.read(nomev1,nomev2)==null){
							try {
								Aresta a2 = new Aresta(nomev2,nomev1, peso,direcional);
								am.createWithNotice(a2, false, true);
								return "Aresta criada com sucesso!";
							} catch (Exception e) {
								am.delete(a.getArestaID());//desfaz
								return e.getMessage();
							}
						}else return "Aresta criada com sucesso!";
					}else{
						try {
								Client NClient=sm.getNClient(responsible2);
								return sm.getNClient(responsible).createArestaWithNotice(nomev2, nomev1, peso, direcional, false, true);
						} catch (TException e) {
							am.delete(a.getArestaID());//desfaz
							return e.getMessage();
						}
					}
				}

				return "Aresta atualizada com sucesso!";
			} catch (Exception e) {
				return e.getMessage();
			}
		}
		
		if(direcional==false && responsible2==me){
			try {
				Aresta a2 = new Aresta(nomev2,nomev1, peso,direcional);
				am.create(a2);
				if(direcional==false){//cria aresta "equivalente" no outro servidor
					responsible=sm.serverByKeyNumber(nomev1); 
						try {
							Client NClient=sm.getNClient(responsible);
							return sm.getNClient(responsible).createArestaWithNotice(nomev1, nomev2, peso, direcional, false, true);
						} catch (TException e) {
							am.delete(a2.getArestaID());//desfaz
							return e.getMessage();
						}
					
				}
				
				return "Aresta atualizada com sucesso!";
			} catch (Exception e) {
				return e.getMessage();
			}
		}
		try {
			return sm.getNClient(responsible).createAresta(nomev1,nomev2,peso,direcional);
		} catch (TException e) {
			return e.getMessage();
		}
		
	}
	public String createVertice(int nome, int cor, double peso, String descricao){
		int responsible=sm.serverByKeyNumber(nome); 
		if(responsible==sm.getMyServerNumber()){
			Vertice v=new Vertice(nome, cor, peso,descricao);
			try {
				vm.create(v);
				return "Vertice inserido com sucesso!";
			} catch (Exception e) {
				return e.getMessage();
			}
		}else{
			try {
				return sm.getNClient(responsible).createVertice(nome, cor, peso, descricao);
			} catch (TException e) {
				return e.getMessage();
			}
		}
		
	}
	public String deleteAresta(int nomev1 , int nomev2){
		int responsible=sm.serverByKeyNumber(nomev1); 
		if(responsible==sm.getMyServerNumber()){
			try {
				ArestaID aid=new ArestaID(nomev1, nomev2);
				Aresta a=am.read(aid);
				Boolean direcional=a.isDirecional();
				am.delete(aid);
				if (direcional==false){//deleta aresta correspondente
					int responsible2=sm.serverByKeyNumber(nomev2); 
					if(responsible2==sm.getMyServerNumber()){
						try {
							ArestaID aid2 = new ArestaID(nomev2,nomev1);
							am.delete(aid2);
							return "Aresta deletada com sucesso!";
						} catch (Exception e) {
							am.create(a);//desfaz
							return e.getMessage();
						}
					}else{
						try {
							return sm.getNClient(responsible).deleteAresta(nomev2, nomev1);
						} catch (TException e) {
							am.create(a);//desfaz
							return e.getMessage();
						}
					}
				}	
			} catch (Exception e) {
				return e.getMessage();
			}
		}else{
			try {
				return sm.getNClient(responsible).deleteAresta(nomev1, nomev2);
			} catch (TException e) {
				return e.getMessage();
			}
		}
		return null;
	}
	public String deleteVertice(int nome){
		int responsible=sm.serverByKeyNumber(nome); 
		if(responsible==sm.getMyServerNumber()){
			try {
				vm.delete(nome);
				return "Vertice deletado com sucesso!";
			} catch (Exception e) {
				return e.getMessage();
			}
		}else{
			try {
				return sm.getNClient(responsible).deleteVertice(nome);
			} catch (TException e) {
				return e.getMessage();
			}
		}
	}
	public String updateAresta(int nomev1, int nomev2 , double peso, boolean direcional){
		//TODO inverter se nomev1>nomev2
		int responsible=sm.serverByKeyNumber(nomev1); 
		if(responsible==sm.getMyServerNumber()){
			try {
				Aresta aAntiga=am.read(nomev1, nomev2);
				Aresta a = new Aresta(nomev1,nomev2, peso,direcional);
				am.update(a);
				if(!!aAntiga.isDirecional() && a.isDirecional()){//deleta correspondente no "outro" servidor
					int responsible2=sm.serverByKeyNumber(nomev2); 
					if(responsible2==sm.getMyServerNumber()){
						try {
							ArestaID aid2 = new ArestaID(nomev2,nomev1);
							am.delete(aid2);
							return "Atualizada com sucesso!";
						} catch (Exception e) {
							am.update(aAntiga);//desfaz
							return e.getMessage();
						}
					}else{
						try {
							return sm.getNClient(responsible).deleteAresta(nomev2, nomev1);
						} catch (TException e) {
							am.update(aAntiga);//desfaz
							return e.getMessage();
						}
					}
				}else if(aAntiga.isDirecional() && !!a.isDirecional()){//cria correspondente no "outro" servidor
					int responsible2=sm.serverByKeyNumber(nomev2); 
					if(responsible2==sm.getMyServerNumber()){
						try {
							Aresta a2 = new Aresta(nomev2, nomev1, peso, direcional);
							am.create(a2);
							return "Aresta atualizada com sucesso!";
						} catch (Exception e) {
							am.update(aAntiga);//desfaz
							return e.getMessage();
						}
					}else{
						try {
							return sm.getNClient(responsible).createAresta(nomev2, nomev1,peso, direcional);
						} catch (TException e) {
							am.update(aAntiga);//desfaz
							return e.getMessage();
						}
					}
				}
				return "Aresta atualizada com sucesso!";
			} catch (Exception e) {
				return e.getMessage();
			}
		}else{
			try {
				return sm.getNClient(responsible).updateAresta(nomev1, nomev2, peso, direcional);
			} catch (TException e) {
				return e.getMessage();
			}
		}
	}
	public String updateVertice(int nome, int cor, double peso, String descricao){
		Vertice v=new Vertice(nome, cor, peso,descricao);
		int responsible=sm.serverByKeyNumber(nome); 
		if(responsible==sm.getMyServerNumber()){
			try {
				vm.update(v);
				return "Vertice atualizado com sucesso!";
			} catch (Exception e) {
				return e.getMessage();
			}
		}else{
			try {
				return sm.getNClient(responsible).updateVertice(nome, cor, peso, descricao);
			} catch (TException e) {
				return e.getMessage();
			}
		}
	}
	
	public String readAresta(int nomev1, int nomev2){
		int responsible=sm.serverByKeyNumber(nomev1); 
		if(responsible==sm.getMyServerNumber()){
			try {
				ArestaID aid = new ArestaID(nomev1,nomev2);
				Aresta a=am.read(aid);
				return a.listVertices() + "\nPeso:" + a.getPeso() + " " + "Direcional:" +(a.isDirecional()?"Sim":"Nao")+"\n";
			} catch (Exception e) {
				return e.getMessage();
			}
		}else{
			try {
				return sm.getNClient(responsible).readAresta(nomev1, nomev2);
			} catch (TException e) {
				return e.getMessage();
			}
		}
		
	}
	
	public String readVertice(int nome){
		int responsible=sm.serverByKeyNumber(nome); 
		if(responsible==sm.getMyServerNumber()){
			try {
				Vertice v=vm.read(nome);
				return "Vertice "+v.getNome()+"\n"+"Cor: "+v.getCor()+" Descricao: "+v.getDescricao()+" Peso: "+v.getPeso()+"\n";
			} catch (Exception e) {
				return e.getMessage();
			}
		}else{
			try {
				return sm.getNClient(responsible).readVertice(nome);
			} catch (TException e) {
				return e.getMessage();
			}
		}
	}
	
	public String deleteArestaByVertice(int nome){
		String arestasDeletadas=am.deleteArestaByVertice(nome);
		Set<Integer> chavesClientes=sm.getClientKeys();
		for(Integer i : chavesClientes){
			try {
				arestasDeletadas+=sm.getNClient(nome).deleteArestaByVertice(nome);
			} catch (TException ignored) {
				//caso um servidor lance exception pula para o proximo
			}
		}
		if(!arestasDeletadas.isEmpty()){
			return "Aresta(s) "+ arestasDeletadas + " foram deletadas!";
		}else{
			return "Nenhumada aresta foi deletada!";
		}		 
	}
	public String listArestaByVertice(int nome){
		int responsible=sm.serverByKeyNumber(nome); 
		if(responsible==sm.getMyServerNumber()){
			try {
				return am.listArestaByVertice(nome);
			} catch (Exception e) {
				return e.getMessage();
			}
		}else{
			try {
				return sm.getNClient(responsible).listArestaByVertice(nome);
			} catch (TException e) {
				return e.getMessage();
			}
		}
	}
	public String listNeighborVertices(int nome){
		int responsible=sm.serverByKeyNumber(nome); 
		if(responsible==sm.getMyServerNumber()){
			try {
				return am.listNeighborVertices(nome);
			} catch (Exception e) {
				return e.getMessage();
			}
		}else{
			try {
				return sm.getNClient(responsible).listNeighborVertices(nome);
			} catch (TException e) {
				return e.getMessage();
			}
		}
	}
	@Override
	public String createArestaWithNotice(int nomev1, int nomev2, double peso, boolean direcional, boolean existv1,
			boolean existv2) throws TException {
		int responsible=sm.serverByKeyNumber(nomev1);
		if(responsible==sm.getMyServerNumber()){
			try {
				Aresta a = new Aresta(nomev1,nomev2, peso,direcional);
				am.createWithNotice(a, existv1, existv2);
				if(direcional==false){//cria aresta "equivalente" no outro servidor
					if(existv2==false){
						int responsible2=sm.serverByKeyNumber(nomev2); 
						if(responsible2==sm.getMyServerNumber()){
							if (am.read(nomev1,nomev2)==null){
								try {
									Aresta a2 = new Aresta(nomev2,nomev1, peso,direcional);
									am.createWithNotice(a2, existv2, existv1);
									return "Aresta criada com sucesso!";
								} catch (Exception e) {
									am.delete(a.getArestaID());//desfaz
									return e.getMessage();
								}
							}else return "Aresta criada com sucesso!";
						}else{
							try {
								Client NClient=sm.getNClient(responsible);
								if(NClient.readAresta(nomev1, nomev2).startsWith("SDDB007:")){
									return sm.getNClient(responsible).createAresta(nomev2, nomev1, peso, direcional);
								}else return "Aresta criada com sucesso!";
								
							} catch (TException e) {
								am.delete(a.getArestaID());//desfaz
								return e.getMessage();
							}
						}
					}	
				}
				
				return "Aresta criada com sucesso!";
			} catch (Exception e) {
				return e.getMessage();
			}
		}else{
			try {
				return sm.getNClient(responsible).createAresta(nomev1, nomev2, peso, direcional);
			} catch (TException e) {
				return e.getMessage();
			}
		}
	}
}
