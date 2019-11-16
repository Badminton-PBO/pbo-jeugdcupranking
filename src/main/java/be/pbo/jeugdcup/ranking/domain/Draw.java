package be.pbo.jeugdcup.ranking.domain;

import lombok.Data;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public abstract class Draw {
    private Integer id;
    private String name;
    private Event event;
    private int size;
    private List<Match> matches;

    public List<Match> wonMatchesByTeamX(final Team t) {
        return this.getMatches().stream()
                .filter(m -> t.equals(m.getWinner()))
                .collect(Collectors.toList());
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
