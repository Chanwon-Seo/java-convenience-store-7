package store.util;

import static store.message.ErrorMessage.FILE_READ_ERROR;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileReader {

    public List<String> readLinesFromFile(String fileName) {
        Path path = getFilePathFromResources(fileName);
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            throw new IllegalArgumentException(FILE_READ_ERROR.getMessage());
        }
    }

    private Path getFilePathFromResources(String fileName) {
        URL resource = getClass().getClassLoader().getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException(FILE_READ_ERROR.getMessage());
        }

        try {
            return Path.of(resource.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
