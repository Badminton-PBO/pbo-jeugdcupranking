package be.pbo.jeugdcup.ranking.domain;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MutualConfrontationWhenExactly2TeamsHaveEqualPointSaldoComparator implements Comparator<Team> {
    private final Round round;
    private final List<Team> teamsInScope;

    public MutualConfrontationWhenExactly2TeamsHaveEqualPointSaldoComparator(final Round round) {
        this.round = round;
        this.teamsInScope = round.getAllTeams().stream()
                .collect(Collectors.groupingBy(t -> round.pointSaldoByTeamX(t)))
                .entrySet().stream()
                .filter(integerListEntry -> integerListEntry.getValue().size() == 2)
                .flatMap(integerListEntry -> integerListEntry.getValue().stream())
                .collect(Collectors.toList());
    }

    @Override
    public int compare(final Team t1, final Team t2) {
        if (!teamsInScope.contains(t1) || !teamsInScope.contains(t2)) {
            return 0;
        }

        final Team teamThatWonConfrontation = round.teamThatWonConfrontation(t1, t2);
        return teamThatWonConfrontation.equals(t1) ? -1 : +1;
    }
}
