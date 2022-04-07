import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {

	final String url;
	final String domain;

	Crawler(String url) {
		if (!url.startsWith("https://")) {
			url = "https://" + url;
		}
		if (!url.endsWith("/")) {
			url += "/";
		}
		// Validate url using regular expression
		if (!url.matches("^(https)://[a-zA-Z0-9-_]+(\\.[a-zA-Z0-9-_]+)+.*$")) {
			System.out.println("Invalid URL - " + url);
			// exit program if invalid url
			System.exit(0);
		}
		// Extract domain from url
		this.domain = url.substring(0, url.indexOf("/", 8));
		this.url = url;
	}

	public HashSet<String> crawl() {
		System.out.println("Started Crawling from " + url);
		HashSet<String> urls = getUrls(url);
		for (String link : urls) {
			htmlToText(link);
		}
		return urls;
	}

	private HashSet<String> getUrls(String url) {
		HashSet<String> urls = new HashSet<String>();

		System.out.println("Domain - " + domain);
		try {
			Document document = Jsoup.connect(url).get();
			Elements links = document.select("a[href]");
			for (Element link : links) {
				// Don't download external links
				if (link.attr("rel").equals("nofollow")) {
					continue;
				}
				String absUrl = link.attr("abs:href");

				// Delete last / from the url
				if (absUrl.endsWith("/")) {
					absUrl = absUrl.substring(0, absUrl.length() - 1);
				}

				// Check if the link is part of the same domain
				if (absUrl.contains(domain)) {
					urls.add(absUrl);
				}
			}
		} catch (Exception e) {
			System.out.println("Couldn't connect to: " + url);

		}
		System.out.println("Found Webpages - " + urls.size());
		// Print found web pages in linear order
		for (String s : urls) {
			System.out.println(s);
		}
		return urls;
	}

	private void htmlToText(String url) {
		try {
			Document document = Jsoup.connect(url).followRedirects(true).get();
			String text = url + "\n" + document.text();
			String folderPath = Config.filePath + "/" + domain.substring(8);
			String fileName = url.replace("/", "").replace(":", "");
			writeToFile(folderPath, fileName, text, ".txt");
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
			String filePath = folderPath + "/" + fileName + ext;
			// rewrite if file exists
			File file = new File(filePath);
			if (file.exists()) {
				file.delete();
			}
			writer = new BufferedWriter(new FileWriter(filePath));
			writer.write(text);
			writer.close();
			return file;
		} finally {
			if (Objects.nonNull(writer)) {
				writer.close();
			}
		}
	}

	public static void main(String[] args) {
		new Crawler("uwindsor.ca").crawl();
		// new Crawler("github.com").crawl();
	}

}
