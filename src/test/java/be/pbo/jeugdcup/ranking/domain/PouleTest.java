package be.pbo.jeugdcup.ranking.domain;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class PouleTest {
    private Draw draw;
    private Poule poule;
    private List<Team> teams;
    private final String S21_0 = "21-0";
    private final String S21_10 = "21-10";
    private final String S0_21 = "0-21";
    private final String S10_21 = "10-21";

    @Test
    public void isValidWhenAllMatchersAreIn() {
        draw = createDraw(DrawType.POULE, 4);
        teams = createTeams(4);
        poule = createPoule(4, Arrays.asList(
                createMatch(1, 2, S21_0, S21_0, null),
                createMatch(1, 3, S21_0, S21_0, null),
                createMatch(1, 4, S21_0, S21_0, null),
                createMatch(2, 3, S21_0, S21_0, null),
                createMatch(2, 4, S21_0, S21_0, null),
                createMatch(3, 4, S21_0, S21_0, null)
        ));
        MatcherAssert.assertThat("Poule should be valid", poule.isValid(), Matchers.equalTo(Boolean.TRUE));
    }

    @Test
    public void isInValidWhenAllMatchersAreIn() {
        draw = createDraw(DrawType.POULE, 4);
        teams = createTeams(4);
        poule = createPoule(4, Arrays.asList(
                createMatch(1, 2, S21_0, S21_0, null),
                createMatch(1, 3, S21_0, S21_0, null),
                createMatch(1, 4, S21_0, S21_0, null),
                createMatch(2, 3, S21_0, S21_0, null),
                createMatch(2, 4, S21_0, S21_0, null)
        ));
        MatcherAssert.assertThat("Poule should be invalid", poule.isValid(), Matchers.equalTo(Boolean.FALSE));
    }

    @Test
    public void getAllTeams() {
        draw = createDraw(DrawType.POULE, 4);
        teams = createTeams(4);
        poule = createPoule(4, Arrays.asList(
                createMatch(1, 2, S21_0, S21_0, null),
                createMatch(1, 3, S21_0, S21_0, null),
                createMatch(1, 4, S21_0, S21_0, null),
                createMatch(2, 3, S21_0, S21_0, null),
                createMatch(2, 4, S21_0, S21_0, null),
                createMatch(3, 4, S21_0, S21_0, null)
        ));

        MatcherAssert.assertThat("Teams are getting extracted", poule.getAllTeams(),
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
        draw = createDraw(DrawType.POULE, 4);
        teams = createTeams(4);
        poule = createPoule(4, Arrays.asList(
                createMatch(1, 2, S21_0, S21_0, null),
                createMatch(1, 3, S21_0, S21_0, null),
                createMatch(1, 4, S21_0, S21_0, null),
                createMatch(2, 3, S21_0, S21_0, null),
                createMatch(2, 4, S21_0, S21_0, null),
                createMatch(3, 4, S21_0, S21_0, null)
        ));

        final List<Team> teamsSortedByPouleResult = poule.getTeamsSortedByPouleResult();
        MatcherAssert.assertThat("De rangschikking van een pool wordt opgemaakt naar het aantal gewonnen wedstrijden.",
                teamsSortedByPouleResult.stream().map(Team::getId).collect(Collectors.toList()),
                Matchers.contains(1, 2, 3, 4));
    }

    @Test
    public void sortByNumberOfWonMatches_2() {
        draw = createDraw(DrawType.POULE, 4);
        teams = createTeams(4);
        poule = createPoule(4, Arrays.asList(
                createMatch(1, 2, S21_0, S21_0, null),
                createMatch(1, 3, S21_0, S21_0, null),
                createMatch(1, 4, S21_0, S21_0, null),
                createMatch(2, 3, S21_0, S21_0, null),
                createMatch(2, 4, S21_0, S21_0, null),
                createMatch(3, 4, S0_21, S0_21, null)
        ));

        final List<Team> teamsSortedByPouleResult = poule.getTeamsSortedByPouleResult();
        MatcherAssert.assertThat("De rangschikking van een pool wordt opgemaakt naar het aantal gewonnen wedstrijden.",
                teamsSortedByPouleResult.stream().map(Team::getId).collect(Collectors.toList()),
                Matchers.contains(1, 2, 4, 3));
    }


    @Test
    public void sortByNumberOfWonMatches_3() {
        draw = createDraw(DrawType.POULE, 4);
        teams = createTeams(4);
        poule = createPoule(4, Arrays.asList(
                createMatch(1, 2, S0_21, S0_21, null),
                createMatch(1, 3, S21_0, S21_0, null),
                createMatch(1, 4, S21_0, S21_0, null),
                createMatch(2, 3, S21_0, S21_0, null),
                createMatch(2, 4, S21_0, S21_0, null),
                createMatch(3, 4, S0_21, S0_21, null)
        ));

        final List<Team> teamsSortedByPouleResult = poule.getTeamsSortedByPouleResult();
        MatcherAssert.assertThat("De rangschikking van een pool wordt opgemaakt naar het aantal gewonnen wedstrijden.",
                teamsSortedByPouleResult.stream().map(Team::getId).collect(Collectors.toList()),
                Matchers.contains(2, 1, 4, 3));

    }

    @Test
    public void whenTwoTeamsWonEqualNumberOfMatches() {
        draw = createDraw(DrawType.POULE, 5);
        teams = createTeams(5);
        poule = createPoule(5, Arrays.asList(
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

        final List<Team> teamsSortedByPouleResult = poule.getTeamsSortedByPouleResult();
        MatcherAssert.assertThat("Wanneer twee spelers/paren een zelfde aantal wedstrijden hebben gewonnen, wordt de winnaar van de onderlinge wedstrijd hoogst gerangschikt.",
                teamsSortedByPouleResult.stream().map(Team::getId).collect(Collectors.toList()),
                Matchers.contains(2, 1, 5, 3, 4)); // 3 & 4 still TODO
    }

    @Test
    public void whenMoreThanTwoTeamsWonEqualNumberOfMatches() {
        draw = createDraw(DrawType.POULE, 4);
        teams = createTeams(4);
        poule = createPoule(4, Arrays.asList(
                createMatch(1, 2, S21_0, S0_21, S21_0),
                createMatch(1, 3, S21_0, S21_0, null),
                createMatch(1, 4, S0_21, S0_21, null),
                createMatch(2, 3, S21_0, S21_0, null),
                createMatch(2, 4, S21_0, S0_21, S21_0),
                createMatch(3, 4, S0_21, S0_21, null)
        ));

        final List<Team> teamsSortedByPouleResult = poule.getTeamsSortedByPouleResult();
        MatcherAssert.assertThat("Wanneer drie of meer spelers/paren een zelfde aantal wedstrijden hebben gewonnen, zal de rangschikking worden opgemaakt naar het verschil tussen het totaal gewonnen games en het aantal verloren games (= saldo van de games)",
                teamsSortedByPouleResult.stream().map(Team::getId).collect(Collectors.toList()),
                Matchers.contains(4, 2, 1, 3));

    }

    @Test
    public void whenMoreThan2TeamsHaveSameGameSaldo() {
        draw = createDraw(DrawType.POULE, 4);
        teams = createTeams(4);
        poule = createPoule(4, Arrays.asList(
                createMatch(1, 2, S21_10, S0_21, S21_10),
                createMatch(1, 3, S21_0, S21_0, null),
                createMatch(1, 4, S21_0, S0_21, S0_21),
                createMatch(2, 3, S21_0, S21_10, null),
                createMatch(2, 4, S21_0, S0_21, S21_0),
                createMatch(3, 4, S0_21, S0_21, null)
        ));

        MatcherAssert.assertThat(poule.wonMatchesByTeamX(teamById(1)).size(), Matchers.comparesEqualTo(2));
        MatcherAssert.assertThat(poule.gameSaldoByTeamX(teamById(1)), Matchers.comparesEqualTo(2));
        MatcherAssert.assertThat(poule.pointSaldoByTeamX(teamById(1)), Matchers.comparesEqualTo(22));

        MatcherAssert.assertThat(poule.wonMatchesByTeamX(teamById(2)).size(), Matchers.comparesEqualTo(2));
        MatcherAssert.assertThat(poule.gameSaldoByTeamX(teamById(2)), Matchers.comparesEqualTo(2));
        MatcherAssert.assertThat(poule.pointSaldoByTeamX(teamById(2)), Matchers.comparesEqualTo(52));

        MatcherAssert.assertThat(poule.wonMatchesByTeamX(teamById(4)).size(), Matchers.comparesEqualTo(2));
        MatcherAssert.assertThat(poule.gameSaldoByTeamX(teamById(4)), Matchers.comparesEqualTo(2));
        MatcherAssert.assertThat(poule.pointSaldoByTeamX(teamById(4)), Matchers.comparesEqualTo(42));

        final List<Team> teamsSortedByPouleResult = poule.getTeamsSortedByPouleResult();
        MatcherAssert.assertThat(" Indien met het saldo van de games drie of meer spelers/paren gelijk eindigen, wordt de rangschikking verkregen door het verschil tussen het totaal aantal gewonnen punten en het totaal aantal verloren punten (= saldo van de punten)",
                teamsSortedByPouleResult.stream().map(Team::getId).collect(Collectors.toList()),
                Matchers.contains(2, 4, 1, 3));

    }

    private Poule createPoule(final int size, final List<Match> matches) {
        final Poule myPoule = Poule.builder()
                .draw(createDraw(DrawType.POULE, size))
                .matches(matches)
                .build();

        createTeams(size);
        return myPoule;
    }

    private Draw createDraw(final DrawType drawType, final int size) {
        return Draw.builder()
                .id(1)
                .name("draw name")
                .drawType(drawType)
                .size(size)
                .build();
    }

    private List<Team> createTeams(final int size) {
        return IntStream.range(1, size + 1).boxed()
                .map(i -> createDoubleTeam(i, String.valueOf(100 + i), String.valueOf(200 + i)))
                .collect(Collectors.toList());
    }

    private Team teamById(final int i) {
        return teams.stream().filter(t -> t.getId() == i).findFirst().get();
    }

    private Team createDoubleTeam(final Integer teamId, final String memberId1, final String memberId2) {
        return Team.builder()
                .id(teamId)
                .player1(Player.builder()
                        .memberId(memberId1)
                        .firstName("firsname" + memberId1)
                        .lastName("lastname" + memberId1)
                        .clubName("BC4LIVE")
                        .build())
                .player2(Player.builder()
                        .memberId(memberId2)
                        .firstName("firsname" + memberId2)
                        .lastName("lastname" + memberId2)
                        .clubName("BC4LIVE")
                        .build())
                .build();
    }

    private List<Match> createPouleMatches(final Draw draw, final List<Team> teams) {
        final List<int[]> uniqueCombinations = CombinationHelper.generate(teams.size(), 2);
        return uniqueCombinations.stream()
                .map(combination -> Arrays.asList(teams.get(combination[0]), teams.get(combination[1])))
                .map(teamCombination -> Match.builder()
                        .draw(draw)
                        .team1(teamCombination.get(0))
                        .team2(teamCombination.get(1))
                        .winner(teamCombination.get(0))
                        .build())
                .collect(Collectors.toList());

    }


    private Match createMatch(final int team1Id, final int team2Id, final String set1, final String set2, final String set3) {
        final Match match = Match.builder()
                .draw(draw)
                .team1(teamById(team1Id))
                .team2(teamById(team2Id))
                .set1(set1)
                .set2(set2)
                .set3(set3)
                .build();
        match.setWinner(match.getWinnerBasedOnSetResults());
        return match;
    }

}