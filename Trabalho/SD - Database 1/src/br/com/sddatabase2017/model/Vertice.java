package br.com.sddatabase2017.model;

import java.io.Serializable;

public class Vertice implements Serializable{
	private int nome;
	
	private int cor;
	private double peso;
	private String descricao;
	
	public Vertice(int nome, int cor, double peso,String descricao) {
		this.nome = nome;
		this.cor = cor;
		this.descricao = descricao;
		this.peso = peso;
	}
	
	public int getCor() {
		return cor;
	}
	public void setCor(int cor) {
		this.cor = cor;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	public int getNome() {
		return nome;
	}

}
