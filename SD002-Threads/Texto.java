import java.util.*;
public class Texto {
    private String txt="";
    private boolean isUpper=false;
    public synchronized void append(String s){
	txt+=s;
    }
    public synchronized String getTxt(){
    	return txt;
    }	
    public synchronized void changeFirstLower(){
	StringBuilder sb = new StringBuilder(txt);
	
	for(int i=0; i<txt.length(); i++){
		if(isUpper==true){//se a string já estiver maiuscula, interrompe com o break 
			break;		
		}else if(Character.isLowerCase(txt.charAt(i))){
			sb.setCharAt(i,Character.toUpperCase(txt.charAt(i)));
			txt=sb.toString();			
			break;
		}
		if(i==txt.length()-1){//se eh o ultimo caractere da string
			isUpper=true;
		}
	}
	
    }
    public boolean getIsUpper(){
    	return isUpper;
    }
	
}
