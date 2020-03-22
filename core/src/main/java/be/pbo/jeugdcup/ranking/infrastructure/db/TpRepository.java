package be.pbo.jeugdcup.ranking.infrastructure.db;

import be.pbo.jeugdcup.ranking.domain.Draw;
import be.pbo.jeugdcup.ranking.domain.Event;
import be.pbo.jeugdcup.ranking.domain.Match;
import be.pbo.jeugdcup.ranking.domain.Player;
import java.util.List;

public interface TpRepository {

    List<Event> getEvents();

    List<Draw> getDraws();

    List<Player> getPlayers();

    List<Match> getMatches();
}
