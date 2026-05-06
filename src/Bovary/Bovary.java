package Bovary;

public class Bovary {
	static HMap buildTable(String[] files, int n) {
		HMap table = new HMap();
		Prefix curPrefix;
		WordReader wr;
		for(String fileName: files) {
			curPrefix = new Prefix(n);
			wr = new WordReader(fileName);
			for (String w = wr.read(); w != null; w = wr.read()) {
			      table.add(curPrefix, w);
			      curPrefix = curPrefix.addShift(w);
			 }
			table.add(curPrefix, "<END>");
		}
		return table;
	}
	
	private static String getRandomAssociatedWord(HMap table, Prefix key) {
		int len = table.find(key).length();
		int index = (int) (Math.random() * len);
		Node cur = table.find(key).content;
		for(int i=0; i < index; i++)
			cur = cur.next;
		return cur.head;
	}
	
	static void generate(HMap t, int n) {
		Prefix curPrefix = new Prefix(n);
		String word = getRandomAssociatedWord(t, curPrefix);
		while(word != "<END>") {
			if(word.matches("\\s*<PAR>\\s*")) System.out.println("\n");
			else System.out.print(word + " ");
			
			curPrefix  = curPrefix.addShift(word);
			word = getRandomAssociatedWord(t, curPrefix);
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		String[] files = new String[35];
		int n = Integer.parseInt(args[0]);
		for(int i=0; i < 35; i++) {
			files[i] = String.format("bovary/%02d.txt", i+1);		
		}
		HMap table = buildTable(files, n);
		generate(table, n);
	}
}