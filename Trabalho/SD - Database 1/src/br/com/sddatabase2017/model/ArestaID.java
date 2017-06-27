package br.com.sddatabase2017.model;

import java.io.Serializable;

public class ArestaID implements Serializable {
	private String nomev1, nomev2;

	public int getNomev1() {
		return Integer.parseInt(nomev1);
	}

	public int getNomev2() {
		return Integer.parseInt(nomev2);
	}

	public ArestaID(int nomev1, int nomev2) {
		this.nomev1 = Integer.toString(nomev1);
		this.nomev2 = Integer.toString(nomev2);
	}
	@Override
	public boolean equals(Object obj) {
        if(obj != null && obj instanceof ArestaID) {
            ArestaID aid = (ArestaID)obj;
            return (this.getNomev1()==aid.getNomev1()) && (this.getNomev2()==aid.getNomev2());
        }
        return false;
    }
	@Override
	public int hashCode(){
		return nomev1.hashCode() * 31 + nomev2.hashCode();
	}
}
