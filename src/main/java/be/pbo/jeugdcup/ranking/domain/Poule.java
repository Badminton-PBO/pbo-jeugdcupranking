package be.pbo.jeugdcup.ranking.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Poule {
    private List<Match> matches;
    private Draw draw;

    public Boolean isValid() {

        return matches.size() > 0 && draw.getSize() > 0 && drawSizeCorrespondsWithNumberOfMatchers();

    }

    private boolean drawSizeCorrespondsWithNumberOfMatchers() {
        final int ms = matches.size();
        final int poolSize = draw.getSize();

        return (poolSize - 1) * poolSize / 2 == ms;
    }

    public Set<Team> getAllTeams() {
        return matches.stream()
                .flatMap(match -> Arrays.asList(match.getTeam1(), match.getTeam2()).stream())
                .collect(Collectors.toSet());
    }

    public List<Team> getTeamsSortedByPouleResult() {
        final ArrayList<Team> teams = new ArrayList<>(getAllTeams());
        Collections.sort(teams, new NumberOfWonMatchesComparator(this)
                .thenComparing(new MutualConfrontationWhenExactly2TeamsHaveEqualNumberOfWonMatchesComparator(this))
                .thenComparing(new GameSaldoWhenMoreThan2TeamsHaveEqualNumberOfWonMatchesComparator(this))
                .thenComparing(new MutualConfrontationWhenExactly2TeamsHaveSameGameSaldoComparator(this))
                .thenComparing(new PointSaldoWhenMoreThan2TeamsHaveSameGameSaldoComparator(this))
                .thenComparing(new MutualConfrontationWhenExactly2TeamsHaveEqualPointSaldoComparator(this))
        );
        return teams;
    }

    public List<Match> wonMatchesByTeamX(final Team t) {
        return this.getMatches().stream()
                .filter(m -> t.equals(m.getWinner()))
                .collect(Collectors.toList());
    }

    public Team teamThatWonConfrontation(final Team t1, final Team t2) {
        final Match match = matches.stream()
                .filter(m -> m.isPlayedByTeams(t1, t2))
                .findAny().orElseThrow(() -> new IllegalArgumentException(String.format("No match was played between %s and %s in poule %s", t1.toStringShort(), t2.toStringShort(), this.getDraw().getName())));

        return match.getWinner();
    }

    public Integer gameSaldoByTeamX(final Team t) {
        return this.getMatches().stream()
                .filter(m -> m.isPlayedWithTeam(t))
                .map(m -> m.gameSaldo(t))
                .mapToInt(Integer::intValue)
                .sum();
    }

    public Integer pointSaldoByTeamX(final Team t) {
        return this.getMatches().stream()
                .filter(m -> m.isPlayedWithTeam(t))
                .map(m -> m.pointsSaldo(t))
                .mapToInt(Integer::intValue)
                .sum();
    }
}
