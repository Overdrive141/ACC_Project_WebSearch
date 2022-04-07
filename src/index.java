import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

import com.google.gson.Gson;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import textprocessing.In;

public class index {
	static boolean isReady = false;

	public static void main(String[] args) {
		
//		Crawler c = new Crawler("https://uwindsor.ca");
//		c.crawl();
		
		for (File f : FileService.getFiles()) {
			Dictionary.buildVocabulary(new In(f).readAll());
		}

		String[] fileNames = FileService.getFileNames();
		InvertedIndexing.buildIndex(fileNames);

		Javalin app = Javalin.create(config -> {
			config.addStaticFiles("/", Location.CLASSPATH);
			config.enableCorsForAllOrigins();
			config.enableDevLogging();
		}).start(7070);
		// Run the Javalin Instance on the port 7070
		app.get("/", ctx -> {
			ctx.render("/GUI/index.html");
		});
		app.get("/common-words", ctx -> {
			var data = ctx.json(common_words.common());
			ctx.render("/GUI/common.html", Collections.singletonMap("data", data));
		});

		app.get("/common", ctx -> {
			String json = new Gson().toJson(common_words.common());
			ctx.result(json);
		});

		app.get("/pattern", ctx -> {
			Map<String, Map<String, List<String>>> map = new HashMap<String, Map<String, List<String>>>();
			map.put("phone", Patternfinder.find_phonenumber());
			map.put("email", Patternfinder.find_emailaddress());
			map.put("url", Patternfinder.find_url());
			String json = new Gson().toJson(map);
			ctx.result(json);
		});

		app.post("/build", ctx -> {
			isReady = false;
			String urls = ctx.formParam("urls");
			System.out.println(urls);
			ctx.status(200);
			String[] urls_array = urls.split(",");
			// trim urls_array
			for (int i = 0; i < urls_array.length; i++) {
				urls_array[i] = urls_array[i].trim();
			}
			// crawl each url
			for (String url : urls_array) {
				new Crawler(url).crawl();
			}
			// build vocabulary
			for (File f : FileService.getFiles()) {
				Dictionary.buildVocabulary(new In(f).readAll());
			}
			// build index
			InvertedIndexing.buildIndex(FileService.getFileNames());
			isReady = true;
			// send ok status
			ctx.status(200);
		});

		app.get("/status", ctx -> {
			ctx.status(isReady ? 200 : 201);
		});

		app.post("/reset", ctx -> {
			FileService.clearFiles();
			ctx.status(200);
		});

		// Run the Javalin Instance to build an API for Search
		app.post("/search", ctx -> {
			ctx.result(SearchEngine.search(ctx.formParam("Search_Query")));
		});
		app.post("/complete", ctx -> {
			String query = ctx.formParam("query");
			List<String> suggestions = Dictionary.autoComplete(query);
			Map<String, List<String>> map = new HashMap<String, List<String>>();
			map.put("data", suggestions);
			String json = new Gson().toJson(map);
			ctx.result(json);
		});
	}
}
