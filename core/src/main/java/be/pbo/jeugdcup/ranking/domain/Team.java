package be.pbo.jeugdcup.ranking.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Team {

    private int id;
    private Player player1;
    private Player player2;

    public String toStringShort() {
        final StringBuilder sb = new StringBuilder(id + "(");
        sb.append(player1.getFirstName()).append(" ").append(player1.getLastName().charAt(0)).append(".");
        if (player2 != null) {
            sb.append(" + ");
            sb.append(player2.getFirstName()).append(" ").append(player2.getLastName().charAt(0)).append(".");
        }
        sb.append(")");
        return sb.toString();
    }
}