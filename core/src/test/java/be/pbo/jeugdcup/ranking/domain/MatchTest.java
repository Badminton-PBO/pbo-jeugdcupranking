package be.pbo.jeugdcup.ranking.domain;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import java.util.Arrays;

public class MatchTest extends DrawTesting {

    @Test
    public void testNumberOfMatchesPlayedExcludingWalkOverMatches() {
        teams = createTeams(4);
        createRound(4, Arrays.asList(
                createMatch(1, 2, S0_0, S0_0, null, 2, false, true),
                createMatch(1, 3, S0_0, S0_0, null, 3, false, true),
                createMatch(1, 4, S0_0, S0_0, null, 4, false, true),
                createMatch(2, 3, S21_0, S21_0, null),
                createMatch(2, 4, S21_0, S21_0, null),
                createMatch(3, 4, S0_0, S0_0, null, 3, false, true)
        ));

        MatcherAssert.assertThat("Team1 didn't play any match at all. Lost by walkover", teams.get(0).getNumberOfMatchesPlayedExcludingWalkOverMatches(),
                Matchers.equalTo(0));
        MatcherAssert.assertThat("Team2 did play 2 real matches", teams.get(1).getNumberOfMatchesPlayedExcludingWalkOverMatches(),
                Matchers.equalTo(2));
        MatcherAssert.assertThat("Team3 did play 1 real match", teams.get(2).getNumberOfMatchesPlayedExcludingWalkOverMatches(),
                Matchers.equalTo(1));
        MatcherAssert.assertThat("Team4 did play 1 real match", teams.get(3).getNumberOfMatchesPlayedExcludingWalkOverMatches(),
                Matchers.equalTo(1));

    }


    @Test
    public void testIsLostByGivingUp() {
        teams = createTeams(4);
        createRound(4, Arrays.asList(
                createMatch(1, 2, S21_0, S21_0, null),
                createMatch(1, 3, S21_0, S10_21, null, 3, true, false),
                createMatch(1, 4, S0_0, S0_0, null, 4, false, true),
                createMatch(2, 3, S21_0, S21_0, null),
                createMatch(2, 4, S21_0, S21_0, null),
                createMatch(3, 4, S0_0, S0_0, null, 3, false, true)
        ));

        MatcherAssert.assertThat("Team1 gave up", teams.get(0).isDidGaveUp(),
                Matchers.equalTo(true));
        MatcherAssert.assertThat("Team2 did not gave uo", teams.get(1).isDidGaveUp(),
                Matchers.equalTo(false));
        MatcherAssert.assertThat("Team3 did not gave up", teams.get(2).isDidGaveUp(),
                Matchers.equalTo(false));
        MatcherAssert.assertThat("Team4 did not gave up", teams.get(3).isDidGaveUp(),
                Matchers.equalTo(false));

    }


}