package be.pbo.jeugdcup.ranking.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Event {
    private Integer id;
    private String name;
    private Gender gender;
    private EventType eventType;

    private List<Round> rounds;
    private List<EliminationScheme> eliminationSchemes;

    private List<Team> generateRanking() {
        if (eliminationSchemes.size() == 0 && rounds.size() == 1) {
            //Geen eindrondes, enkel 1 poule
            return rounds.get(0).getTeamsSortedByPouleResult();
        } else if (eliminationSchemes.size() == 0) {
            throw new IllegalArgumentException("More than 1 rounds but no eliminationscheme for event " + this);
        } else {
            return null;
        }

    }

}
