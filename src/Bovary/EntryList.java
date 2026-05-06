package Bovary;

public class EntryList {
	Entry head;
	EntryList next;
	
	EntryList(Entry head, EntryList next){
		this.head = head;
		this.next = next;
	}
}
