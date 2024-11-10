package store.message;

public enum OutputMessage {
    STORE_OVERVIEW_MESSAGE("안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다."),
    ORDER_INSTRUCTION_MESSAGE("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])"),
    FREE_PRODUCT_OFFER_MESSAGE("현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)\n"),
    PROMOTION_CONDITION_NOT_MET_MESSAGE("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)\n"),
    MEMBERSHIP_DISCOUNT_CONFIRMATION_MESSAGE("멤버십 할인을 받으시겠습니까? (Y/N)");
    private final String message;

    OutputMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
