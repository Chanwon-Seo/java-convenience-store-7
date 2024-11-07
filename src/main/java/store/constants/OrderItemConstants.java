package store.constants;

public class OrderItemConstants {
    public static final String ITEM_SEPARATOR_REGEX = "],\\[";
    public static final char OPEN_BRACKET = '[';
    public static final char CLOSE_BRACKET = ']';
    public static final String OPEN_BRACKET_STR = "[";
    public static final String CLOSE_BRACKET_STR = "]";
    public static final String NESTED_BRACKET_ERROR_REGEX = "\\[\\[";
    public static final String NESTED_BRACKET_CLOSE_ERROR_REGEX = "]]";
    public static final String ITEM_FORMAT_REGEX = "[^\\-]+-\\d+";
    public static final String QUANTITY_SEPARATOR = "-";

    public static final int MIN_INPUT_LENGTH = 5;
}
