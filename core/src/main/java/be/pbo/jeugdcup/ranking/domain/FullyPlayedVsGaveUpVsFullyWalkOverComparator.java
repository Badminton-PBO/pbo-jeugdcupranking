package be.pbo.jeugdcup.ranking.domain;

import java.util.Comparator;

public class FullyPlayedVsGaveUpVsFullyWalkOverComparator implements Comparator<Team> {
    public FullyPlayedVsGaveUpVsFullyWalkOverComparator() {
    }


    @Override
    public int compare(final Team t1, final Team t2) {

        return Integer.compare(convertToNumber(t2), convertToNumber(t1));
    }

    private int convertToNumber(final Team t) {
        if (t.getNumberOfMatchesPlayedExcludingWalkOverMatches() == 0) {
            return 0;
        } else if (t.isDidGaveUp()) {
            return 1;
        } else {
            return 2;
        }

    }
}
