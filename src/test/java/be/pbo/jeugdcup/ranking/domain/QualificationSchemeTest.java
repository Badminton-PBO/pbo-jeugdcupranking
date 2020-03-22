package be.pbo.jeugdcup.ranking.domain;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class QualificationSchemeTest extends DrawTesting {

    @Test
    public void testConversionWithValidQualificationScheme() {
        teams = createTeams(8);
        final QualificationScheme qualificationScheme = createQualificationScheme(1, 2, Arrays.asList(
                createMatch(1, 2, S21_0, S21_0, null),
                createMatch(3, 4, S21_0, S21_0, null),
                createMatch(5, 6, S21_0, S21_0, null),
                createMatch(7, 8, S21_0, S21_0, null)
        ));
        MatcherAssert.assertThat("", qualificationScheme.isValid(), Matchers.equalTo(Boolean.TRUE));

        final List<EliminationScheme> eliminationSchemes = qualificationScheme.convertToEliminationSchemes();
        final List<String> collect = eliminationSchemes.stream().map(e -> "eliminationId:" + e.getId() + ", match:" + e.getMatches().stream().map(m -> m.getTeam1().getId() + "<->" + m.getTeam2().getId()).collect(Collectors.joining())).collect(Collectors.toList());
        Collections.sort(collect);

        MatcherAssert.assertThat("A QualificationScheme with 8 teams and 4 matches can be converted into a List of EliminationSchemes",
                collect,
                Matchers.contains("eliminationId:1001, match:1<->2",
                        "eliminationId:1002, match:3<->4",
                        "eliminationId:1003, match:5<->6",
                        "eliminationId:1004, match:7<->8"));
    }


    @Test
    public void testConversionWithInValidQualificationScheme() {
        teams = createTeams(8);
        final QualificationScheme qualificationScheme = createQualificationScheme(1, 8, Arrays.asList(
                createMatch(1, 2, S21_0, S21_0, null),
                createMatch(3, 4, S21_0, S21_0, null),
                createMatch(5, 6, S21_0, S21_0, null),
                createMatch(7, 8, S21_0, S21_0, null),

                createMatch(3, 1, S21_0, S21_0, null),
                createMatch(7, 5, S21_0, S21_0, null),

                createMatch(7, 3, S21_0, S21_0, null)
        ));
        MatcherAssert.assertThat("", qualificationScheme.isValid(), Matchers.equalTo(Boolean.FALSE));

        Assertions.assertThrows(RuntimeException.class, qualificationScheme::convertToEliminationSchemes);
    }


    private QualificationScheme createQualificationScheme(final int id, final int size, final List<Match> matches) {
        final QualificationScheme qualificationScheme = createQualificationScheme(id, size);
        matches.forEach(m -> m.setDraw(qualificationScheme));
        qualificationScheme.setMatches(matches);


        return qualificationScheme;
    }


    private QualificationScheme createQualificationScheme(final int id, final int size) {
        final QualificationScheme draw = new QualificationScheme();
        draw.setId(id);
        draw.setName("draw name " + id);
        draw.setSize(size);

        return draw;
    }
}