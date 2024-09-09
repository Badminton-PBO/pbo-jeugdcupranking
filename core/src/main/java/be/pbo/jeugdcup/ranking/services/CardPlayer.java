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
public class CardPlayer {
    private Player player;
    private List<EventNameWithDate> firstMatchPerEventType;
}
