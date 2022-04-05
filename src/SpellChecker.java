import java.io.*;
import java.util.*;

import textprocessing.In;

public class SpellChecker {

	private static Set<String> vocab = new HashSet<String>();

	public static void buildVocabulary(String content) {
		assert content != null;
		StringTokenizer tokenizer = new StringTokenizer(content, "0123456789 ,`*$|~(){}_@><=+[]\\?;/&#-.!:\"'\n\t\r");
		while (tokenizer.hasMoreTokens()) {
			String tk = tokenizer.nextToken().toLowerCase(Locale.ROOT);
			if (!vocab.contains(tk)) {
				vocab.add(tk);
			}
		}
	}

	public static String[] getWordSuggestions(String query) {
		HashMap<String, Integer> map = new HashMap<>();
		String[] altWords = new String[10];
		for (String word : vocab) {
			int editDis = EditDistance.find(query, word);
			map.put(word, editDis);
		}
		Map<String, Integer> sortedMap = sortByValue(map);
		// get top 10 alternative words
		int rank = 0;
		for (Map.Entry<String, Integer> en : sortedMap.entrySet()) {
			altWords[rank] = en.getKey();
			rank++;
			if (rank == 10) {
				break;
			}
		}
		return altWords;
	}

	private static HashMap<String, Integer> sortByValue(HashMap<String, Integer> map) {
		// Create a list from elements of HashMap
		List<Map.Entry<String, Integer>> list = new LinkedList<>(map.entrySet());

		// Sort the list
		list.sort(Map.Entry.comparingByValue());

		// put data from sorted list to hashmap
		HashMap<String, Integer> temp = new LinkedHashMap<>();
		for (Map.Entry<String, Integer> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}

	public static void main(String[] args) throws IOException {
		for (File f : FileService.getFiles()) {
			SpellChecker.buildVocabulary(new In(f).readAll());
		}
		System.out.println("Getting");
		System.out.println(vocab.size());
		String[] words = getWordSuggestions("windsor");
		System.out.println(Arrays.toString(words));
	}
}
