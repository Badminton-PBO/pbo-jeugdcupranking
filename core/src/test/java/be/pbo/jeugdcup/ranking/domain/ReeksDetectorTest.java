package be.pbo.jeugdcup.ranking.domain;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

public class ReeksDetectorTest {

    private static ReeksDetector cut;

    @BeforeAll
    public static void init() {
        cut = new ReeksDetector();
    }

    @Test
    public void test() {
        final Map<String, Reeks> expectedResultMap = new HashMap<String, Reeks>() {{
            put("Minibad", Reeks.NA);
            put("MINIBAD", Reeks.NA);
            put("JE U11", Reeks.NA);
            put("JE U11-U13 A", Reeks.A_REEKS);
            put("JE U15 A", Reeks.A_REEKS);
            put("JE U15 a", Reeks.A_REEKS);
            put("JE U15 B", Reeks.B_REEKS);
            put("JE U15 A-reeks", Reeks.A_REEKS);
            put("JE U15 A-REEKS", Reeks.A_REEKS);
            put("JE U15 A_REEKS", Reeks.A_REEKS);
            put("JE U15 A REEKS", Reeks.A_REEKS);
        }};

        expectedResultMap.forEach((eventName, ageCategory) ->
                MatcherAssert.assertThat("Reeks should be resolved correctly for eventName '" + eventName + "'",
                        cut.resolveFromEventName(eventName),
                        Matchers.equalTo(ageCategory)));
    }

}