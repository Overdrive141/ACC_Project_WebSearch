import java.io.File;

import io.javalin.http.staticfiles.Location;
import io.javalin.Javalin;

import textprocessing.In;

public class index {
	public static void main(String[] args) {
		for (File f : FileService.getFiles()) {
			SpellChecker.buildVocabulary(new In(f).readAll());
		}

		String[] fileNames = FileService.getFileNames();
		InvertedIndexing.buildIndex(fileNames);

		Javalin app = Javalin.create(config -> {
			config.addStaticFiles("/", Location.CLASSPATH);
		}).start(7070);
		// Run the Javalin Instance on the port 7070
		app.get("/", ctx -> {
			ctx.render("/GUI/index.html");
		});
		app.get("/common-words", ctx -> {
			ctx.render("/GUI/common.html");
		});
		// Run the Javalin Instance to build an API for Search
		app.post("/search", ctx -> {
			ctx.result(SearchEngine.search(ctx.formParam("Search_Query")));
		});
	}
}
