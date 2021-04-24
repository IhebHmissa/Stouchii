package stochi.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import stochi.app.web.rest.TestUtil;

class HistoryLineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HistoryLine.class);
        HistoryLine historyLine1 = new HistoryLine();
        historyLine1.setId("id1");
        HistoryLine historyLine2 = new HistoryLine();
        historyLine2.setId(historyLine1.getId());
        assertThat(historyLine1).isEqualTo(historyLine2);
        historyLine2.setId("id2");
        assertThat(historyLine1).isNotEqualTo(historyLine2);
        historyLine1.setId(null);
        assertThat(historyLine1).isNotEqualTo(historyLine2);
    }
}
