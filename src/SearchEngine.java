
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import textprocessing.In;

public class SearchEngine {

	public static void main(String[] args) throws IOException {
		Crawler.crawl("https://www.uwindsor.ca/");
		for (File f : FileService.getFiles()) {
			SpellChecker.buildVocabulary(new In(f).readAll());
		}
		String[] words = SpellChecker.getWordSuggestions("windsor");
		System.out.println(Arrays.toString(words));
	}
}
