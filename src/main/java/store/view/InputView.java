package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.List;
import store.dto.CartItemDto;
import store.parser.InputParser;

public class InputView {
    private final InputParser inputParser;

    public InputView() {
        this.inputParser = new InputParser();
    }

    public List<CartItemDto> getOrderItem() {
        try {
            String input = Console.readLine();
            return inputParser.parseOrderItems(input);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return getOrderItem();
        }
    }

    public boolean getYesOrNo() {
        try {
            String input = Console.readLine();
            return inputParser.parseYesOrNo(input);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return getYesOrNo();
        }
    }

}
