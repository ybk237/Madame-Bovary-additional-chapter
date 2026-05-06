package Bovary;

public class WordList {
	Node content;
	
	WordList(){
		content = null;
	}
	
	WordList(Node content){
		this.content = content;
	}
	
	WordList(String[] t){
		Node res = new Node("");
		for(String w: t) {
			Node.addLast(w, res);
		}
		this.content = res.next;
	}
	
	static WordList foobar = new WordList(new Node("foo", new Node("bar", new Node("baz", null))));
	
	int length() {
		return Node.length(content);
	}
	
	public String toString() {
		return Node.makeString(content);
	}
	
	void addFirst(String w) {
		content = new Node(w, content);
	}
	
	void addLast(String w) {
		if(content == null)
			content = new Node(w);
		else
			Node.addLast(w, content);
	}
	
	String removeFirst() {
		if(content == null)
			return null;
		String first = content.head;
		content = content.next;
		return first;
	}
	
	String removeLast() {
		if (content == null) return null;
		if (content.next == null) return removeFirst();
		
		Node cur = content, prev = null;
		while(cur.next != null) {
			prev = cur;
			cur = cur.next;
		}
		prev.next = null;
		
		return cur.head;
	}
	
	void insert(String s) {
		content = Node.insert(s, content);
	}
	
	void insertionSort() {
		content = Node.insertionSort(content);
	}
	
	void mergeSort() {
		int len = length();
		
		if(len <= 1) return;
		
		int i = 0, mid = (len-1) / 2;
		Node cur = content;
		
		while(i < mid){cur = cur.next; i++;}
		Node startHalf2 = cur.next;
		cur.next = null;
		
		WordList half1 = new WordList(content);
		WordList half2 = new WordList(startHalf2);
		
		half1.mergeSort();
		half2.mergeSort();
		
		content = Node.merge(half1.content, half2.content);
	}
	
	String[] toArray() {
		int i = 0, len = length();	
		String[] res = new String[len];
		
		Node cur = content;
		while(i < len) {
			res[i] = cur.head;
			cur = cur.next;
			i++;
		}
		
		return res;
	}
}
