package store;

import camp.nextstep.edu.missionutils.test.NsTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static camp.nextstep.edu.missionutils.test.Assertions.assertNowTest;
import static camp.nextstep.edu.missionutils.test.Assertions.assertSimpleTest;
import static org.assertj.core.api.Assertions.assertThat;

class ApplicationTest extends NsTest {
    @Test
    void 파일에_있는_상품_목록_출력() {
        assertSimpleTest(() -> {
            run("[물-1]", "N", "N");
            assertThat(output()).contains(
                    "- 콜라 1,000원 10개 탄산2+1",
                    "- 콜라 1,000원 10개",
                    "- 사이다 1,000원 8개 탄산2+1",
                    "- 사이다 1,000원 7개",
                    "- 오렌지주스 1,800원 9개 MD추천상품",
                    "- 오렌지주스 1,800원 재고 없음",
                    "- 탄산수 1,200원 5개 탄산2+1",
                    "- 탄산수 1,200원 재고 없음",
                    "- 물 500원 10개",
                    "- 비타민워터 1,500원 6개",
                    "- 감자칩 1,500원 5개 반짝할인",
                    "- 감자칩 1,500원 5개",
                    "- 초코바 1,200원 5개 MD추천상품",
                    "- 초코바 1,200원 5개",
                    "- 에너지바 2,000원 5개",
                    "- 정식도시락 6,400원 8개",
                    "- 컵라면 1,700원 1개 MD추천상품",
                    "- 컵라면 1,700원 10개"
            );
        });
    }

    @Test
    void 여러_개의_일반_상품_구매() {
        assertSimpleTest(() -> {
            run("[비타민워터-3],[물-2],[정식도시락-2]", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈18,300");
        });
    }

    @Test
    void 기간에_해당하지_않는_프로모션_적용() {
        assertNowTest(() -> {
            run("[감자칩-2]", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈3,000");
        }, LocalDate.of(2024, 2, 1).atStartOfDay());
    }

    @Test
    void 예외_테스트() {
        assertSimpleTest(() -> {
            runException("[컵라면-12]", "N", "N");
            assertThat(output()).contains("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        });
    }

    @Test
    void 영수증_출력_테스트() {
        assertSimpleTest(() -> {
            run("[콜라-3],[에너지바-5]", "N", "N");
            assertThat(output().replaceAll("\\s", "")).contains("내실돈13,000");
        });
    }

    @Test
    void 증정_출력_여부_테스트() {
        assertSimpleTest(() -> {
            run("[콜라-3],[에너지바-5]", "Y", "N");
            assertThat(output().replaceAll("\\s", "")).contains(
                    "증정",
                    "콜라1",
                    "총구매액14,000",
                    "행사할인1,000",
                    "멤버십할인3,000",
                    "내실돈10,000");
        });
    }

    @Test
    void 프로모션_혜택_없이_결제하는_경우_테스트() {
        assertSimpleTest(() -> {
            run("[콜라-11],[에너지바-5]", "N", "Y", "N");
            assertThat(output().replaceAll("\\s", "")).contains(
                    "콜라9",
                    "에너지바5",
                    "콜라3",
                    "총구매액22,000",
                    "행사할인3,000",
                    "멤버십할인3,000",
                    "내실돈16,000");
        });
    }

    @Override
    public void runMain() {
        Application.main(new String[]{});
    }
}
