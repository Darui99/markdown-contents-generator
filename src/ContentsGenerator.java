import java.io.IOException;
import java.nio.file.*;

/**
 * Class with {@code main} to make interaction via command line
 * There is one argument which is path to file with markdown text.
 * Generate contents for this file and print it with original text
 */
public class ContentsGenerator {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("One argument required: path to markdown file");
            return;
        }
        if (args[0] == null) {
            System.out.println("Null argument");
            return;
        }

        try {
            String markdownText = Common.readFile(args[0]);
            String generatedContests = new MdParser(markdownText).generateContents();
            System.out.println(generatedContests);
            System.out.print(markdownText);
        } catch (InvalidPathException e) {
            System.out.println("Invalid path: " + e.getMessage());
        } catch (NoSuchFileException e) {
            System.out.println("No such file: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error while reading file: " + e.getMessage());
        }
    }
}
