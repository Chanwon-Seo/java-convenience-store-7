package store.util;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class DateUtilTest {

    @Test
    void 날짜_파싱_테스트() {
        String inputDate = "2024-11-10";
        LocalDateTime expectedDate = LocalDate.of(2024, 11, 10).atStartOfDay();

        LocalDateTime parsedDate = DateUtil.dateParse(inputDate);

        assertEquals(expectedDate, parsedDate);
    }

    @Test
    void 파싱할_수_없는_날짜() {
        String invalidDate = "2024-13-10";

        assertThrows(IllegalArgumentException.class, () -> DateUtil.dateParse(invalidDate));
    }

}
