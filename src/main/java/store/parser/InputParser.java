package store.parser;

import static store.message.ErrorMessage.INVALID_INPUT_FORMAT_ERROR;

import java.util.ArrayList;
import java.util.List;
import store.dto.OrderItemDto;

public class InputParser {
    private static final String ITEM_SEPARATOR_REGEX = "],\\[";
    private static final char OPEN_BRACKET = '[';
    private static final char CLOSE_BRACKET = ']';
    private static final String OPEN_BRACKET_STR = "[";
    private static final String CLOSE_BRACKET_STR = "]";
    private static final String NESTED_BRACKET_ERROR_REGEX = "\\[\\[";
    private static final String NESTED_BRACKET_CLOSE_ERROR_REGEX = "]]";
    private static final String ITEM_FORMAT_REGEX = "[^\\-]+-\\d+";
    private static final String QUANTITY_SEPARATOR = "-";

    private static final int MIN_INPUT_LENGTH = 5;

    public List<OrderItemDto> parse(String input) {
        validate(input);
        String[] items = splitItems(input);
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        for (String item : items) {
            orderItemDtos.add(parseOrderItem(item));
        }
        return orderItemDtos;
    }

    public void validate(String input) {
        validateInputLength(input);
        validateBrackets(input);
    }

    private String[] splitItems(String input) {
        String cleanInput = input.substring(1, input.length() - 1);
        return cleanInput.split(ITEM_SEPARATOR_REGEX);
    }

    private void validateInputLength(String input) {
        if (input.length() < MIN_INPUT_LENGTH) {
            exception();
        }
    }

    private void validateBrackets(String input) {
        checkBracketsBalance(input);
        checkNestedBrackets(input);
        checkBracketStructure(input);
    }

    private void checkBracketsBalance(String input) {
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

    private void checkNestedBrackets(String input) {
        if (input.contains(NESTED_BRACKET_ERROR_REGEX) ||
                input.contains(NESTED_BRACKET_CLOSE_ERROR_REGEX)) {
            exception();
        }
    }

    private void checkBracketStructure(String input) {
        if (!(input.startsWith(OPEN_BRACKET_STR) && input.endsWith(
                CLOSE_BRACKET_STR))) {
            exception();
        }
    }

    private OrderItemDto parseOrderItem(String item) {
        if (!item.matches(ITEM_FORMAT_REGEX)) {
            exception();
        }
        String[] itemParts = item.split(QUANTITY_SEPARATOR);
        return OrderItemDto.toOrderItemDto(itemParts[0], Integer.parseInt(itemParts[1]));
    }

    private void exception() {
        throw new IllegalArgumentException(INVALID_INPUT_FORMAT_ERROR.getMessage());
    }
}
