package be.pbo.jeugdcup.ranking.domain;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RoundTest extends DrawTesting {

    private Round round;

    @Test
    public void isValidWhenAllMatchersAreIn() {
        teams = createTeams(4);
        round = createRound(4, Arrays.asList(
                createMatch(1, 2, S21_0, S21_0, null),
                createMatch(1, 3, S21_0, S21_0, null),
                createMatch(1, 4, S21_0, S21_0, null),
                createMatch(2, 3, S21_0, S21_0, null),
                createMatch(2, 4, S21_0, S21_0, null),
                createMatch(3, 4, S21_0, S21_0, null)
        ));
        MatcherAssert.assertThat("Round should be valid", round.isValid(), Matchers.equalTo(Boolean.TRUE));
    }

    @Test
    public void isInValidWhenAllMatchersAreIn() {
        teams = createTeams(4);
        round = createRound(4, Arrays.asList(
                createMatch(1, 2, S21_0, S21_0, null),
                createMatch(1, 3, S21_0, S21_0, null),
                createMatch(1, 4, S21_0, S21_0, null),
                createMatch(2, 3, S21_0, S21_0, null),
                createMatch(2, 4, S21_0, S21_0, null)
        ));
        MatcherAssert.assertThat("Round should be invalid", round.isValid(), Matchers.equalTo(Boolean.FALSE));
    }

    @Test
    public void getAllTeams() {
        teams = createTeams(4);
        round = createRound(4, Arrays.asList(
                createMatch(1, 2, S21_0, S21_0, null),
                createMatch(1, 3, S21_0, S21_0, null),
                createMatch(1, 4, S21_0, S21_0, null),
                createMatch(2, 3, S21_0, S21_0, null),
                createMatch(2, 4, S21_0, S21_0, null),
                createMatch(3, 4, S21_0, S21_0, null)
        ));

        MatcherAssert.assertThat("Teams are getting extracted", round.getAllTeams(),
                Matchers.containsInAnyOrder(
                        Matchers.equalTo(teams.get(0)),
                        Matchers.equalTo(teams.get(1)),
                        Matchers.equalTo(teams.get(2)),
                        Matchers.equalTo(teams.get(3))
                )
        );
    }

    @Test
    public void sortByNumberOfWonMatches_1() {
        teams = createTeams(4);
        round = createRound(4, Arrays.asList(
                createMatch(1, 2, S21_0, S21_0, null),
                createMatch(1, 3, S21_0, S21_0, null),
                createMatch(1, 4, S21_0, S21_0, null),
                createMatch(2, 3, S21_0, S21_0, null),
                createMatch(2, 4, S21_0, S21_0, null),
                createMatch(3, 4, S21_0, S21_0, null)
        ));

        final List<Team> teamsSortedByPouleResult = round.getTeamsSortedByPouleResult();
        MatcherAssert.assertThat("De rangschikking van een pool wordt opgemaakt naar het aantal gewonnen wedstrijden.",
                teamsSortedByPouleResult.stream().map(Team::getId).collect(Collectors.toList()),
                Matchers.contains(1, 2, 3, 4));
    }

    @Test
    public void sortByNumberOfWonMatches_2() {
        teams = createTeams(4);
        round = createRound(4, Arrays.asList(
                createMatch(1, 2, S21_0, S21_0, null),
                createMatch(1, 3, S21_0, S21_0, null),
                createMatch(1, 4, S21_0, S21_0, null),
                createMatch(2, 3, S21_0, S21_0, null),
                createMatch(2, 4, S21_0, S21_0, null),
                createMatch(3, 4, S0_21, S0_21, null)
        ));

        final List<Team> teamsSortedByPouleResult = round.getTeamsSortedByPouleResult();
        MatcherAssert.assertThat("De rangschikking van een pool wordt opgemaakt naar het aantal gewonnen wedstrijden.",
                teamsSortedByPouleResult.stream().map(Team::getId).collect(Collectors.toList()),
                Matchers.contains(1, 2, 4, 3));
    }


    @Test
    public void sortByNumberOfWonMatches_3() {
        teams = createTeams(4);
        round = createRound(4, Arrays.asList(
                createMatch(1, 2, S0_21, S0_21, null),
                createMatch(1, 3, S21_0, S21_0, null),
                createMatch(1, 4, S21_0, S21_0, null),
                createMatch(2, 3, S21_0, S21_0, null),
                createMatch(2, 4, S21_0, S21_0, null),
                createMatch(3, 4, S0_21, S0_21, null)
        ));

        final List<Team> teamsSortedByPouleResult = round.getTeamsSortedByPouleResult();
        MatcherAssert.assertThat("De rangschikking van een pool wordt opgemaakt naar het aantal gewonnen wedstrijden.",
                teamsSortedByPouleResult.stream().map(Team::getId).collect(Collectors.toList()),
                Matchers.contains(2, 1, 4, 3));

    }

    @Test
    public void whenTwoTeamsWonEqualNumberOfMatches() {
        teams = createTeams(5);
        round = createRound(5, Arrays.asList(
                createMatch(1, 2, S0_21, S0_21, null),
                createMatch(1, 3, S21_0, S21_0, null),
                createMatch(1, 4, S21_0, S21_0, null),
                createMatch(1, 5, S21_0, S21_0, null),
                createMatch(2, 3, S21_0, S21_0, null),
                createMatch(2, 4, S21_0, S21_0, null),
                createMatch(2, 5, S0_21, S0_21, null),
                createMatch(3, 4, S21_0, S21_0, null),
                createMatch(3, 5, S0_21, S0_21, null),
                createMatch(4, 5, S21_0, S21_0, null)
        ));

        final List<Team> teamsSortedByPouleResult = round.getTeamsSortedByPouleResult();
        MatcherAssert.assertThat("Wanneer twee spelers/paren een zelfde aantal wedstrijden hebben gewonnen, wordt de winnaar van de onderlinge wedstrijd hoogst gerangschikt.",
                teamsSortedByPouleResult.stream().map(Team::getId).collect(Collectors.toList()),
                Matchers.contains(2, 1, 5, 3, 4)); // 3 & 4 still TODO
    }

    @Test
    public void whenMoreThanTwoTeamsWonEqualNumberOfMatches() {
        teams = createTeams(4);
        round = createRound(4, Arrays.asList(
                createMatch(1, 2, S21_0, S0_21, S21_0),
                createMatch(1, 3, S21_0, S21_0, null),
                createMatch(1, 4, S0_21, S0_21, null),
                createMatch(2, 3, S21_0, S21_0, null),
                createMatch(2, 4, S21_0, S0_21, S21_0),
                createMatch(3, 4, S0_21, S0_21, null)
        ));

        final List<Team> teamsSortedByPouleResult = round.getTeamsSortedByPouleResult();
        MatcherAssert.assertThat("Wanneer drie of meer spelers/paren een zelfde aantal wedstrijden hebben gewonnen, zal de rangschikking worden opgemaakt naar het verschil tussen het totaal gewonnen games en het aantal verloren games (= saldo van de games)",
                teamsSortedByPouleResult.stream().map(Team::getId).collect(Collectors.toList()),
                Matchers.contains(4, 2, 1, 3));

    }

    @Test
    public void whenMoreThan2TeamsHaveSameGameSaldo() {
        teams = createTeams(4);
        round = createRound(4, Arrays.asList(
                createMatch(1, 2, S21_10, S0_21, S21_10),
                createMatch(1, 3, S21_0, S21_0, null),
                createMatch(1, 4, S21_0, S0_21, S0_21),
                createMatch(2, 3, S21_0, S21_10, null),
                createMatch(2, 4, S21_0, S0_21, S21_0),
                createMatch(3, 4, S0_21, S0_21, null)
        ));

        MatcherAssert.assertThat(round.wonMatchesByTeamX(teamById(1)).size(), Matchers.comparesEqualTo(2));
        MatcherAssert.assertThat(round.gameSaldoByTeamX(teamById(1)), Matchers.comparesEqualTo(2));
        MatcherAssert.assertThat(round.pointSaldoByTeamX(teamById(1)), Matchers.comparesEqualTo(22));

        MatcherAssert.assertThat(round.wonMatchesByTeamX(teamById(2)).size(), Matchers.comparesEqualTo(2));
        MatcherAssert.assertThat(round.gameSaldoByTeamX(teamById(2)), Matchers.comparesEqualTo(2));
        MatcherAssert.assertThat(round.pointSaldoByTeamX(teamById(2)), Matchers.comparesEqualTo(52));

        MatcherAssert.assertThat(round.wonMatchesByTeamX(teamById(4)).size(), Matchers.comparesEqualTo(2));
        MatcherAssert.assertThat(round.gameSaldoByTeamX(teamById(4)), Matchers.comparesEqualTo(2));
        MatcherAssert.assertThat(round.pointSaldoByTeamX(teamById(4)), Matchers.comparesEqualTo(42));

        final List<Team> teamsSortedByPouleResult = round.getTeamsSortedByPouleResult();
        MatcherAssert.assertThat(" Indien met het saldo van de games drie of meer spelers/paren gelijk eindigen, wordt de rangschikking verkregen door het verschil tussen het totaal aantal gewonnen punten en het totaal aantal verloren punten (= saldo van de punten)",
                teamsSortedByPouleResult.stream().map(Team::getId).collect(Collectors.toList()),
                Matchers.contains(2, 4, 1, 3));

    }

}