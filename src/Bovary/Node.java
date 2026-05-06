package Bovary;

public class Node {
	String head;
	Node next;
	
	Node(String head){
		this.head = head;
		this.next = null;
	}
	Node(String head, Node next){
		this.head = head;
		this.next = next;
	}
	
	static int lengthRec(Node l) {
		if(l == null)
			return 0;
		return 1 + lengthRec(l.next);
	}
	
	static int length(Node l) {
		int len = 0;
		for(Node cur = l; cur != null; cur = cur.next)
			len++;
		return len;
	}
	
	static String makeString(Node l) {
		if(l == null)
			return "[]";
		
		String res = "[";
		Node cur = l;
		while(cur != null && cur.next != null) {
			res = res.concat(cur.head).concat(", ");
			cur = cur.next;
		}
		res = res.concat(cur.head).concat("]");
		return res;
	}
	
	static void addLast(String s, Node l) {
		Node dummy = new Node("", l);
		
		Node cur = dummy;
		while(cur != null && cur.next != null) {
			cur = cur.next;
		}
		cur.next = new Node(s);
		dummy = null;
	}
	
	static Node copy(Node l) {
		Node dummy = new Node("", l);
		
		Node temp = new Node("");
		Node res = temp;
		for(Node cur = dummy.next; cur != null; cur = cur.next) {
			temp.next = new Node(cur.head);
			temp = temp.next;
		}
		return res.next;
	}
	
	static Node insert(String s, Node l) {
		Node dummy = new Node("", l);
		
		Node cur = dummy, prev = null;
		while(cur != null && cur.head.compareTo(s) <= 0) {
			prev = cur;
			cur = cur.next;	
		}
		Node temp = new Node(s, cur);
		prev.next = temp;
		
		return dummy.next;
	}
	
	static Node insertionSort(Node l) {
		Node dummy = new Node("", l);
		Node cur = dummy.next, prev = dummy;
		String temp = "";
		while(cur != null) {
			temp = cur.head;
			prev.next = null;
			
			dummy = insert(temp, dummy);
			
			prev = cur;
			cur = cur.next;
		}
		
		return dummy.next;
	}
	
	static Node merge(Node l1, Node l2) {
		Node res = new Node("");
		Node cur = res;
		while (l1 != null && l2 != null) {
			if(l1.head.compareTo(l2.head) < 0) {
				cur.next = l1;
				l1 = l1.next;
			}
			else {
				cur.next = l2;
				l2 = l2.next;
			}
			cur = cur.next;
		}
		
		if(l1 != null)
			cur.next = l1;
		else
			cur.next = l2;
		return res.next;
	}
	
	
	
	public static void main(String[] args) {
		Node foobar = new Node("foo", new Node("baz", new Node("bar", null)));
		foobar = insertionSort(foobar);
		System.out.println(makeString(foobar));
	}
}
