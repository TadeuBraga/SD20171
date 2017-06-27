package br.com.sddatabase2017.model;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.Semaphore;

public class Aresta implements Serializable {
	private ArestaID arestaid;
	private double peso;
	private boolean direcional;
	private Semaphore semaforo;
	public double getPeso() {
		return peso;
	}
	public Aresta(int nomev1, int nomev2, double peso, boolean direcional) {
		this.arestaid=new ArestaID(nomev1,nomev2);
		this.peso = peso;
		this.direcional = direcional;
	}
	
	public void setPeso(double peso) {
		this.peso = peso;
	}
	public boolean isDirecional() {
		return direcional;
	}
	public void setDirecional(boolean direcional) {
		this.direcional = direcional;
	}
	public int getNomev1() {
		return arestaid.getNomev1();
	}
	public int getNomev2() {
		return arestaid.getNomev2();
	}
	public ArestaID getArestaID() {
		return arestaid;
	}
	public String listVertices(){
		return "Vertice 1: "+this.arestaid.getNomev1()+" Vertice 2: "+this.arestaid.getNomev2();
	}
	
}
