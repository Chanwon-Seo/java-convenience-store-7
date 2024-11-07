package store.service;

import static store.exception.FileReaderException.validateProductHeader;
import static store.exception.FileReaderException.validatePromotionHeader;

import java.util.List;
import store.dto.ProductDto;
import store.dto.PromotionDto;
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

    public StoreInitializationDto initializeStoreData() {
        List<ProductDto> productDtos = readProductData();
        List<PromotionDto> promotionDtos = readPromotionData();
        return StoreInitializationDto.of(productDtos, promotionDtos);
    }

    public List<ProductDto> readProductData() {
        List<String> products = fileReader.readLinesFromFile(PRODUCTS_FILE);
        validateProductHeader(products);
        return fileReaderParser.parseProduct(products);
    }

    public List<PromotionDto> readPromotionData() {
        List<String> promotions = fileReader.readLinesFromFile(PROMOTIONS_FILE);
        validatePromotionHeader(promotions);
        return fileReaderParser.parsePromotion(promotions);
    }

}
