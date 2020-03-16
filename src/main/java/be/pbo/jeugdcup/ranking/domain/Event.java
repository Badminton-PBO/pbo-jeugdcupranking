package be.pbo.jeugdcup.ranking.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Event {
    private Integer id;
    private String name;
    private Gender gender;
    private EventType eventType;

    private List<Round> rounds = new ArrayList<>();
    private List<EliminationScheme> eliminationSchemes = new ArrayList<>();
    private List<QualificationScheme> qualificationSchemes = new ArrayList<>();

    public SortedMap<Integer, List<Team>> generateRanking() {
        final TreeMap<Integer, List<Team>> result = new TreeMap<>(Comparator.reverseOrder());

        if (eliminationSchemes.isEmpty() && qualificationSchemes.isEmpty() && rounds.size() == 1) {
            //Geen eindrondes, enkel 1 poule
            final List<Team> teamsSortedByPouleResult = rounds.get(0).getTeamsSortedByPouleResult();
            for (int i = 0; i < teamsSortedByPouleResult.size(); i++) {
                result.put(i, Arrays.asList(teamsSortedByPouleResult.get(i)));
            }
        } else if (eliminationSchemes.size() > 0) {
            eliminationSchemes.sort(new EliminationSchemeComparator(this));

            final AtomicInteger resultPosition = new AtomicInteger(0);
            for (final EliminationScheme eliminationScheme : eliminationSchemes) {
                final SortedMap<Integer, List<Team>> teamsSortedByEliminationResult = eliminationScheme.getTeamsSortedByEliminationResult();
                teamsSortedByEliminationResult.keySet().stream().sorted().forEach(k -> {
                    result.put(resultPosition.addAndGet(1), teamsSortedByEliminationResult.get(k));
                });
            }

            //Add teams that are part of a round but did not make it into the EliminationSchemes
            final List<Team> teamsPartOfEliminationsSchemes = eliminationSchemes.stream().flatMap(e -> e.getAllTeams().stream()).collect(Collectors.toList());
            final TreeMap<Integer, List<Team>> remainingTeams = new TreeMap<>(Comparator.reverseOrder());
            this.getRounds().forEach(round -> {
                final List<Team> teamsSortedByPouleResult = round.getTeamsSortedByPouleResult();
                for (int i = 0; i < teamsSortedByPouleResult.size(); i++) {
                    final Team team = teamsSortedByPouleResult.get(i);
                    if (!teamsPartOfEliminationsSchemes.contains(team)) {
                        remainingTeams.compute(i, (k, v) -> {
                            if (v == null) {
                                return new ArrayList<>(Arrays.asList(team));
                            } else {
                                v.add(team);
                                return v;
                            }
                        });
                    }
                }
            });

            remainingTeams.keySet().stream()
                    .sorted()
                    .forEach(k -> {
                        result.put(resultPosition.addAndGet(1), remainingTeams.get(k));
                    });

            return result;
        } else {
            throw new IllegalArgumentException("Event has more than one round but no eliminationscheme.");
        }
        return result;
    }

    private Set<Team> getTeams() {
        if (rounds.isEmpty()) {
            throw new RuntimeException("No rounds are yet assigned to this Event" + this);
        }
        return rounds.stream().flatMap(r -> r.getAllTeams().stream()).collect(Collectors.toSet());
    }


}
