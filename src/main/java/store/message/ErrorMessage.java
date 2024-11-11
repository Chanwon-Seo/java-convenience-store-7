package store.message;

public enum ErrorMessage {
    FILE_READ_ERROR("파일을 읽는 도중 오류가 발생했습니다."),
    EMPTY_DATA("데이터가 존재하지 않습니다."),
    INVALID_HEADER("헤더 데이터가 일치하지 않습니다."),
    INVALID_DATA_FORMAT("데이터 형식이 올바르지 않습니다."),
    EMPTY_DATE("빈칸이 될 수 없습니다."),
    NON_NUMERIC("정수형이 아닙니다."),
    INVALID_DATE_FORMAT("날짜 표현이 잘못되었습니다."),
    START_DATE_AFTER_END_DATE("시작일이 종료일보다 늦을 수 없습니다."),
    NOT_FOUND_PROMOTION("찾을 수 없는 프로모션 입니다."),
    INVALID_INPUT_FORMAT_ERROR("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    NOT_FOUND_PRODUCT("존재하지 않는 상품입니다. 다시 입력해 주세요."),
    QUANTITY_BELOW_MINIMUM("주문수량이 1이상이어야 합니다."),
    QUANTITY_EXCEEDS_STOCK("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    INSUFFICIENT_STOCK_ERROR("구매할 수량이 없어 다시 선택합니다."),
    ;

    private final String prefix = "[ERROR] ";
    private final String message;

    // 생성자
    ErrorMessage(String message) {
        this.message = message;
    }

    // 메시지를 반환하는 메서드
    public String getMessage() {
        return prefix + message;
    }
}
