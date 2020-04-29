import javax.swing.text.html.Option;
import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.HashSet;

class IO {

    /**
     * reads a .txt file containing ~ 370k words in English.
     * @param charset: the charset the user would like the file content read as.
     * @return: a set containing all the words,
     * null if file doesn't exist.
     * @throws Exception: URISyntaxException if the URI / path is incorrect,
     * IOException if reading the file fails.
     */
    HashSet<String> readFile(Charset charset) throws Exception
    {
        URI uri = getClass().getResource("/files/dictionaries/words_alpha.txt").toURI();
        File inFile = new File(uri);

        if (!inFile.exists()) { return null; }

        return new HashSet<>(Files.readAllLines(Paths.get(uri), charset));
    }

}
