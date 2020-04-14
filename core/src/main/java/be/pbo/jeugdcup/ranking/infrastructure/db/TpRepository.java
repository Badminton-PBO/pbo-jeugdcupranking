package be.pbo.jeugdcup.ranking.infrastructure.db;

import be.pbo.jeugdcup.ranking.domain.Draw;
import be.pbo.jeugdcup.ranking.domain.Event;
import be.pbo.jeugdcup.ranking.domain.Match;
import be.pbo.jeugdcup.ranking.domain.Player;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TpRepository {
    public String TOURNAMENT_NAME_SETTING_NAME = "Tournament";

    List<Event> getEvents();

    List<Draw> getDraws();

    List<Player> getPlayers();

    List<Match> getMatches();

    Optional<String> getSettingWithName(String settingName);

    Optional<LocalDate> getTournamentDay();
}
