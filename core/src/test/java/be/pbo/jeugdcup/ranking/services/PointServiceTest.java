package be.pbo.jeugdcup.ranking.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import be.pbo.jeugdcup.ranking.domain.DrawTesting;
import be.pbo.jeugdcup.ranking.domain.EventType;
import be.pbo.jeugdcup.ranking.domain.PBOJeugdCupTournament;
import be.pbo.jeugdcup.ranking.domain.Player;
import be.pbo.jeugdcup.ranking.domain.Reeks;
import be.pbo.jeugdcup.ranking.domain.Team;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class PointServiceTest extends DrawTesting {

    private static PBOJeugdCupTournament pboJeugdCupTournament;
    private PointService cut;

    @BeforeAll
    public static void init() {
        pboJeugdCupTournament = mock(PBOJeugdCupTournament.class);
    }

    @Test
    public void pointWith3Teams() {
        when(pboJeugdCupTournament.isAlwaysUsingDoubleSchemes()).thenReturn(Boolean.TRUE);
        cut = new PointService(pboJeugdCupTournament);
        final TreeMap<Integer, List<Team>> teamsSortedByEventResult = new TreeMap<>();
        teams = createTeams(3);
        teamsSortedByEventResult.put(1, Arrays.asList(teamById(1)));
        teamsSortedByEventResult.put(2, Arrays.asList(teamById(2)));
        teamsSortedByEventResult.put(3, Arrays.asList(teamById(3)));

        cut.addEventResultPerPlayer(EventType.DOUBLE, null, teamsSortedByEventResult);
        final Map<Player, Integer> pointPerPlayer = cut.getPointPerPlayer();
        MatcherAssert.assertThat("With only 3 teams, no points should be given",
                pointPerPlayer.keySet(),
                Matchers.hasSize(0));
    }


    @Test
    public void pointWhenFourDoubleTeamsAReeks() {
        when(pboJeugdCupTournament.isAlwaysUsingDoubleSchemes()).thenReturn(Boolean.FALSE);
        cut = new PointService(pboJeugdCupTournament);
        final TreeMap<Integer, List<Team>> teamsSortedByEventResult = new TreeMap<>();
        teams = createTeams(4);
        teamsSortedByEventResult.put(1, Arrays.asList(teamById(1)));
        teamsSortedByEventResult.put(2, Arrays.asList(teamById(2)));
        teamsSortedByEventResult.put(3, Arrays.asList(teamById(3), teamById(4)));

        cut.addEventResultPerPlayer(EventType.DOUBLE, Reeks.NA, teamsSortedByEventResult);
        final Map<Player, Integer> pointPerPlayer = cut.getPointPerPlayer();
        MatcherAssert.assertThat("Points should be calculcated correclty.",
                pointPerPlayer.keySet().stream().map(p -> p.getMemberId() + "_" + pointPerPlayer.get(p)).collect(Collectors.toList()),
                Matchers.containsInAnyOrder("101_100", "201_100", "102_95", "202_95", "103_90", "203_90", "104_90", "204_90"));
    }


    @Test
    public void pointWhenFourSingleTeamsAReeks() {
        when(pboJeugdCupTournament.isAlwaysUsingDoubleSchemes()).thenReturn(Boolean.FALSE);
        cut = new PointService(pboJeugdCupTournament);
        final TreeMap<Integer, List<Team>> teamsSortedByEventResult = new TreeMap<>();
        teams = createSingleTeams(4);
        teamsSortedByEventResult.put(1, Arrays.asList(teamById(1)));
        teamsSortedByEventResult.put(2, Arrays.asList(teamById(2)));
        teamsSortedByEventResult.put(3, Arrays.asList(teamById(3), teamById(4)));

        cut.addEventResultPerPlayer(EventType.SINGLE, Reeks.A_REEKS, teamsSortedByEventResult);
        final Map<Player, Integer> pointPerPlayer = cut.getPointPerPlayer();
        MatcherAssert.assertThat("Points should be calculcated correclty.",
                pointPerPlayer.keySet().stream().map(p -> p.getMemberId() + "_" + pointPerPlayer.get(p)).collect(Collectors.toList()),
                Matchers.containsInAnyOrder("101_100", "102_95", "103_90", "104_90"));
    }


    @Test
    public void pointWhenFourSingleTeamsBReeks() {
        when(pboJeugdCupTournament.isAlwaysUsingDoubleSchemes()).thenReturn(Boolean.FALSE);
        cut = new PointService(pboJeugdCupTournament);
        final TreeMap<Integer, List<Team>> teamsSortedByEventResult = new TreeMap<>();
        teams = createSingleTeams(4);
        teamsSortedByEventResult.put(1, Arrays.asList(teamById(1)));
        teamsSortedByEventResult.put(2, Arrays.asList(teamById(2)));
        teamsSortedByEventResult.put(3, Arrays.asList(teamById(3), teamById(4)));

        cut.addEventResultPerPlayer(EventType.SINGLE, Reeks.B_REEKS, teamsSortedByEventResult);
        final Map<Player, Integer> pointPerPlayer = cut.getPointPerPlayer();
        MatcherAssert.assertThat("Points should be calculcated correclty.",
                pointPerPlayer.keySet().stream().map(p -> p.getMemberId() + "_" + pointPerPlayer.get(p)).collect(Collectors.toList()),
                Matchers.containsInAnyOrder("101_97", "102_92", "103_87", "104_87"));
    }


    @Test
    public void pointWhenFourDoubleAndSingleIsPlayedBestPointIsKept() {
        when(pboJeugdCupTournament.isAlwaysUsingDoubleSchemes()).thenReturn(Boolean.FALSE);
        cut = new PointService(pboJeugdCupTournament);
        final TreeMap<Integer, List<Team>> teamsSortedByEventResult = new TreeMap<>();
        teams = createTeams(4);
        teamsSortedByEventResult.put(1, Arrays.asList(teamById(1)));
        teamsSortedByEventResult.put(2, Arrays.asList(teamById(2)));
        teamsSortedByEventResult.put(3, Arrays.asList(teamById(3), teamById(4)));

        cut.addEventResultPerPlayer(EventType.DOUBLE, Reeks.NA, teamsSortedByEventResult);

        final Map<Player, Integer> pointPerPlayer = cut.getPointPerPlayer();
        MatcherAssert.assertThat("Points should be calculcated correclty.",
                pointPerPlayer.keySet().stream().map(p -> p.getMemberId() + "_" + pointPerPlayer.get(p)).collect(Collectors.toList()),
                Matchers.containsInAnyOrder("101_100", "201_100", "102_95", "202_95", "103_90", "203_90", "104_90", "204_90"));


        final List<Team> singleTeams = createSingleTeams(Arrays.asList(teams.get(0).getPlayer1(), teams.get(1).getPlayer2(), teams.get(3).getPlayer1(), teams.get(3).getPlayer2()));
        final TreeMap<Integer, List<Team>> teamsSortedBySingleEventResult = new TreeMap<>();
        teamsSortedBySingleEventResult.put(1, Arrays.asList(teamById(singleTeams,4)));
        teamsSortedBySingleEventResult.put(2, Arrays.asList(teamById(singleTeams,3)));
        teamsSortedBySingleEventResult.put(3, Arrays.asList(teamById(singleTeams,1), teamById(singleTeams,2)));

        cut.addEventResultPerPlayer(EventType.SINGLE, Reeks.A_REEKS, teamsSortedBySingleEventResult);

        final Map<Player, Integer> pointPerPlayerAfterSingles = cut.getPointPerPlayer();
        MatcherAssert.assertThat("Points should be calculcated correclty.",
                pointPerPlayerAfterSingles.keySet().stream().map(p -> p.getMemberId() + "_" + pointPerPlayerAfterSingles.get(p)).collect(Collectors.toList()),
                Matchers.containsInAnyOrder("101_100", "201_100", "102_95", "202_95", "103_90", "203_90", "104_95", "204_100"));


    }

}