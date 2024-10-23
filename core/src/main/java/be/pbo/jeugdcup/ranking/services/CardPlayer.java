package be.pbo.jeugdcup.ranking.services;

import be.pbo.jeugdcup.ranking.domain.EventNameWithDate;
import be.pbo.jeugdcup.ranking.domain.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardPlayer implements Comparable<CardPlayer> {
    private Player player;
    private List<EventNameWithDate> firstMatchPerEventType;
    private int partitionId=0;
    private int boxId=0;

    public String uniqueId() {
        return partitionId + "-" + boxId;
    }

    @Override
    public int compareTo(CardPlayer o) {
        return this.player.getMemberId().compareTo(o.player.getMemberId());
    }
}
