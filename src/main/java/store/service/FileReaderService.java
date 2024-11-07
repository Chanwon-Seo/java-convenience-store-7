package store.service;

import static store.constants.FileReaderConstants.PRODUCTS_FILE;
import static store.constants.FileReaderConstants.PROMOTIONS_FILE;
import static store.exception.FileReaderException.validateProductHeader;
import static store.exception.FileReaderException.validatePromotionHeader;

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

    public List<String> readProductData() {
        List<String> products = fileReader.readLinesFromFile(PRODUCTS_FILE);
        validateProductHeader(products);
        return removeHeader(products);
    }

    public List<String> readPromotionsData() {
        List<String> promotions = fileReader.readLinesFromFile(PROMOTIONS_FILE);
        validatePromotionHeader(promotions);
        return removeHeader(promotions);
    }

    public List<String> removeHeader(List<String> list) {
        return list.stream().skip(1).toList();
    }

}
