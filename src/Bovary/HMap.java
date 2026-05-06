package Bovary;

public class HMap {
	EntryList[] t;
	int nbEntries = 0;
	int defaultLength = 4;
	
	HMap(int n){
		t = new EntryList[n];
		for(int i=0; i < n; i++) {
			t[i] = null;
		}
	}
	
	HMap(){
		t = new EntryList[defaultLength];
		for(int i=0; i < defaultLength; i++) {
			t[i] = null;
		}
	}
	
	
	void rehash(int n) {
		int hashKey = 0;
		EntryList candidates = null;
		
		EntryList[] temp = new EntryList[n];
		for(int i=0; i < n; i++) {
			temp[i] = null;
		}
		for(int i=0; i < this.t.length; i++) {
			for(EntryList cur = t[i]; cur != null; cur = cur.next) {
				hashKey = cur.head.key.hashCode(n);
				if(temp[hashKey] == null) {
					temp[hashKey] = new EntryList(cur.head, null);
				}
				else {
					candidates = temp[hashKey];
					while(candidates.next != null)
						candidates = candidates.next;
					candidates.next = new EntryList(cur.head, null);
				}
			}
		}
		
		this.t = temp;
	}
	
	WordList find(Prefix key) {
		int hashKey = key.hashCode(this.t.length);
		
		EntryList candidates = this.t[hashKey];
		while(candidates != null) {
			if(Prefix.eq(candidates.head.key, key))
				return candidates.head.value;
			candidates = candidates.next;
		}
		
		return null;
	}
	
	void add(Prefix key, String w) {
		float charge = (float) nbEntries / this.t.length;
		this.addSimple(key, w);
		int i = 0, len = this.t.length;
		while(charge >= 0.75) {
			i++;
			len *= 2;
			charge = (float) charge / 2;
		}
		if(i > 0) rehash(len);
	}
	void addSimple(Prefix key, String w) {
		int hashKey = key.hashCode(this.t.length);
		
		if(this.t[hashKey] == null) {
			this.t[hashKey] = new EntryList(new Entry(key, new WordList(new String[] {w})), null);
			this.nbEntries++;
			return;
		}
		
		EntryList candidates = this.t[hashKey];
		while(true){
			if(Prefix.eq(candidates.head.key, key)) {
				candidates.head.value.addLast(w);
				return;
			}
			if(candidates.next != null) candidates = candidates.next;
			else
				break;
		}
		
		candidates.next = new EntryList(new Entry(key, new WordList(new String[] {w})), null);
		this.nbEntries++;
		return;
	}
	
	void print() {
		for(int i=0; i < this.t.length; i++) {
			if(t[i] == null) {
				System.out.println("Line" + i + "Empty");
				continue;
			}
			System.out.print("Line" + i + ": ");
			for(EntryList cur = t[i]; cur != null; cur = cur.next) {
				System.out.print(cur.head.key + " ");
				System.out.print(Node.makeString(cur.head.value.content) + "->");
			}
			System.out.println("\n");
		}
	}
	public static void main(String[] args) {
		HMap table = new HMap();
		table.add(new Prefix(2), "aa");
		table.add(new Prefix(2), "b");
		table.add(new Prefix(3), "aa");
		table.add(new Prefix(4), "ad");
		table.add(new Prefix(8), "ac");
		table.add(new Prefix(9), "ae");
		table.add(new Prefix(10), "aac");
		table.add(new Prefix(10), "aab");
		table.add(new Prefix(11), "ba");
		table.add(new Prefix(11), "aa");
		table.print();
		System.out.println(table.nbEntries + " / " + table.t.length);
		float charge = (float) table.nbEntries / table.t.length;
		System.out.println(charge);
	}
}

