package be.pbo.jeugdcup.ranking.domain;

import lombok.Data;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class PBOJeugdCupTournament {

    private final List<Round> rounds = new ArrayList<>();
    private final List<EliminationScheme> eliminationSchemes = new ArrayList<>();
    private final List<QualificationScheme> qualificationSchemes = new ArrayList<>();

    private final List<Player> players;
    private final List<Event> events;
    private final boolean isAlwaysUsingDoubleSchemes;

    public PBOJeugdCupTournament(final List<Player> players, final List<Event> events, final List<Match> matches, final boolean isAlwaysUsingDoubleSchemes) {
        this.players = players;
        this.events = events;
        this.isAlwaysUsingDoubleSchemes = isAlwaysUsingDoubleSchemes;


        matches.stream()
                .filter(match -> match.getDraw().getClass().equals(Round.class))
                .collect(Collectors.groupingBy(match -> match.getDraw().getId()))
                .forEach((drawId, matches1) -> {
                    final Round draw = (Round) matches1.get(0).getDraw();
                    draw.setMatches(matches1);
                    rounds.add(draw);
                });

        matches.stream()
                .filter(match -> match.getDraw().getClass().equals(EliminationScheme.class))
                .collect(Collectors.groupingBy(match -> match.getDraw().getId()))
                .forEach((drawId, matches1) -> {
                    final EliminationScheme draw = (EliminationScheme) matches1.get(0).getDraw();
                    draw.setMatches(matches1);
                    eliminationSchemes.add(draw);
                });

        matches.stream()
                .filter(match -> match.getDraw().getClass().equals(QualificationScheme.class))
                .collect(Collectors.groupingBy(match -> match.getDraw().getId()))
                .forEach((drawId, matches1) -> {
                    final QualificationScheme draw = (QualificationScheme) matches1.get(0).getDraw();
                    draw.setMatches(matches1);
                    qualificationSchemes.add(draw);

                    // QualificationSchemes are not part of a normal PBO Jeugdcup tour but can be converted in a List of EliminationSchemes
                    eliminationSchemes.addAll(draw.convertToEliminationSchemes());
                });

        events.forEach(e -> {
            e.setRounds(this.getRounds(e));
            e.setEliminationSchemes(this.getEliminationSchemes(e));
        });
    }

    public List<Round> getRounds(final Event event) {
        return getRounds().stream()
                .filter(r -> event.getId().equals(r.getEvent().getId()))
                .collect(Collectors.toList());
    }

    public List<EliminationScheme> getEliminationSchemes(final Event event) {
        return getEliminationSchemes().stream()
                .filter(e -> event.getId().equals(e.getEvent().getId()))
                .collect(Collectors.toList());
    }

    public List<QualificationScheme> getQualificationSchemes(final Event event) {
        return getQualificationSchemes().stream()
                .filter(e -> event.getId().equals(e.getEvent().getId()))
                .collect(Collectors.toList());

    }
}
