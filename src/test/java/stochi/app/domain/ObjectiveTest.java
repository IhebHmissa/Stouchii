package stochi.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import stochi.app.web.rest.TestUtil;

class ObjectiveTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Objective.class);
        Objective objective1 = new Objective();
        objective1.setId("id1");
        Objective objective2 = new Objective();
        objective2.setId(objective1.getId());
        assertThat(objective1).isEqualTo(objective2);
        objective2.setId("id2");
        assertThat(objective1).isNotEqualTo(objective2);
        objective1.setId(null);
        assertThat(objective1).isNotEqualTo(objective2);
    }
}
