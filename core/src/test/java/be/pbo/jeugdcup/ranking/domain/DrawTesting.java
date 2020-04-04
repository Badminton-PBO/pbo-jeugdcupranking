package be.pbo.jeugdcup.ranking.domain;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DrawTesting {

    final String S21_0 = "21-0";
    final String S21_10 = "21-10";
    final String S0_21 = "0-21";
    final String S10_21 = "10-21";
    final String S0_0 = "0-0";


    protected List<Team> teams;

    protected Round createRound(final int size) {
        final Round draw = new Round();
        draw.setId(1);
        draw.setName("draw name");
        draw.setSize(size);

        return draw;
    }


    protected Event createEvent(final List<Round> rounds, final List<EliminationScheme> eliminationSchemes) {
        final Event event = new Event();
        event.setId(1);
        event.setName("event name");
        event.setRounds(rounds);
        event.setEliminationSchemes(eliminationSchemes);

        return event;
    }

    protected EliminationScheme createEliminationScheme(final int id, final int size) {
        final EliminationScheme draw = new EliminationScheme();
        draw.setId(id);
        draw.setName("draw name " + id);
        draw.setSize(size);

        return draw;
    }

    protected List<Team> createTeams(final int size) {
        return IntStream.range(1, size + 1).boxed()
                .map(i -> createDoubleTeam(i, String.valueOf(100 + i), String.valueOf(200 + i)))
                .collect(Collectors.toList());
    }

    protected List<Team> createSingleTeams(final List<Player> players) {
        return IntStream.range(1, players.size() + 1).boxed()
                .map(i -> createSingleTeam(i, players.get(i - 1)))
                .collect(Collectors.toList());
    }


    protected List<Team> createSingleTeams(final int size) {
        return IntStream.range(1, size + 1).boxed()
                .map(i -> createSingleTeam(i, String.valueOf(100 + i)))
                .collect(Collectors.toList());
    }

    protected Team createDoubleTeam(final Integer teamId, final String memberId1, final String memberId2) {
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

    protected Team createSingleTeam(final Integer teamId, final String memberId1) {
        return Team.builder()
                .id(teamId)
                .player1(Player.builder()
                        .memberId(memberId1)
                        .firstName("firsname" + memberId1)
                        .lastName("lastname" + memberId1)
                        .clubName("BC4LIVE")
                        .build())
                .build();
    }

    protected Team createSingleTeam(final Integer teamId, final Player player) {
        return Team.builder()
                .id(teamId)
                .player1(player)
                .build();
    }

    protected Match createMatch(final int team1Id, final int team2Id, final String set1, final String set2, final String set3, final int winnerTeamId, final boolean lostByGivingUp, final boolean isWalkOverMatch) {
        final Match match = Match.builder()
                .team1(teamById(team1Id))
                .team2(teamById(team2Id))
                .set1(set1)
                .set2(set2)
                .set3(set3)
                .winner(teamById(winnerTeamId))
                .isLostByGivingUp(lostByGivingUp)
                .isWalkOverMatch(isWalkOverMatch)
                .build();

        return match;
    }


    protected Match createMatch(final int team1Id, final int team2Id, final String set1, final String set2, final String set3) {
        final int winnerTeamId = getWinnerBasedOnSetResults(team1Id, team2Id, set1, set2, set3);
        final Match match = createMatch(team1Id, team2Id, set1, set2, set3, winnerTeamId, false, false);
        return match;
    }

    //VisibleForTesting
    private int getWinnerBasedOnSetResults(final int team1Id, final int team2Id, final String set1, final String set2, final String set3) {
        final Map<Integer, Long> numberOfSetsWonPerTeam = getNumberOfSetsWonPerTeam(team1Id, team2Id, set1, set2, set3);
        final Integer setsWonByTeam1 = numberOfSetsWonPerTeam.getOrDefault(team1Id, 0L).intValue();
        final Integer setsWonByTeam2 = numberOfSetsWonPerTeam.getOrDefault(team2Id, 0L).intValue();

        if (setsWonByTeam1 > setsWonByTeam2) {
            return team1Id;
        } else if (setsWonByTeam1 < setsWonByTeam2) {
            return team2Id;
        } else {
            throw new IllegalStateException("Unable to detect a winner based on sets for match " + this);
        }
    }

    private Map<Integer, Long> getNumberOfSetsWonPerTeam(final int team1Id, final int team2Id, final String set1, final String set2, final String set3) {
        return Stream.of(set1, set2, set3)
                .filter(set -> set != null && Match.SET_PATTERN.matcher(set).matches())
                .map(set -> {
                    final Matcher matcher = Match.SET_PATTERN.matcher(set);
                    matcher.matches();
                    int team1points = Integer.parseInt(matcher.group(1));
                    int team2points = Integer.parseInt(matcher.group(2));
                    if (team1points == 0 && team2points == 0) { // Walkover game
                        return -1;
                    }
                    return team1points > team2points ? team1Id : team2Id;
                })
                .filter(x -> x != -1)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }


    protected Round createRound(final int size, final List<Match> matches) {
        final Round round = createRound(size);
        matches.forEach(m -> m.setDraw(round));
        round.setMatches(matches);

        return round;
    }


    protected EliminationScheme createEliminationScheme(final int id, final int size, final List<Match> matches) {
        final EliminationScheme eliminationScheme = createEliminationScheme(id, size);
        matches.forEach(m -> m.setDraw(eliminationScheme));
        eliminationScheme.setMatches(matches);

        MatcherAssert.assertThat("", eliminationScheme.isValid(), Matchers.equalTo(Boolean.TRUE));
        return eliminationScheme;
    }

    protected Team teamById(final int i) {
        return teams.stream().filter(t -> t.getId() == i).findFirst().get();
    }

    protected Team teamById(final List<Team> myTeams, final int i) {
        return myTeams.stream().filter(t -> t.getId() == i).findFirst().get();
    }

    protected List<Match> createPouleMatches(final Draw draw, final List<Team> teams) {
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
}
