package be.pbo.jeugdcup.ranking.domain;

import lombok.Data;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// In Dutch called 'Poule'
@Data
public class Round extends Draw {

    @Override
    public Boolean isValid() {
        return this.getMatches().size() > 2 && this.getSize() > 2 && drawSizeCorrespondsWithNumberOfMatchers();
    }

    private boolean drawSizeCorrespondsWithNumberOfMatchers() {
        final int ms = this.getMatches().size();
        final int poolSize = this.getSize();

        return (poolSize - 1) * poolSize / 2 == ms;
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

        public Round build() {
            final Round round = new Round();
            round.setId(this.id);
            round.setName(this.name);
            round.setEvent(this.event);
            round.setSize(this.size);
            round.setMatches(this.matches);
            return round;
        }
    }



}
