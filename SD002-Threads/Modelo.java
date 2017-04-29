import java.util.Scanner;
public class Modelo implements Runnable {
    private int identificador;
    Texto t;
    public Modelo(int identificador, Texto t){
	this.identificador=identificador;
	this.t=t;
	
}
    public void run(){
       System.out.println("Eu sou a thread: "+identificador);
       System.out.println(t.append(""+identificador));
    }
  }
