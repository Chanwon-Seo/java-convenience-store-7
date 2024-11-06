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
    NOT_FOUND_PROMOTION("찾을 수 없는 프로모션 입니다.");

    private final String message;

    // 생성자
    ErrorMessage(String message) {
        this.message = message;
    }

    // 메시지를 반환하는 메서드
    public String getMessage() {
        return message;
    }
}
