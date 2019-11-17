package be.pbo.jeugdcup.ranking.domain;

import lombok.Data;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class Afvalschema extends Draw {

    private static final Map<Integer, Integer> supportedAfvalSchemaSize = new HashMap<>();

    static {
        supportedAfvalSchemaSize.put(2, 1);
        supportedAfvalSchemaSize.put(4, 3);
        supportedAfvalSchemaSize.put(8, 7);
    }

    public Map<Integer, List<Team>> getTeamsSortedByAfvalschemaResult() {

        return null;
    }

    public List<Match> wonMatchesByTeamX(final Team t) {
        return this.getMatches().stream()
                .filter(m -> t.equals(m.getWinner()))
                .collect(Collectors.toList());
    }

    @Override
    public Boolean isValid() {
        return
                this.getSize() > 1 &&
                        supportedAfvalSchemaSize.containsKey(this.getSize()) &&
                        supportedAfvalSchemaSize.get(this.getSize()).equals(getMatches().size());

    }
}
