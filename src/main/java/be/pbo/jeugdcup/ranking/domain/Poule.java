package be.pbo.jeugdcup.ranking.domain;

import lombok.Data;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class Poule extends Draw {

    public Boolean isValid() {
        return this.getMatches().size() > 0 && this.getSize() > 0 && drawSizeCorrespondsWithNumberOfMatchers();
    }

    private boolean drawSizeCorrespondsWithNumberOfMatchers() {
        final int ms = this.getMatches().size();
        final int poolSize = this.getSize();

        return (poolSize - 1) * poolSize / 2 == ms;
    }

    public Set<Team> getAllTeams() {
        return this.getMatches().stream()
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


    public Team teamThatWonConfrontation(final Team t1, final Team t2) {
        final Match match = this.getMatches().stream()
                .filter(m -> m.isPlayedByTeams(t1, t2))
                .findAny().orElseThrow(() -> new IllegalArgumentException(String.format("No match was played between %s and %s in poule %s", t1.toStringShort(), t2.toStringShort(), this.getName())));

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

    public static class Builder {
        private final Integer id;
        private String name;
        private Event event;
        private int size;
        private List<Match> matches;

        public Builder(final Integer id) {
            this.id = id;
        }

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public Builder event(final Event event) {
            this.event = event;
            return this;
        }

        public Builder size(final int size) {
            this.size = size;
            return this;
        }

        public Builder matches(final List<Match> matches) {
            this.matches = matches;
            return this;
        }

        public Poule build() {
            final Poule poule = new Poule();
            poule.setId(this.id);
            poule.setName(this.name);
            poule.setEvent(this.event);
            poule.setSize(this.size);
            poule.setMatches(this.matches);
            return poule;
        }
    }



}
