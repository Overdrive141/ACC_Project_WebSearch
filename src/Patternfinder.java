import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import textprocessing.In;

public class Patternfinder {

	public static HashMap<String, List<String>> find_phonenumber() {
		HashMap<String, List<String>> map = new HashMap<>();

		String phonepattern = "(\\+1)?[(]?(\\d){3}[)]?[- ]?[(]?(\\d){3}[)]?[- ]?(\\d){4}";
		Pattern r = Pattern.compile(phonepattern);
		File[] allfiles = FileService.getFiles();
		System.out.println("----------------");
		System.out.println("Pattern finder starts:");
		for (int i = 0; i < allfiles.length; i++) {
			if (allfiles[i].isFile()) {
				File oneFile = allfiles[i];

				String[] input_data = new In(oneFile).readAllLines();
				String url = input_data[0];
				for (String temp : input_data) {
					Matcher m1 = r.matcher(temp);
					while (m1.find()) {
						// append the matched url to the map
						if (map.get(url) == null) {
							List<String> list = new ArrayList<>();
							list.add(m1.group());
							map.put(url, list);
						} else {
							List<String> list = map.get(url);
							list.add(m1.group());
							map.put(url, list);
						}
						System.out.println("Found phonenumber from file " + i + ": " + m1.group());
					}
				}
			} else {
				System.out.println("not a file: " + i);
			}
		}
		System.out.println("----------------");

		return map;
	}

	public static HashMap<String, List<String>> find_emailaddress() {
		HashMap<String, List<String>> map = new HashMap<>();

		String emailpattern = "[\\w-]+@([\\w-]+\\.)+[\\w-]+";
		Pattern r = Pattern.compile(emailpattern);
		File[] allfiles = FileService.getFiles();

		System.out.println("----------------");
		System.out.println("Pattern finder starts:");
		for (int i = 0; i < allfiles.length; i++) {
			if (allfiles[i].isFile()) {
				File oneFile = allfiles[i];

				String[] input_data = new In(oneFile).readAllLines();
				String url = input_data[0];
				for (String temp : input_data) {
					Matcher m = r.matcher(temp);
					while (m.find()) {
						// append the matched email to the map
						if (map.get(url) == null) {
							List<String> list = new ArrayList<>();
							list.add(m.group());
							map.put(url, list);
						} else {
							List<String> list = map.get(url);
							list.add(m.group());
							map.put(url, list);
						}
						System.out.println("Found emailaddress from file " + i + ": " + m.group());
					}
				}
			} else {
				System.out.println("not a file: " + i);
			}
		}
		System.out.println("----------------");

		return map;
	}

	public static HashMap<String, List<String>> find_url() {
		HashMap<String, List<String>> map = new HashMap<>();
		String Urlpattern = "http(s)?:\\/\\/[\\w-]+(.){1}(?:[\\w\\-]+)+(.){1}[\\w-._~:/?\\[\\]@!&'\\(\\)\\*\\+,;=]+";
		Pattern r = Pattern.compile(Urlpattern);
		File[] allfiles = FileService.getFiles();

		System.out.println("----------------");
		System.out.println("Pattern finder starts:");
		for (int i = 0; i < allfiles.length; i++) {
			if (allfiles[i].isFile()) {
				File oneFile = allfiles[i];

				String[] input_data = new In(oneFile).readAllLines();
				String url = input_data[0];
				input_data = Arrays.copyOfRange(input_data, 1, input_data.length);
				for (String temp : input_data) {
					Matcher m = r.matcher(temp);
					while (m.find()) {
						// append the matched email to the map
						if (map.get(url) == null) {
							List<String> list = new ArrayList<>();
							list.add(m.group());
							map.put(url, list);
						} else {
							List<String> list = map.get(url);
							list.add(m.group());
							map.put(url, list);
						}
						System.out.println("Found Url from file " + i + ": " + m.group());
					}
				}
			} else {
				System.out.println("not a file: " + i);
			}
		}
		System.out.println("----------------");

		return map;
	}

	public static void main(String args[]) {
		File[] allfiles = FileService.getFiles();
		System.out.println(allfiles.length);
		find_phonenumber();
		find_emailaddress();
		find_url();
	}
}
