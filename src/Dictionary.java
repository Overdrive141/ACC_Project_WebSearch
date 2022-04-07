import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import textprocessing.In;
import textprocessing.TST;

public class Dictionary {

	private static TST<String> dictionary = new TST<String>();

	public static void buildVocabulary(String content) {
		assert content != null;
		String[] words = content.split("[^a-zA-Z0-9]+");
		for (String word : words) {
			word = word.toLowerCase(Locale.ENGLISH);
			if (word.length() > 2) {
				dictionary.put(word, word);
			}
		}
	}

	public static String[] getWordSuggestions(String userInput) {
		// Stores the words from dictionary with their edit distance
		HashMap<String, Integer> map = new HashMap<>();

		userInput = userInput.toLowerCase(Locale.ENGLISH);
		String[] altWords = new String[10];
		for (String word : dictionary.keys()) {
			int editDis = EditDistance.find(userInput, word);
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

	// autocompletes a word from the vocabulary using the dictionary
	public static List<String> autoComplete(String userInput) {
		List<String> result = new ArrayList<String>();
		for (String word : dictionary.prefixMatch(userInput)) {
			result.add(word);
		}
		// return first 10 words as array
		return result.subList(0, Math.min(result.size(), 10));
	}

	public static void main(String[] args) throws IOException {
		for (File f : FileService.getFiles()) {
			Dictionary.buildVocabulary(new In(f).readAll());
		}
		System.out.println("Getting");
		System.out.println(dictionary.size());
		String[] words = getWordSuggestions("windsor");
		System.out.println(Arrays.toString(words));
		List<String> matches = autoComplete("w");
		// print matches in order
		for (String str : matches) {
			System.out.println(str);
		}

	}
}
