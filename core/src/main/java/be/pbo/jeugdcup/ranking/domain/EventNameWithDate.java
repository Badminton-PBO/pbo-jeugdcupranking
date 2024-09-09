package be.pbo.jeugdcup.ranking.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventNameWithDate {
    private String eventName;
    private Date date;
    private Match match;

    public String teamVsTeamVisual() {
        return match.getTeam1().toStringVisual() + " vs " + match.getTeam2().toStringVisual();
    }
}
