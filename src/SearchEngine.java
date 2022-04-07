
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import textprocessing.In;

public class SearchEngine {

	public static String search(String input) throws IOException {
		// Crawler.crawl("https://www.uwindsor.ca/");
		// for (File f : FileService.getFiles()) {
		// SpellChecker.buildVocabulary(new In(f).readAll());
		//
		// }
		//
		// String [] fileNames = FileService.getFileNames();
		// for (int i=0 ; i < fileNames.length; i++) {
		// System.out.println(Config.filePath + fileNames[i]);
		// fileNames[i] = Config.filePath + fileNames[i];
		// }
		//
		// System.out.println(Arrays.asList(FileService.getFileNames()));
		// InvertedIndexing.buildIndex(fileNames);

		TreeMap<String, Map<String, Integer>> response = new TreeMap<String, Map<String, Integer>>();

		Scanner sc = new Scanner(System.in);
		System.out.print("Type something to search: ");
		// String input = sc.nextLine();

		sc.close();

		long startTime = System.nanoTime();
		// Hashtable<String, Integer> searchResults =
		// PatternMatching.matchPattern(input);
		HashMap<String, Integer> searchResults = InvertedIndexing.find(input);
		long endTime = System.nanoTime();

		int totalOccurences = 0;
		for (int occurences : searchResults.values())
			totalOccurences += occurences;

		System.out.println("About " + totalOccurences + " results (" + (endTime - startTime) + ") ns");

		Map<String, Integer> tempHm = new HashMap<String, Integer>();
		tempHm.put("total", totalOccurences);
		response.put("totalOccurrences", tempHm);

		if (searchResults.size() == 0) {
			String[] words = SpellChecker.getWordSuggestions(input);
			System.out.println("Did you mean? " + Arrays.toString(words));

			Map<String, Integer> tempMap = new HashMap<String, Integer>();
			int i = 0;
			for (String word : words) {
				tempMap.put(word, i);
				i++;
			}
			response.put("suggestions", tempMap);
		} else {

			Map<String, Integer> sortedByValueDesc = searchResults.entrySet()
					.stream()
					.sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
							LinkedHashMap::new));
			sortedByValueDesc.forEach((key, value) -> System.out.println(key + "  - occured " + value + " times"));

			response.put("searchResults", sortedByValueDesc);

			// searchResults.forEach((key, value) -> System.out.println(key + " - occured "
			// + value + " times"));
		}

		String json = new Gson().toJson(response);

		return json;

	}

	public static void main(String[] args) throws IOException {
		// Crawler.crawl("https://www.uwindsor.ca/");
		for (File f : FileService.getFiles()) {
			SpellChecker.buildVocabulary(new In(f).readAll());

		}

		String[] fileNames = FileService.getFileNames();
		for (int i = 0; i < fileNames.length; i++) {
			System.out.println(Config.filePath + fileNames[i]);
			fileNames[i] = Config.filePath + fileNames[i];
		}

		System.out.println(Arrays.asList(FileService.getFileNames()));
		InvertedIndexing.buildIndex(fileNames);

		Scanner sc = new Scanner(System.in);
		System.out.print("Type something to search: ");
		String input = sc.nextLine();

		sc.close();

		long startTime = System.nanoTime();
		// Hashtable<String, Integer> searchResults =
		// PatternMatching.matchPattern(input);
		HashMap<String, Integer> searchResults = InvertedIndexing.find(input);
		long endTime = System.nanoTime();

		int totalOccurences = 0;
		for (int occurences : searchResults.values())
			totalOccurences += occurences;

		System.out.println("About " + totalOccurences + " results (" + (endTime - startTime) + ") ns");

		if (searchResults.size() == 0) {
			String[] words = SpellChecker.getWordSuggestions(input);
			System.out.println("Did you mean? " + Arrays.toString(words));
		} else {

			Map<String, Integer> sortedByValueDesc = searchResults.entrySet()
					.stream()
					.sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
							LinkedHashMap::new));
			sortedByValueDesc.forEach((key, value) -> System.out.println(key + "  - occured " + value + " times"));

			// searchResults.forEach((key, value) -> System.out.println(key + " - occured "
			// + value + " times"));
		}

	}
}
