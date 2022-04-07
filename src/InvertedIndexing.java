import java.io.*;
import java.util.*;

import textprocessing.TST;

public class InvertedIndexing {

	static Map<Integer, String> sources = new HashMap<Integer, String>();
	static HashMap<String, HashMap<String, Integer>> index = new HashMap<String, HashMap<String, Integer>>();
	// Word: {sourceDir: occurence}

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
					for (String word : words) {
						word = word.toLowerCase();
						if (!index.containsKey(word)) {
							HashMap<String, Integer> hm = new HashMap<String, Integer>();
							hm.put(url, 1);
							index.put(word, hm);
						} else {
							HashMap<String, Integer> p = index.get(word);
							if (p.containsKey(url)) {
								int o = p.get(url);
								o++;
								p.put(url, o);
							} else {
								p.put(url, 1);
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

		// Hashtable<Integer, String> searchResults = new Hashtable<Integer, String>();

		HashMap<String, Integer> res = new HashMap<String, Integer>();

		String[] words = phrase.toLowerCase().split("\\W+");

		if (!index.containsKey(words[0].toLowerCase()))
			return res;

		System.out.println(index.get(words[0].toLowerCase()));

		res = new HashMap<String, Integer>(index.get(words[0].toLowerCase()));

		for (String word : words) {
			System.out.println(word + " " + index.get(word) + " " + res);
			// res.retainAll(index.get(word)); // We retain all indexes of all words
			res.keySet().retainAll(index.get(word).keySet());
		}

		System.out.println(res);
		if (res.size() == 0) {
			// System.out.println("Not found");
			return res;
		}

		// System.out.println("Found in: ");
		// for(int num : res.){
		// for(String num : res.keySet())
		// searchResults.put(num, res.);
		//// System.out.println("\t"+sources.get(num));
		// }

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