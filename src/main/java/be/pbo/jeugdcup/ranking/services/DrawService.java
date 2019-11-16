package be.pbo.jeugdcup.ranking.services;

import be.pbo.jeugdcup.ranking.domain.Afvalschema;
import be.pbo.jeugdcup.ranking.domain.Match;
import be.pbo.jeugdcup.ranking.domain.Poule;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DrawService {

    private final List<Poule> poules = new ArrayList<>();
    private final List<Afvalschema> afvalschemas = new ArrayList<>();

    public DrawService(final List<Match> matches) {
        matches.stream()
                .filter(match -> match.getDraw().getClass().equals(Poule.class))
                .collect(Collectors.groupingBy(match -> match.getDraw().getId()))
                .forEach((drawId, matches1) -> {
                    final Poule draw = (Poule) matches1.get(0).getDraw();
                    draw.setMatches(matches1);
                    poules.add(draw);
                });

        matches.stream()
                .filter(match -> match.getDraw().getClass().equals(Afvalschema.class))
                .collect(Collectors.groupingBy(match -> match.getDraw().getId()))
                .forEach((drawId, matches1) -> {
                    final Afvalschema draw = (Afvalschema) matches1.get(0).getDraw();
                    draw.setMatches(matches1);
                    afvalschemas.add(draw);
                });

    }

    public List<Poule> getPoules() {
        return poules;
    }

}
