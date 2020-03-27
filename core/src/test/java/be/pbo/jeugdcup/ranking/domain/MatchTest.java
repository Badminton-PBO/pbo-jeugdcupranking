package be.pbo.jeugdcup.ranking.domain;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import java.util.Arrays;

public class MatchTest extends DrawTesting {

    @Test
    public void test() {
        teams = createTeams(4);
        createRound(4, Arrays.asList(
                createMatch(1, 2, S0_0, S0_0, null),
                createMatch(1, 3, S0_0, S0_0, null),
                createMatch(1, 4, S0_0, S0_0, null),
                createMatch(2, 3, S21_0, S21_0, null),
                createMatch(2, 4, S21_0, S21_0, null),
                createMatch(3, 4, S0_0, S0_0, null)
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

}