public class Texto {
    String txt=":";
    public synchronized String append(String s){
	txt+=":"+s+":";
	return txt;
    }
	
}
