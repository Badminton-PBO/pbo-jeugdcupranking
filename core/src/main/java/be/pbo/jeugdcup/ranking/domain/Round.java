package be.pbo.jeugdcup.ranking.domain;

import lombok.Data;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Collections.sort(teams, new FullyPlayedVsGaveUpVsFullyWalkOverComparator()
                .thenComparing(new NumberOfWonMatchesComparator(this))
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
                .filter(m -> !m.isIgnoreMatchInThisDraw())
                .filter(m -> m.isPlayedWithTeam(t))
                .map(m -> m.gameSaldo(t))
                .mapToInt(Integer::intValue)
                .sum();
    }

    public Integer pointSaldoByTeamX(final Team t) {
        return this.getMatches().stream()
                .filter(m -> !m.isIgnoreMatchInThisDraw())
                .filter(m -> m.isPlayedWithTeam(t))
                .map(m -> m.pointsSaldo(t))
                .mapToInt(Integer::intValue)
                .sum();
    }


    public void handleMatchesOfTeamsThatGaveUp() {
        final List<Team> teamsThatGaveUp = this.getMatches().stream()
                .filter(Match::isLostByGivingUp)
                .map(Match::getLoser)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .distinct().collect(Collectors.toList());

        //Mark all matches of teams that gave up in this round as to be ignored
        this.getMatches().forEach(m -> {
            if ((m.getTeam1() != null && teamsThatGaveUp.contains(m.getTeam1()))
                    || (m.getTeam2() != null && teamsThatGaveUp.contains(m.getTeam2()))) {
                m.setIgnoreMatchInThisDraw(true);
            }
        });

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
