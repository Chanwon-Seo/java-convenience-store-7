package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.List;
import store.dto.OrderItemDto;
import store.parser.InputParser;

public class InputView {
    private final InputParser inputParser;

    public InputView() {
        this.inputParser = new InputParser();
    }

    public List<OrderItemDto> getOrderItem() {
        try {
            String input = Console.readLine();
            return inputParser.parse(input);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return getOrderItem();
        }
    }
}
