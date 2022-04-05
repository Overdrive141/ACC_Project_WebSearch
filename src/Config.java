import java.io.File;

public class Config {
    private static String path = System.getProperty("user.dir");
    public static String filePath = new File(path) + "/files/";
}
