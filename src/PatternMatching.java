import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import java.util.Hashtable;

import java.util.regex.Matcher;

public class PatternMatching {

	public static Hashtable<String, Integer> matchPattern(String pattern) throws IOException{

		Hashtable<String, Integer> searchQueries = new Hashtable<String, Integer>();
		
		String joinedText = "";
		
		// get the list of all available files
		// TODO: Loop through hashvalues to patternMatch first 
		// If found then call InvertedIndexing.find. Beter to return from when found.
		String htmlFileList[] = FileService.getFileNames();
		for (int i = 0; i < htmlFileList.length; i++) {
			String fileName = htmlFileList[i];

			// read and save text of file
			joinedText = FileService.readFile(Config.filePath + fileName, StandardCharsets.US_ASCII);
			// Create a Pattern object
			Pattern p1 = Pattern.compile(pattern);

			// Now create matcher object.
			Matcher m = p1.matcher(joinedText);
			
			while (m.find()) 
				searchQueries.merge(fileName, 1, Integer::sum);
			
		}

		return searchQueries;
	}
	
}