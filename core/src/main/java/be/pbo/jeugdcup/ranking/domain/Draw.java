package be.pbo.jeugdcup.ranking.domain;

import lombok.Data;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public abstract class Draw {
    private Integer id;
    private String name;
    private Event event;
    private int size;
    private List<Match> matches = new ArrayList<>();

    public List<Match> wonMatchesByTeamX(final Team t) {
        return this.getMatches().stream()
                .filter(m -> t.equals(m.getWinner()))
                .collect(Collectors.toList());
    }

    public abstract Boolean isValid();

    public Set<Team> getAllTeams() {
        return this.getMatches().stream()
                .flatMap(match -> Arrays.asList(match.getTeam1(), match.getTeam2()).stream())
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public Team teamThatWonConfrontation(final Team t1, final Team t2) {
        final Match match = this.getMatches().stream()
                .filter(m -> m.isPlayedByTeams(t1, t2))
                .findAny().orElseThrow(() -> new IllegalArgumentException(String.format("No match was played between %s and %s in poule %s", t1.toStringShort(), t2.toStringShort(), this.getName())));

        return match.getWinner();
    }


    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Draw draw = (Draw) o;
        return id.equals(draw.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
