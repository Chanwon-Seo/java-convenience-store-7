package store.util;

import static store.message.ErrorMessage.FILE_READ_ERROR;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileReader {
    private static final String RESOURCES_DIRECTORY = "src/main/resources";

    public List<String> readLinesFromFile(String fileName) {
        try {
            return Files.readAllLines(getFilePath(fileName));
        } catch (IOException e) {
            throw new RuntimeException(FILE_READ_ERROR.getMessage());
        }
    }

    public Path getFilePath(String fileName) {
        return Path.of(RESOURCES_DIRECTORY, fileName);
    }
}
