package be.pbo.jeugdcup.ranking.domain;

import java.util.Comparator;

public class NumberOfWonMatchesComparator implements Comparator<Team> {
    private final Poule poule;

    public NumberOfWonMatchesComparator(final Poule poule) {
        this.poule = poule;
    }

    @Override
    public int compare(final Team t1, final Team t2) {
        return Integer.compare(poule.wonMatchesByTeamX(t2).size(), poule.wonMatchesByTeamX(t1).size());
    }
}
