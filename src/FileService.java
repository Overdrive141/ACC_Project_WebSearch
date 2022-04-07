import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileService {
    public static File[] getFiles() {
        return new File(Config.filePath).listFiles();
    }
    
    public static String readFile(String path, Charset encoding) throws IOException {
    	byte[] encoded = Files.readAllBytes(Paths.get(path));
    	return new String(encoded, encoding);
    }
    
    public static String[] getFileNames() {
    	return new File(Config.filePath).list(); // List of all files and directories
    } 
   
}
