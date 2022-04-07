import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import textprocessing.BoyerMoore;
import textprocessing.TST;

// Inverted Indexing + Frequency Count Per File

public class InvertedIndexing {

	static Map<Integer, String> sources = new HashMap<Integer, String>();
	static HashMap<String, HashMap<String, Integer>> index = new HashMap<String, HashMap<String, Integer>>();
	// word: { url: count: } } 

	public static void buildIndex(String[] files) {
		int i = 0;
		for (String fileName : files) {
			
			try (BufferedReader file = new BufferedReader(new FileReader(fileName))) {
				String url = file.readLine();
				if (url == null) {
					continue;
				}
				sources.put(i, url);
				String ln;
								
				while ((ln = file.readLine()) != null) {
					String[] words = ln.split("\\W+");
					Set<String> set = new HashSet<String>(Arrays.asList(words));
					
					
					
					for (String word : set) {

						word = word.toLowerCase();
						BoyerMoore boyermoore1 = new BoyerMoore(word);
						int count = boyermoore1.search(ln.toLowerCase()); // Count all words in same line with boyerMoore PM
						if (!index.containsKey(word)) {
							HashMap<String, Integer> hm = new HashMap<String, Integer>();
							hm.put(url, count);
							index.put(word, hm); 
						} 
						else {
							HashMap<String, Integer> p = index.get(word);
							if (p.containsKey(url)) { // if url saved already in hash
								int o = p.get(url);
								o++; // we increment count
								p.put(url, o);
							} else {	
								p.put(url, count);
							}

						}
						// index.get(word).add(i);
					}

					System.out.println("Building... " + index);
				}
			} catch (IOException e) {
				System.out.println("File " + fileName + " not found. Skip it");
			}
			i++;
		}

	}

	public static HashMap<String, Integer> find(String phrase) {

		HashMap<String, Integer> res = new HashMap<String, Integer>();

		String[] words = phrase.toLowerCase().split("\\W+"); // matches all characters except alphanumeric characters and _.

		if (!index.containsKey(words[0].toLowerCase())) // if we dont find item return empty
			return res;

//		System.out.println(index.get(words[0].toLowerCase()));

		res = new HashMap<String, Integer>(index.get(words[0].toLowerCase()));

//		for (String word : words) {
//			System.out.println(word + " " + index.get(word) + " " + res);
//			res.keySet().retainAll(index.get(word).keySet()); // get a set view
//			// retain elements that match the response => remove ones that dont match.
//			//used to remove all the array list’s elements that 
//			//are not contained in the specified collection or retains 
//		}

		System.out.println(res);
		if (res.size() == 0) {
			// System.out.println("Not found");
			return res;
		}


		return res;
	}

	public static void main(String args[]) throws IOException {
		try {
			// FileIndex fileIndex = new FileIndex(new
			// String[]{"H:\\UoW\\ACC\\Workspace\\ACC_Project_WebSearch\\src\\httpask.uwindsor.ca.txt","H:\\UoW\\ACC\\Workspace\\ACC_Project_WebSearch\\src\\httpsblackboard.uwindsor.ca.txt"});
			buildIndex(new String[] { "H:\\UoW\\ACC\\Workspace\\ACC_Project_WebSearch\\src\\httpask.uwindsor.ca.txt",
					"H:\\UoW\\ACC\\Workspace\\ACC_Project_WebSearch\\src\\httpsblackboard.uwindsor.ca.txt" });

			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String phrase = in.readLine();

			find(phrase);
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}