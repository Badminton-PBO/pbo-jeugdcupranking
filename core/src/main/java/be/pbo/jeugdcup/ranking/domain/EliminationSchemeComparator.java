package be.pbo.jeugdcup.ranking.domain;

import java.util.Comparator;

public class EliminationSchemeComparator implements Comparator<EliminationScheme> {

    private final Event event;

    public EliminationSchemeComparator(final Event event) {
        this.event = event;
    }

    @Override
    public int compare(final EliminationScheme es1, final EliminationScheme es2) {
        return scoreEliminationScheme(es1)  - scoreEliminationScheme(es2);
    }

    // Returns the sum of all team-positions from the Rounds
    // Ex.
    //      an EliminationScheme with the winners from the previous round will have score=0
    //      an EliminationScheme with 4 teams containing the seconds from previous rounds will have score = 4
    private Integer scoreEliminationScheme(final EliminationScheme es) {
        // Assumption: every team in a EliminationScheme is part of a single Round linked to the same event
        //
        return es.getAllTeams().stream()
                .map(t -> event.getRounds().stream()
                        .filter(r -> r.getAllTeams().contains(t))
                        .findFirst().orElseThrow(() -> new RuntimeException("Team part of en EliminationScheme should also be part of a Round linked to the same event."))
                        .getTeamsSortedByPouleResult().indexOf(t))
                .mapToInt(Integer::intValue).sum();

    }
}
