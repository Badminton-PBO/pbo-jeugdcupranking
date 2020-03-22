package be.pbo.jeugdcup.ranking.services;

import be.pbo.jeugdcup.ranking.domain.EventType;
import be.pbo.jeugdcup.ranking.domain.PBOJeugdCupTournament;
import be.pbo.jeugdcup.ranking.domain.Player;
import be.pbo.jeugdcup.ranking.domain.Reeks;
import be.pbo.jeugdcup.ranking.domain.Team;
import lombok.Data;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.stream.Stream;

@Data
public class PointService {

    private final Map<Player, Integer> pointPerPlayer = new HashMap<>();

    private final PBOJeugdCupTournament pboJeugdCupTournament;

    public PointService(final PBOJeugdCupTournament pboJeugdCupTournament) {
        this.pboJeugdCupTournament = pboJeugdCupTournament;
    }

    public void addEventResultPerPlayer(final EventType eventType, final Reeks reeks, final SortedMap<Integer, List<Team>> teamsSortedByEventResult) {
        final Map<Player, Integer> pointPerPlayerForEvent = generatePointPerPlayerForEventResults(eventType, reeks, teamsSortedByEventResult);

        pointPerPlayerForEvent.forEach((p, ppoint) -> {
            pointPerPlayer.compute(p, (player, point) -> {
                if (point == null) {
                    return ppoint;
                } else {
                    // Only the best result should be maintained
                    return ppoint > point ? ppoint : point;
                }
            });
        });

    }


    private Map<Player, Integer> generatePointPerPlayerForEventResults(final EventType eventType, final Reeks reeks, final SortedMap<Integer, List<Team>> teamsSortedByEventResult) {
        final Map<Player, Integer> result = new HashMap<>();
        final Long numberOfTeamsInvolved = teamsSortedByEventResult.values().stream().mapToLong(Collection::size).sum();
        if (numberOfTeamsInvolved < 4L) {
            //Artikel 9.5. Onderdelen met minder dan 3 wedstrijden komen niet in aanmerking voor punten.
            return result;
        }

        if (EventType.DOUBLE.equals(eventType) || EventType.MIX.equals(eventType) || pboJeugdCupTournament.isAlwaysUsingDoubleSchemes()) {
            teamsSortedByEventResult.keySet().forEach(k -> {
                final int point = k < 21 ? 100 - ((k - 1) * 5) : 0;
                teamsSortedByEventResult.get(k).stream()
                        .flatMap(t -> Stream.of(t.getPlayer1(), t.getPlayer2()))
                        .filter(Objects::nonNull)
                        .forEach(p -> result.put(p, point));
            });
        }

        //TODO: singles with A/B-reeks

        return result;
    }
}
