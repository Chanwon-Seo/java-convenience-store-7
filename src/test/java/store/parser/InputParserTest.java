package store.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import store.dto.CartItemDto;

class InputParserTest {
    private final InputParser inputParser = new InputParser();

    @ParameterizedTest
    @MethodSource("provideOrderItems")
    void 주문_수량_파싱_테스트(String input, List<CartItemDto> expectedOrderItems) {
        List<CartItemDto> cartItemDtos = inputParser.parseOrderItems(input);

        assertEquals(expectedOrderItems, cartItemDtos);
    }

    static Stream<Arguments> provideOrderItems() {
        return Stream.of(
                Arguments.of(
                        "[물-1]",
                        List.of(new CartItemDto("물", 1))
                ),
                Arguments.of(
                        "[콜라-1],[사이다-1]",
                        List.of(new CartItemDto("콜라", 1), new CartItemDto("사이다", 1))
                )
        );
    }

    @Test
    void 수량이_잘못된_입력_테스트() {
        String input = "[물-물]";
        assertThrows(IllegalArgumentException.class,
                () -> inputParser.parseOrderItems(input));
    }

    @Test
    void 사용자_입력이_Y인_경우_테스트() {
        String input = "Y";

        boolean b = inputParser.parseYesOrNo(input);

        assertTrue(b);
    }

    @Test
    void 사용자_입력이_N인_경우_테스트() {
        String input = "N";

        boolean b = inputParser.parseYesOrNo(input);

        assertFalse(b);
    }

    @ParameterizedTest
    @MethodSource("provideValidOrderItemsInputs")
    void 유효한_주문_입력_테스트(String input) {
        inputParser.validateOrderItems(input);
    }

    static Stream<String> provideValidOrderItemsInputs() {
        return Stream.of(
                "[사이다-2],[감자칩-1]",
                "[사이다-2]",
                "[콜라-5],[물-3]"
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidOrderItemsInputs")
    void 유효하지_않은_주문_입력_테스트(String input) {
        assertThrows(IllegalArgumentException.class,
                () -> inputParser.validateOrderItems(input));
    }


    static Stream<String> provideInvalidOrderItemsInputs() {
        return Stream.of(
                "]1-1[",                   // 닫는 괄호가 먼저 나오는 경우
                "[1-1",                    // 여는 괄호만 있는 경우
                "[[사이다-2],[감자칩-1]",   // 중첩된 여는 괄호
                "[사이다-2],[감자칩-1]]",   // 중첩된 닫는 괄호
                "[[[[[[[[[사이다-2],[감자칩-1]", // 과도한 중첩된 여는 괄호
                "[[사이다-2]]",             // 중첩된 괄호로 둘러싸인 항목
                "사이다-2"                   // 괄호가 없는 경우
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidYNInputs1")
    void 유효하지_않은_예_아니요_입력_테스트(String input) {
        assertThrows(IllegalArgumentException.class,
                () -> inputParser.validateYesOrNo(input));
    }


    static Stream<String> provideInvalidYNInputs1() {
        return Stream.of(
                "Y ", // 공백이 포함된 경우
                "N ",
                " Y",
                "A", //YN이 아닌 경우
                "",
                " ",
                null
        );
    }
}