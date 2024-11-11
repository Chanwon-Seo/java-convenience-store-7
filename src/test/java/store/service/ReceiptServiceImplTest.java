package store.service;

import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;

import camp.nextstep.edu.missionutils.test.NsTest;
import org.junit.jupiter.api.Test;
import store.Application;

class ReceiptServiceImplTest extends NsTest {

    @Test
    void 영수층_출력_테스트() {
        assertSimpleTest(() -> {
            run("[콜라-3],[에너지바-5]", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈13,000");
        });
    }

    @Test
    void 증정_출력_여부_테스트() {
        assertSimpleTest(() -> {
            run("[콜라-2],[에너지바-5]", "Y", "Y", "N");
            assertThat(output().replaceAll("\\s", "")).contains(
                    "증정",
                    "콜라1",
                    "멤버십할인3,000",
                    "내실돈9,000");
        });
    }

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}