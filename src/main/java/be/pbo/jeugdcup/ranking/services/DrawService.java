package be.pbo.jeugdcup.ranking.services;

import be.pbo.jeugdcup.ranking.domain.EliminationScheme;
import be.pbo.jeugdcup.ranking.domain.Match;
import be.pbo.jeugdcup.ranking.domain.Round;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DrawService {

    private final List<Round> rounds = new ArrayList<>();
    private final List<EliminationScheme> eliminationSchemes = new ArrayList<>();

    public DrawService(final List<Match> matches) {
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

    }

    public List<Round> getRounds() {
        return rounds;
    }

    public List<EliminationScheme> getEliminationSchemes() {
        return eliminationSchemes;
    }
}
