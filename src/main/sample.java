package main;

/**
 * Possible List of Features
 * 
 * Crawler (Jsoup) / Hashset
 * HTML to Text (Jsoup)
 * Pattern Searching (BoyerMoore, KMP, etc) - Compare input against text files.
 * Dictionary / AutoSuggestion (Trie) - Store words from dictionary into trie and then retrieve from suggested words.
 * Frequency Count - No. of times a searched keyword appears in text files. Can use self balancing BST. use Regex Pattern to match chars.
 * Spell Checker (Edit Distance) - Possibly check word in dictionary first, if not found then Edit Dist algorithm to suggest similar words.
 * Page Ranking
 * 
 * Basic Flow: Crawl -> HTML to Text -> User Search => 1. Pattern Search | 2. Auto Suggest | 3. Spell Checker
 * 
 */

public class sample {
	public static void main(String[] args) {
		System.out.println("Testing");
	}
}
