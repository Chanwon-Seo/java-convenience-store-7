package store.parser;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class InputParserTest {
    private final InputParser inputParser = new InputParser();

    @ParameterizedTest
    @MethodSource("provideValidInputs")
    void 유효한_입력_테스트(String input) {
        inputParser.validate(input);
    }

    static Stream<String> provideValidInputs() {
        return Stream.of(
                "[사이다-2],[감자칩-1]",
                "[사이다-2]",
                "[콜라-5],[물-3]"
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidInputs")
    void 유효하지_않은_입력_테스트(String input) {
        assertThrows(IllegalArgumentException.class, () -> inputParser.validate(input));
    }


    static Stream<String> provideInvalidInputs() {
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
}