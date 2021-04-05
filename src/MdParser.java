import java.util.Arrays;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Class whose objects parse markdown files and generate its contents
 */
public class MdParser {
    /**
     * Regex for set of markup symbols
     */
    private static final String MARKUP_SYMBOLS_REG = "[*\\-_`]";

    /**
     * Markdown text we want to generate contents for
     */
    private final String text;

    /**
     * Number of symbol which is current for parsing
     */
    private int ptr;

    /**
     * @param text is {@link String} markdown text we want to generate contents for
     */
    public MdParser(final String text) {
        this.text = text;
        ptr = 0;
    }

    /**
     * check if current symbol exists and satisfy given predicate
     * @param predicate is {@link Predicate} which we want to test on current symbol
     * @return true if symbol exists and satisfy the predicate and false otherwise
     */
    private boolean checkSymbol(final Predicate<Character> predicate) {
        return ptr < text.length() && predicate.test(text.charAt(ptr));
    }

    /**
     * skip whitespace symbols in text
     */
    private void skipWhitespace() {
        while (checkSymbol(Character::isWhitespace)) {
            ptr++;
        }
    }

    /**
     * skip symbols until '\n'
     */
    private void skipLine() {
        while (checkSymbol(c -> c != '\n')) {
            ptr++;
        }
        ptr++;
    }

    /**
     * Skip symbols until they are exist and satisfy given predicate
     * @param predicate is {@link Predicate} which we want to test on next symbols
     * @return number of skipped symbols
     */
    private int consumeCharacter(final Predicate<Character> predicate) {
        int res = 0;
        while (checkSymbol(predicate)) {
            ptr++;
            res++;
        }
        return res;
    }

    /**
     * skip symbols until they are exist and equal to given character
     * @param character is char we want next symbols will be equal to
     * @return number of skipped characters
     */
    private int consumeCharacter(final char character) {
        return consumeCharacter(c -> c == character);
    }

    /**
     * Check if next line is header
     * @return -1 if line is not header or level of header (number in range [1-6]) otherwise
     */
    private int isHeader() {
        int level = consumeCharacter('#');
        if (level == 0) {
            return -1;
        }
        int afterSharp = consumeCharacter(c -> Character.isWhitespace(c) && c != '\n');
        if (afterSharp == 0) {
            return -1;
        } else {
            if (level <= 6) {
                return level;
            } else {
                return -1;
            }
        }
    }

    /**
     * @param text is given {@link String}
     * @return {@link String} which is given text without whitespaces in the end
     */
    private String removeSuffixWhitespace(final String text) {
        int ptr = text.length() - 1;
        while (ptr >= 0 && Character.isWhitespace(text.charAt(ptr))) {
            ptr--;
        }
        return text.substring(0, ptr + 1);
    }

    /**
     * @return {@link String} which is sequence of symbols from current to nearest '\n'
     */
    private String getLine() {
        StringBuilder res = new StringBuilder();
        while (checkSymbol(c -> c != '\n')) {
            res.append(text.charAt(ptr++));
        }
        return res.toString();
    }

    /**
     * Formatting header's name for link usage
     * @param header is {@link String} with header's name
     * @return {@link String} where all symbols are in lower case,
     * markup symbols are removed and all words joining with '-'
     */
    private String formatHeader(final String header) {
        return Arrays.stream(header.split("\\s+"))
                                   .map(s -> s.replaceAll(MARKUP_SYMBOLS_REG, ""))
                                   .map(String::toLowerCase)
                                   .collect(Collectors.joining("-"));
    }

    /**
     * Generate contents for {@code text} (field of class)
     * @return {@link String} with result
     */
    public String generateContents() {
        StringBuilder contents = new StringBuilder();
        Stack<Integer> headersLevels = new Stack<>();
        while (ptr < text.length()) {
            skipWhitespace();
            int headerLevel = isHeader();
            if (headerLevel == -1) {
                skipLine();
                continue;
            }
            String headerName = removeSuffixWhitespace(getLine());
            if (headerName.isEmpty()) {
                skipLine();
                continue;
            }
            while (headersLevels.size() < headerLevel) {
                headersLevels.push(0);
            }
            while (headersLevels.size() > headerLevel) {
                headersLevels.pop();
            }
            headersLevels.push(headersLevels.pop() + 1);

            contents.append("    ".repeat(headersLevels.size() - 1))
                    .append(headersLevels.peek().toString()).append(". ")
                    .append("[").append(headerName).append("]")
                    .append("(#").append(formatHeader(headerName)).append(")")
                    .append("\n");
        }
        return contents.toString();
    }
}
