import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Class which has implementations of common functions
 */
public class Common {
    /**
     * Read file's content
     * @param pathToFile is {@link String} which represent path to file
     * @return {@link String} with file's content
     * @throws InvalidPathException if path is invalid
     * @throws IOException if there are some problems with reading or something similar
     */
    public static String readFile(final String pathToFile) throws InvalidPathException, IOException {
        Path path = Paths.get(pathToFile);
        try (BufferedReader in = Files.newBufferedReader(path)) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        }
    }
}
