package be.pbo.jeugdcup.ranking.domain;

import java.util.Comparator;

public class NumberOfWonMatchesComparator implements Comparator<Team> {
    private final Draw draw;

    public NumberOfWonMatchesComparator(final Draw draw) {
        this.draw = draw;
    }


    @Override
    public int compare(final Team t1, final Team t2) {
        return Integer.compare(draw.wonMatchesByTeamX(t2).size(), draw.wonMatchesByTeamX(t1).size());
    }
}
