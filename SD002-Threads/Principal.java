import java.util.ArrayList; 
public class Principal{
	public static void main(String [] args){
		ArrayList<Thread> listadets= new ArrayList();
		int i;
 		Thread t;
		Texto txt= new Texto();
		for(i=0; i<30; i++){
		  
			t = new Thread(new Modelo(i, txt));
			listadets.add(t);
		}
		for(i=29;  i>0; i--){
		   listadets.get(i).start();
		}	
	}
	
}
