package be.pbo.jeugdcup.ranking.domain;

import lombok.Data;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

// In Dutch called 'Afvalschema'
@Data
public class EliminationScheme extends Draw {

    private static final Map<Integer, Integer> supportedEliminationSchemeSize = new HashMap<>();

    static {
        supportedEliminationSchemeSize.put(2, 1);
        supportedEliminationSchemeSize.put(4, 3);
        supportedEliminationSchemeSize.put(8, 7);
    }

    public SortedMap<Integer, List<Team>> getTeamsSortedByEliminationResult() {
        final TreeMap<Integer, List<Team>> result = new TreeMap<>(Comparator.reverseOrder());
        getAllTeams().stream()
                .collect(Collectors.groupingBy(t -> wonMatchesByTeamX(t).size()))
                .forEach(result::put);
        return result;
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
                        supportedEliminationSchemeSize.containsKey(this.getSize()) &&
                        supportedEliminationSchemeSize.get(this.getSize()).equals(getMatches().size());

    }

}
