package store.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

class FileReaderTest {
    private static final String PRODUCTS_FILE = "products.md";

    private final FileReader fileReader = new FileReader();

    @Test
    void testReadFile_validLinesFromFile() throws IOException {
        String expectedContent = "name,price,quantity,promotion";

        List<String> lines = fileReader.readLinesFromFile(PRODUCTS_FILE);

        assertNotNull(lines);
        assertEquals(lines.get(0), expectedContent);
    }

    @Test
    void testReadFile_LinesFrom_fileNotFound() {
        String fileName = "not_file_file.md";
        assertThrows(RuntimeException.class, () -> {
            fileReader.readLinesFromFile(fileName);
        });
    }

}