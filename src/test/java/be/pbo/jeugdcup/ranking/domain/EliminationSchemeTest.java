package be.pbo.jeugdcup.ranking.domain;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.stream.Collectors;

public class EliminationSchemeTest extends DrawTesting {

    private EliminationScheme eliminationScheme;

    @Test
    public void sortWhenFirstTeamOutOfTwoTeamsWon() {
        teams = createTeams(2);
        eliminationScheme = createEliminationScheme(2, Arrays.asList(
                createMatch(1, 2, S21_0, S21_0, null)
        ));

        final SortedMap<Integer, List<Team>> teamsSortedByEliminationResult = eliminationScheme.getTeamsSortedByEliminationResult();
        MatcherAssert.assertThat("Sorting elimination schema based on results.",
                mapToTeamIdPlusOrder(teamsSortedByEliminationResult),
                Matchers.contains("t1_1", "t2_0"));
    }

    @Test
    public void sortWhenSecondTeamOutOfTwoTeamsWon() {
        teams = createTeams(2);
        eliminationScheme = createEliminationScheme(2, Arrays.asList(
                createMatch(1, 2, S21_0, S0_21, S10_21)
        ));

        final SortedMap<Integer, List<Team>> teamsSortedByEliminationResult = eliminationScheme.getTeamsSortedByEliminationResult();
        MatcherAssert.assertThat("Sorting elimination schema based on results.",
                mapToTeamIdPlusOrder(teamsSortedByEliminationResult),
                Matchers.contains("t2_1", "t1_0"));
    }


    @Test
    public void sortWithFourTeams() {
        teams = createTeams(4);
        eliminationScheme = createEliminationScheme(4, Arrays.asList(
                createMatch(1, 2, S21_0, S21_0, null),
                createMatch(3, 4, S21_0, S21_0, null),

                createMatch(3, 1, S21_0, S21_0, null)
        ));

        final SortedMap<Integer, List<Team>> teamsSortedByEliminationResult = eliminationScheme.getTeamsSortedByEliminationResult();
        MatcherAssert.assertThat("Sorting elimination schema based on results.",
                mapToTeamIdPlusOrder(teamsSortedByEliminationResult),
                Matchers.contains("t3_2", "t1_1", "t2_0", "t4_0"));
    }

    @Test
    public void sortWithEightTeams() {
        teams = createTeams(8);
        eliminationScheme = createEliminationScheme(8, Arrays.asList(
                createMatch(1, 2, S21_0, S21_0, null),
                createMatch(3, 4, S21_0, S21_0, null),
                createMatch(5, 6, S21_0, S21_0, null),
                createMatch(7, 8, S21_0, S21_0, null),

                createMatch(3, 1, S21_0, S21_0, null),
                createMatch(7, 5, S21_0, S21_0, null),

                createMatch(7, 3, S21_0, S21_0, null)
        ));

        final SortedMap<Integer, List<Team>> teamsSortedByEliminationResult = eliminationScheme.getTeamsSortedByEliminationResult();
        MatcherAssert.assertThat("Sorting elimination schema based on results.",
                mapToTeamIdPlusOrder(teamsSortedByEliminationResult),
                Matchers.contains("t7_3", "t3_2", "t1_1", "t5_1", "t2_0", "t4_0", "t6_0", "t8_0"));
    }

    private List<String> mapToTeamIdPlusOrder(final SortedMap<Integer, List<Team>> teamsSortedByEliminationResult) {
        return teamsSortedByEliminationResult.keySet().stream()
                .flatMap(integer -> teamsSortedByEliminationResult.get(integer).stream()
                        .map(t -> t.getId())
                        .sorted()
                        .map(teamId -> "t" + teamId + "_" + integer))
                .collect(Collectors.toList());
    }
}