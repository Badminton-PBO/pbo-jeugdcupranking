package be.pbo.jeugdcup.ranking.services;

import be.pbo.jeugdcup.ranking.domain.DrawType;
import be.pbo.jeugdcup.ranking.domain.Match;
import be.pbo.jeugdcup.ranking.domain.Poule;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PouleService {

    private final List<Poule> poules = new ArrayList<>();

    public PouleService(final List<Match> matches) {
        matches.stream()
                .filter(match -> DrawType.POULE.equals(match.getDraw().getDrawType()))
                .collect(Collectors.groupingBy(Match::getDraw))
                .forEach((draw, matches1) -> poules.add(Poule.builder()
                        .draw(draw)
                        .matches(matches1)
                        .build()));
    }

    public List<Poule> getPoules() {
        return poules;
    }

}
