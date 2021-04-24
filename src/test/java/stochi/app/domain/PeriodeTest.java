package stochi.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import stochi.app.web.rest.TestUtil;

class PeriodeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Periode.class);
        Periode periode1 = new Periode();
        periode1.setId("id1");
        Periode periode2 = new Periode();
        periode2.setId(periode1.getId());
        assertThat(periode1).isEqualTo(periode2);
        periode2.setId("id2");
        assertThat(periode1).isNotEqualTo(periode2);
        periode1.setId(null);
        assertThat(periode1).isNotEqualTo(periode2);
    }
}
