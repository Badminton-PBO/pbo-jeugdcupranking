package be.pbo.jeugdcup.ranking.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public List<Player> oppositeTeamPlayers(String memberId) {
        List<Player> result = new ArrayList<>();
        if (
                (match.getTeam1().getPlayer1() != null && match.getTeam1().getPlayer1().getMemberId().equals(memberId))
                        || (match.getTeam1().getPlayer2() != null && match.getTeam1().getPlayer2().getMemberId().equals(memberId))
        ) {
            addIfNotNull(result, match.getTeam2().getPlayer1());
            addIfNotNull(result, match.getTeam2().getPlayer2());
        } else if ((
                (match.getTeam2().getPlayer1() != null && match.getTeam2().getPlayer1().getMemberId().equals(memberId))
                        || (match.getTeam2().getPlayer2() != null && match.getTeam2().getPlayer2().getMemberId().equals(memberId))
        )) {
            addIfNotNull(result, match.getTeam1().getPlayer1());
            addIfNotNull(result, match.getTeam1().getPlayer2());
        }
        return result;
    }

    private void addIfNotNull(List<Player> result, Player player) {
        if (player != null) {
            result.add(player);
        }
    }
}
