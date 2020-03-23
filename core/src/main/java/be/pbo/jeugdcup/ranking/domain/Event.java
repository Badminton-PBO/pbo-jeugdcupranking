package be.pbo.jeugdcup.ranking.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
@Builder(toBuilder = true, builderClassName = "EventInternalBuilder", builderMethodName = "internalBuilder")
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Slf4j
public class Event {
    private static final Pattern AGE_CATEGORY_PATTERN = Pattern.compile(".*(U\\d\\d)");
    private Integer id;
    private String name;
    private Gender gender;
    private EventType eventType;
    private AgeCategory ageCategory = AgeCategory.DEFAULT_AGE_CATEGORY;

    private List<Round> rounds = new ArrayList<>();
    private List<EliminationScheme> eliminationSchemes = new ArrayList<>();


    void init() {
        if (name != null) {
            if (name.toLowerCase().indexOf("mini") > -1) {
                ageCategory = AgeCategory.MINIBAD;
            }
            final Matcher matcher = AGE_CATEGORY_PATTERN.matcher(name);
            if (matcher.matches()) {
                try {
                    ageCategory = AgeCategory.valueOf(matcher.group(1));
                } catch (final IllegalArgumentException e) {
                    log.warn("Unable to convert Event name " + name + " into a known AgeCategory");
                }
            }

        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends EventInternalBuilder {

        Builder() {
            super();
        }

        @Override
        public Event build() {
            final Event event = super.build();
            event.init();
            return event;
        }
    }


    // Returns teams sorted by their results for this event
    // It's possible that not all teams play in the Elimination phase.
    // For example: 13 inschijvingen, dubbel/gemengd -> vierde & vijfde uit poule spelen geen eindronde
    // In that case multiple teams end at the same Event-rank and should  get equal points
    public SortedMap<Integer, List<Team>> sortTeamsByEventResult() {
        final TreeMap<Integer, List<Team>> result = new TreeMap<>();
        if (rounds.isEmpty()) {
            log.info("Event has no rounds:" + this);
        } else if (eliminationSchemes.isEmpty() && rounds.size() == 1) {
            //Geen eindrondes, enkel 1 poule
            final List<Team> teamsSortedByPouleResult = rounds.get(0).getTeamsSortedByPouleResult();
            for (int i = 0; i < teamsSortedByPouleResult.size(); i++) {
                result.put(i + 1, Arrays.asList(teamsSortedByPouleResult.get(i)));
            }
        } else if (eliminationSchemes.size() > 0) {
            //Sort EliminationScheme so winners scheme come in front
            eliminationSchemes.sort(new EliminationSchemeComparator(this));

            final AtomicInteger resultPosition = new AtomicInteger(0);
            for (final EliminationScheme eliminationScheme : eliminationSchemes) {
                final SortedMap<Integer, List<Team>> teamsSortedByEliminationResult = eliminationScheme.getTeamsSortedByEliminationResult();
                teamsSortedByEliminationResult.keySet().forEach(k -> result.put(resultPosition.addAndGet(1), teamsSortedByEliminationResult.get(k)));
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
            throw new IllegalArgumentException("Event has more than one round but no eliminationscheme." + this);
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
