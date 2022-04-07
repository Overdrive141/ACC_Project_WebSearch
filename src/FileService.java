import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileService {
    public static File[] getFiles() {
        List<File> files = getAllFiles(new File(Config.filePath));
        // Convert List to array
        return files.toArray(new File[files.size()]);
    }

    // remove all files in the folder
    public static void clearFiles() {
        removeAllFiles(new File(Config.filePath));
    }

    // Recursive function to get all files in subdirectories
    private static void removeAllFiles(File dir) {
        File[] allFiles = dir.listFiles();
        for (File file : allFiles) {
            if (file.isFile()) {
                // delete the file
                file.delete();
            } else if (file.isDirectory()) {
                removeAllFiles(file);
            }
        }

    }

    // Recursive function to get all files in subdirectories
    private static List<File> getAllFiles(File dir) {
        File[] allFiles = dir.listFiles();
        List<File> files = new ArrayList<File>();
        for (File file : allFiles) {
            if (file.isFile()) {
                if (file.getName().endsWith(".txt")) {
                    files.add(file);
                }
            } else if (file.isDirectory()) {
                files.addAll(getAllFiles(file));
            }
        }
        return files;
    }

    public static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    // return pathname of all files
    public static String[] getFileNames() {
        File[] files = getFiles();
        String[] fileNames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            fileNames[i] = files[i].getPath();
        }
        return fileNames;
    }

    // public static void main(String[] args) {
    // // print all files and directories
    // for (String file : getFileNames()) {
    // System.out.println(file);
    // }

    // clearFiles();

    // }

}
