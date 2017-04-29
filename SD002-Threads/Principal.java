import java.util.ArrayList; 
public class Principal{
	public static void main(String [] args){
		ArrayList<Thread> listadets= new ArrayList();
		int i, j;
 		Thread t;
		Texto txt= new Texto();
		txt.append("pratica de sd utilizando uma string compartilhada entre multiplas threads e a aplicação do runnable com isolamento");
		for(i=0; i<30; i++){
		  
			t = new Thread(new Modelo(i, txt));
			listadets.add(t);
		}
		int contaTrinta=0;
		while(txt.getIsUpper()==false){
			for(i=29;  i>0; i--){
			      try{
				listadets.get(i).start(); 
			      }catch(Exception e){//caso as threads já tenham sido iniciadas, instancia se 30 novas threads 
				listadets= new ArrayList();
			      	for(j=0; j<30; j++){
		  			t = new Thread(new Modelo(j, txt));
					listadets.add(t);
					
				}
				listadets.get(i).start();
			      } 
			      
			   try{
				listadets.get(i).sleep(1000);
				listadets.get(i).stop();

			   }
			   catch(Exception e){
			   	System.out.println("Falha!");
			   }	
			}
			contaTrinta++;
			   
		}	
	}
	
}
