
import io.javalin.http.staticfiles.Location;
import textprocessing.In;

import java.io.File;
import java.util.Arrays;

import io.javalin.Javalin;



public class index {
    public static void main(String[] args) {
    	
		for (File f : FileService.getFiles()) {
			SpellChecker.buildVocabulary(new In(f).readAll());

		}
		
		String [] fileNames = FileService.getFileNames();
	    for (int i=0 ; i < fileNames.length; i++) {
			System.out.println(Config.filePath + fileNames[i]);
	    	fileNames[i] = Config.filePath + fileNames[i];
	      }

		System.out.println(Arrays.asList(FileService.getFileNames()));
		InvertedIndexing.buildIndex(fileNames);
    	
    	
      Javalin app = Javalin.create(config -> {
      	config.addStaticFiles("/", Location.CLASSPATH);
		}).start(7070);
      // Run the Javalin Instance on the port 7070
      app.get("/main",ctx->{
      	ctx.render("/GUI/index.html");
      });
      app.get("/common-words",ctx->{
        	ctx.render("/GUI/common.html");
//        	ctx.result(common_words.common());
      });
      // Run the Javalin Instance to build an API for Search
      app.post("/search", ctx -> {
			ctx.result(SearchEngine.search(ctx.formParam("Search_Query")));
		});
  }
}
