package be.pbo.jeugdcup.ranking.domain;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

public class AgeCategoryDetectorTest {

    private static AgeCategoryDetector cut;

    @BeforeAll
    public static void init() {
        cut = new AgeCategoryDetector();
    }

    @Test
    public void test() {
        final Map<String, AgeCategory> expectedResultMap = new HashMap<String, AgeCategory>() {{
            put("Minibad", AgeCategory.MINIBAD);
            put("JE U11", AgeCategory.U11);
            put("JE U15", AgeCategory.U15);
            put("JE U17", AgeCategory.U17U19);
            put("JE U19", AgeCategory.U17U19);
            put(" JE u11 ", AgeCategory.U11);
            put(" minibad ", AgeCategory.MINIBAD);
            put("JE U11-U13 A", AgeCategory.UNKNOWN);
            put("JE U15 A", AgeCategory.U15);
            put("JE U15 A-reeks", AgeCategory.U15);
        }};

        expectedResultMap.forEach((eventName, ageCategory) ->
                MatcherAssert.assertThat("Agecategory should be resolved correctly for eventName '" + eventName + "'",
                        cut.resolveFromEventName(eventName),
                        Matchers.equalTo(ageCategory)));
    }

}