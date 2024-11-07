package store.service;

import static store.exception.FileReaderException.validateProductHeader;
import static store.exception.FileReaderException.validatePromotionHeader;

import java.util.List;
import store.dto.StoreInitializationDto;
import store.parser.FileReaderParser;
import store.util.FileReader;

public class FileReaderService {
    public static final String PRODUCTS_FILE = "products.md";
    public static final String PROMOTIONS_FILE = "promotions.md";

    private final FileReader fileReader;
    private final FileReaderParser fileReaderParser;

    public FileReaderService() {
        this.fileReader = new FileReader();
        this.fileReaderParser = new FileReaderParser();
    }

    public StoreInitializationDto initializeData() {
        List<String> productData = readProductData();
        List<String> promotionsData = readPromotionsData();
        return StoreInitializationDto.of(productData, promotionsData);
    }

    public List<String> readProductData() {
        List<String> products = fileReader.readLinesFromFile(PRODUCTS_FILE);
        validateProductHeader(products);
        List<String> removeHeaderProducts = removeHeader(products);
        fileReaderParser.parse(removeHeaderProducts);
        return null;
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
