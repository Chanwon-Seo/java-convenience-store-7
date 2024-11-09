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
    private static final int MIN_ORDER_ITEMS_INPUT_LENGTH = 5;

    private static final char YES = 'Y';
    private static final char NO = 'N';
    private static final int YES_OR_NO_INPUT_LENGTH = 1;

    public List<OrderItemDto> parseOrderItems(String input) {
        validateOrderItems(input);
        String[] items = splitItems(input);
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        for (String item : items) {
            orderItemDtos.add(convertToOrderItemDtos(item));
        }
        return orderItemDtos;
    }

    public boolean parseYesOrNo(String input) {
        validateYesOrNo(input);
        return input.charAt(YES_OR_NO_INPUT_LENGTH - 1) == YES;
    }

    public void validateOrderItems(String input) {
        validateInputLength(input);
        validateBrackets(input);
    }

    public void validateYesOrNo(String input) {
        validateEmpty(input);
        validateYNLength(input);
        validateUppercase(input);
        validateYN(input);
    }

    private void validateUppercase(String input) {
        if (!Character.isUpperCase(input.charAt(0))) {
            exception();
        }
    }

    private void validateYN(String input) {
        char YN = input.charAt(YES_OR_NO_INPUT_LENGTH - 1);
        if (YN == YES || YN == NO) {
            return;
        }
        exception();
    }

    private String[] splitItems(String input) {
        String cleanInput = input.substring(1, input.length() - 1);
        return cleanInput.split(ITEM_SEPARATOR_REGEX);
    }

    private void validateInputLength(String input) {
        if (input.length() < MIN_ORDER_ITEMS_INPUT_LENGTH) {
            exception();
        }
    }

    private void validateEmpty(String input) {
        if (input == null || input.isEmpty()) {
            exception();
        }
    }

    private void validateYNLength(String input) {
        if (input.length() != YES_OR_NO_INPUT_LENGTH) {
            exception();
        }
    }

    private void validateBrackets(String input) {
        checkBracketsBalance(input);
        checkNestedBrackets(input);
        checkBracketStructure(input);
    }

    private void checkBracketsBalance(String input) {
        int openBrackets = countOpenBrackets(input);
        if (openBrackets != 0) {
            exception();
        }
    }

    private int countOpenBrackets(String input) {
        int openBrackets = 0;
        for (char c : input.toCharArray()) {
            if (c == OPEN_BRACKET) {
                openBrackets++;
            }
            if (c == CLOSE_BRACKET) {
                openBrackets--;
            }
        }
        return openBrackets;
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

    private OrderItemDto convertToOrderItemDtos(String item) {
        if (!item.matches(ITEM_FORMAT_REGEX)) {
            exception();
        }
        String[] itemParts = item.split(QUANTITY_SEPARATOR);
        return new OrderItemDto(itemParts[0], Integer.parseInt(itemParts[1]));
    }

    private void exception() {
        throw new IllegalArgumentException(INVALID_INPUT_FORMAT_ERROR.getMessage());
    }
}
