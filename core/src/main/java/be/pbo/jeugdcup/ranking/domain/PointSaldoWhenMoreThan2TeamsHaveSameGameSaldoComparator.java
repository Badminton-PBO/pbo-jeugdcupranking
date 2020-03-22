package be.pbo.jeugdcup.ranking.domain;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PointSaldoWhenMoreThan2TeamsHaveSameGameSaldoComparator implements Comparator<Team> {
    private final Round round;
    private final List<Team> teamsInScope;

    public PointSaldoWhenMoreThan2TeamsHaveSameGameSaldoComparator(final Round round) {
        this.round = round;
        this.teamsInScope = round.getAllTeams().stream()
                .collect(Collectors.groupingBy(round::gameSaldoByTeamX))
                .entrySet().stream()
                .filter(integerListEntry -> integerListEntry.getValue().size() > 2)
                .flatMap(integerListEntry -> integerListEntry.getValue().stream())
                .collect(Collectors.toList());
    }

    @Override
    public int compare(final Team t1, final Team t2) {
        if (!teamsInScope.contains(t1) || !teamsInScope.contains(t2)) {
            return 0;
        }

        return Integer.compare(round.pointSaldoByTeamX(t2), round.pointSaldoByTeamX(t1));
    }
}
