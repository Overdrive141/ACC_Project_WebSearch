import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {

	public static HashMap<String, String> links = new HashMap<String, String>();

	public static List<String> crawl(String url) {
		List<String> urls = getUrls(url);
		for (String link : urls) {
			htmlToText(link);
		}
		return urls;
	}

	private static List<String> getUrls(String url) {
		List<String> urls = new ArrayList<>();
		try {
			System.out.println("Started Crawling!!");
			Document document = Jsoup.connect(url).get();
			Elements links = document.select("a[href]");
			for (Element link : links) {
				String s = link.attr("abs:href");
				urls.add(s);
				// String regex = new URL();
				// Pattern p = Pattern.compile(regex);
				// Matcher m = p.matcher(s);
				// while (m.find()) {
				// urls.add(m.group(0));
				// }
			}
		} catch (Exception e) {
			System.out.println("Couldn't connect to: " + url);

		}
		return urls;
	}

	private static void htmlToText(String url) {
		try {
			Document document = Jsoup.connect(url).followRedirects(true).get();
			String text = document.text();
			writeToFile(Config.filePath, url.replace("/", ""), text, ".txt");
		} catch (Exception e) {

			System.out.println(e);
			System.out.println("Crawling forbidden for url: " + url);
		}
	}

	private static File writeToFile(String folderPath, String fileName, String text, String ext) throws IOException {
		BufferedWriter writer = null;
		try {
			File outputFolder = new File(folderPath);
			if (!outputFolder.exists() && !outputFolder.isDirectory()) {
				outputFolder.mkdir();
			}
			writer = new BufferedWriter(new FileWriter(folderPath + fileName + ext));
			writer.write(text);
			writer.close();
			File file = new File(folderPath + fileName + ext);
			System.out.println("Saved: " + file.getAbsolutePath());
			return file;
		} finally {
			if (Objects.nonNull(writer)) {
				writer.close();
			}
		}
	}

	public static void main(String[] args) {
		crawl("https://www.uwindsor.ca/");
	}

}
