package store.util;

import static store.message.ErrorMessage.FILE_READ_ERROR;
import static store.util.FileReaderConstants.RESOURCES_DIRECTORY;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileReader {

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
