package Bovary;

public class Prefix {
	String[] t;
	final static String start = "<START>", end = "<END>", par = "<PAR>";
	
	Prefix(int n){
		t = new String[n];
		for(int i=0; i < n; i++)
			t[i] = "<START>";
	}
	
	
	Prefix(String[] t){
		if(t == null) {return;}
		else {
			this.t = new String[t.length];
			for(int i=0; i < t.length; i++)
				this.t[i] = t[i];
		}
	}
	
	
	static Boolean eq(Prefix p1, Prefix p2) {
		if(p1.t == null && p2.t == null) return true;
		if(p1.t == null || p2.t == null || p1.t.length != p2.t.length) return false;
		
		int len = p1.t.length;
		for(int i = 0; i < len; i++) {
			if(!p1.t[i].equals(p2.t[i]))
				return false;
		}
		
		return true;
	}
	
	Prefix addShift(String w) {
		WordList listeMots = new WordList(this.t);
		listeMots.removeFirst();
		listeMots.addLast(w);
		
		return new Prefix(listeMots.toArray());
	}
	
	public int hashCode() {
		if (this.t == null) return 0;
		
		int h = 0;
		for(String s: t) {
			h = 37*h + s.hashCode();
		}
		return h;
	}
	
	int hashCode(int n) {
		int res = hashCode() % n;
		if(res < 0) res += n;
		return res;
	}
}