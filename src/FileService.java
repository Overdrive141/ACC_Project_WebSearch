import java.io.File;

public class FileService {
    public static File[] getFiles() {
        return new File(Config.filePath).listFiles();
    }
}
