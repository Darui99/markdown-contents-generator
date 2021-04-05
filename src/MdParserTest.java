import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.InvalidPathException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class which tests {@code MdParser}
 */
public class MdParserTest {
    /**
     * Generate contents for given markdown text
     * @param text is {@link String} with markdown text
     * @return {@link String} with result
     */
    private String getContents(final String text) {
        return new MdParser(text).generateContents();
    }


    /**
     * Read data for test from file
     * @param number is number of test
     * @param suffix is "in" if we need input or "out" if we need output
     * @return {@link String} with file's content
     * @throws InvalidPathException if path to file was invalid
     * @throws IOException if were some problems with reading or something similar
     */
    private String readTestData(final int number, final String suffix) throws InvalidPathException, IOException {
        return Common.readFile("src/tests/" +  number + "_" + suffix + ".txt");
    }

    /**
     * Run test with given number
     * @param number is number of test
     * @throws InvalidPathException and
     * @throws IOException
     * if were some problems while getting data for test
     */
    private void runTest(final int number) throws InvalidPathException, IOException {
        String input = readTestData(number, "in");
        String output = readTestData(number, "out");
        assertEquals(output, getContents(input));
    }

    // Tests are splitted to know what test was failed

    @Test
    public void test1() throws InvalidPathException, IOException {
        runTest(1);
    }

    @Test
    public void test2() throws InvalidPathException, IOException {
        runTest(2);
    }

    @Test
    public void test3() throws InvalidPathException, IOException {
        runTest(3);
    }

    @Test
    public void test4() throws InvalidPathException, IOException {
        runTest(4);
    }

    @Test
    public void test5() throws InvalidPathException, IOException {
        runTest(5);
    }
}