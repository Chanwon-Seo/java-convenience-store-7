package store.parser;

import static store.constants.OrderItemConstants.CLOSE_BRACKET;
import static store.constants.OrderItemConstants.CLOSE_BRACKET_STR;
import static store.constants.OrderItemConstants.ITEM_FORMAT_REGEX;
import static store.constants.OrderItemConstants.ITEM_SEPARATOR_REGEX;
import static store.constants.OrderItemConstants.MIN_INPUT_LENGTH;
import static store.constants.OrderItemConstants.NESTED_BRACKET_CLOSE_ERROR_REGEX;
import static store.constants.OrderItemConstants.NESTED_BRACKET_ERROR_REGEX;
import static store.constants.OrderItemConstants.OPEN_BRACKET;
import static store.constants.OrderItemConstants.OPEN_BRACKET_STR;
import static store.constants.OrderItemConstants.QUANTITY_SEPARATOR;
import static store.message.ErrorMessage.INVALID_INPUT_FORMAT_ERROR;
import static store.message.ErrorMessage.NOT_FOUND_PRODUCT;

import java.util.ArrayList;
import java.util.List;
import store.domain.OrderItem;
import store.domain.Product;

public class OrderItemParser {

    public static List<OrderItem> parse(String input, List<Product> products) {
        validate(input);
        String[] items = splitItems(input);
        List<OrderItem> orderItems = new ArrayList<>();
        for (String item : items) {
            orderItems.add(parseOrderItem(item, products));
        }
        return orderItems;
    }

    public static void validate(String input) {
        validateInputLength(input);
        validateBrackets(input);
    }

    private static String[] splitItems(String input) {
        String cleanInput = input.substring(1, input.length() - 1);
        return cleanInput.split(ITEM_SEPARATOR_REGEX);
    }

    private static void validateInputLength(String input) {
        if (input.length() < MIN_INPUT_LENGTH) {
            exception();
        }
    }

    private static void validateBrackets(String input) {
        checkBracketsBalance(input);
        checkNestedBrackets(input);
        checkBracketStructure(input);
    }

    private static void checkBracketsBalance(String input) {
        int openBrackets = 0;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (c == OPEN_BRACKET) {
                openBrackets++;
            }

            if (c == CLOSE_BRACKET) {
                openBrackets--;
            }
        }

        if (openBrackets != 0) {
            exception();
        }
    }

    private static void checkNestedBrackets(String input) {
        if (input.contains(NESTED_BRACKET_ERROR_REGEX) ||
                input.contains(NESTED_BRACKET_CLOSE_ERROR_REGEX)) {
            exception();
        }
    }

    private static void checkBracketStructure(String input) {
        if (!(input.startsWith(OPEN_BRACKET_STR) && input.endsWith(
                CLOSE_BRACKET_STR))) {
            exception();
        }
    }

    private static OrderItem parseOrderItem(String item, List<Product> products) {
        if (!item.matches(ITEM_FORMAT_REGEX)) {
            exception();
        }
        String[] itemParts = item.split(QUANTITY_SEPARATOR);
        Product findProduct = findProduct(itemParts[0], products);

        return new OrderItem(findProduct.getName(), Integer.parseInt(itemParts[1]));
    }

    private static Product findProduct(String orderItemName, List<Product> products) {
        for (Product product : products) {
            if (product.isProductName(orderItemName)) {
                return product;
            }
        }
        throw new IllegalArgumentException(NOT_FOUND_PRODUCT.getMessage());
    }

    private static void exception() {
        throw new IllegalArgumentException(INVALID_INPUT_FORMAT_ERROR.getMessage());
    }
}
