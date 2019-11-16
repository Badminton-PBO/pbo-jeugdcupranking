package be.pbo.jeugdcup.ranking.domain;

import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class Afvalschema extends Draw {

    public Map<Integer, List<Team>> getTeamsSortedByAfvalschemaResult() {

        return null;
    }

    public List<Match> wonMatchesByTeamX(final Team t) {
        return this.getMatches().stream()
                .filter(m -> t.equals(m.getWinner()))
                .collect(Collectors.toList());
    }
}
