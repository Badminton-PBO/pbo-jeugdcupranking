package be.pbo.jeugdcup.ranking.domain;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EliminationSchemeComparatorTest extends DrawTesting {

    @Test
    public void testComparator() {
        teams = createTeams(8);

        final Round round1 = createRound(4, Arrays.asList(
                createMatch(1, 2, S21_0, S21_0, null),
                createMatch(1, 3, S21_0, S21_0, null),
                createMatch(1, 4, S21_0, S21_0, null),
                createMatch(2, 3, S21_0, S21_0, null),
                createMatch(2, 4, S21_0, S21_0, null),
                createMatch(3, 4, S21_0, S21_0, null)
        ));

        final Round round2 = createRound(4, Arrays.asList(
                createMatch(5, 6, S21_0, S21_0, null),
                createMatch(5, 7, S21_0, S21_0, null),
                createMatch(5, 8, S21_0, S21_0, null),
                createMatch(6, 7, S21_0, S21_0, null),
                createMatch(6, 8, S21_0, S21_0, null),
                createMatch(7, 8, S21_0, S21_0, null)
        ));

        final EliminationScheme eliminationScheme1 = createEliminationScheme(1, 2, Arrays.asList(
                createMatch(1, 5, S21_0, S21_0, null)
        ));
        final EliminationScheme eliminationScheme2 = createEliminationScheme(2, 2, Arrays.asList(
                createMatch(2, 6, S21_0, S21_0, null)
        ));
        final EliminationScheme eliminationScheme3 = createEliminationScheme(3, 2, Arrays.asList(
                createMatch(3, 7, S21_0, S21_0, null)
        ));
        final EliminationScheme eliminationScheme4 = createEliminationScheme(4, 2, Arrays.asList(
                createMatch(4, 8, S21_0, S21_0, null)
        ));

        final Event event = createEvent(Arrays.asList(round1, round2), Arrays.asList(eliminationScheme1, eliminationScheme2, eliminationScheme3, eliminationScheme4));

        final EliminationSchemeComparator eliminationSchemeComparator = new EliminationSchemeComparator(event);

        final List<EliminationScheme> listToSort = Arrays.asList(eliminationScheme4, eliminationScheme3, eliminationScheme2, eliminationScheme1);
        listToSort.sort(eliminationSchemeComparator);

        MatcherAssert.assertThat("EliminationScheme must be sorted correctly", listToSort.stream().map(Draw::getId).collect(Collectors.toList()), Matchers.contains(1, 2, 3, 4));

    }

}