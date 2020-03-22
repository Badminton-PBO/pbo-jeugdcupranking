package be.pbo.jeugdcup.ranking.domain;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

// In Dutch called 'Kwalificatieschema"
@Data
public class QualificationScheme extends Draw {

    private static int CONVERT_DRAW_ID_FROM = 1000;
    @Override
    public Boolean isValid() {
        // Only supporting QualificationsScheme having only 1 Match per team
        return this.getAllTeams().size() / 2 == this.getMatches().size();
    }

    public List<EliminationScheme> convertToEliminationSchemes() {

        if (!this.isValid()) {
            throw new RuntimeException("A QualificationScheme is only supported when every team plays only one match:" + this.toString());
        }

        final AtomicInteger counter = new AtomicInteger();
        return this.getMatches().stream().map(m -> {
            final EliminationScheme eliminationScheme = new EliminationScheme();
            eliminationScheme.setId(CONVERT_DRAW_ID_FROM + counter.addAndGet(1));
            eliminationScheme.setName(this.getName() + " " + eliminationScheme.getId() + " (convert from QualificationScheme)");
            eliminationScheme.setEvent(this.getEvent());
            List<Match> matches = new ArrayList<>();
            matches.add(m);
            eliminationScheme.setMatches(matches);
            eliminationScheme.setSize(2);
            m.setDraw(eliminationScheme);

            return eliminationScheme;
        }).collect(Collectors.toList());

    }

}
