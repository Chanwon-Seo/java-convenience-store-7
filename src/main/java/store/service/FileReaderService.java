package store.service;

import static store.util.FileReaderConstants.PRODUCTS_FILE;
import static store.util.FileReaderConstants.PROMOTIONS_FILE;

import java.util.List;
import store.dto.StoreInitializationDto;
import store.util.FileReader;

public class FileReaderService {
    private final FileReader fileReader;

    public FileReaderService() {
        this.fileReader = new FileReader();
    }

    public StoreInitializationDto initializeData() {
        List<String> productData = readProductData();
        List<String> promotionsData = readPromotionsData();
        return StoreInitializationDto.of(productData, promotionsData);
    }

    private List<String> readProductData() {
        return fileReader.readLinesFromFile(PRODUCTS_FILE);
    }

    private List<String> readPromotionsData() {
        return fileReader.readLinesFromFile(PROMOTIONS_FILE);
    }
}
