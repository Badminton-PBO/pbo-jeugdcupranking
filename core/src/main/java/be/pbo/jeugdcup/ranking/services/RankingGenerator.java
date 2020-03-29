package be.pbo.jeugdcup.ranking.services;

import be.pbo.jeugdcup.ranking.domain.Event;
import be.pbo.jeugdcup.ranking.domain.Match;
import be.pbo.jeugdcup.ranking.domain.PBOJeugdCupTournament;
import be.pbo.jeugdcup.ranking.domain.Player;
import be.pbo.jeugdcup.ranking.infrastructure.db.TpRepository;
import be.pbo.jeugdcup.ranking.infrastructure.db.TpRepositoryImpl;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class RankingGenerator {
    private final Path filePath;
    private final boolean isAlwaysUsingDoubleSchemes;
    private final TpRepository tpRepository;

    public RankingGenerator(final Path filePath, final boolean isAlwaysUsingDoubleSchemes) {
        this.filePath = filePath;
        this.isAlwaysUsingDoubleSchemes = isAlwaysUsingDoubleSchemes;
        tpRepository = new TpRepositoryImpl(this.filePath);

    }

    public Map<Player, Integer> generate() {
        final List<Event> events = tpRepository.getEvents();
        final List<Player> players = tpRepository.getPlayers();
        final List<Match> matches = tpRepository.getMatches();

        final PBOJeugdCupTournament pboJeugdCupTournament = new PBOJeugdCupTournament(players, events, matches, isAlwaysUsingDoubleSchemes);
        final PointService pointService = new PointService(pboJeugdCupTournament);

        events.forEach(e -> pointService.addEventResultPerPlayer(e));

        final Map<Player, Integer> pointPerPlayer = pointService.getPointPerPlayer();

        return pointPerPlayer;
    }

    public String getTournamentNameAndDate() {
        final Optional<String> tournamentName = tpRepository.getSettingWithName(TpRepository.TOURNAMENT_NAME_SETTING_NAME);
        final Optional<String> tournamentDate = tpRepository.getSettingWithName(TpRepository.TOURNAMENT_NAME_SETTING_DATE);

        return tournamentName.flatMap(s1 ->
                tournamentDate.map(s2 -> {
                    final String tournamentNameCleaned = s1.toUpperCase().replace("PBO", "").replace("JEUGDCUPTOUR", "").trim();

                    return tournamentNameCleaned + ", " + s2;
                })
        ).orElse("");
    }

    public Predicate<Match> matchWithMemberId(final String memberId) {
        return m -> memberId.equals(m.getTeam1().getPlayer1().getMemberId())
                || (m.getTeam1().getPlayer2() != null && memberId.equals(m.getTeam1().getPlayer2().getMemberId()))
                || memberId.equals(m.getTeam2().getPlayer1().getMemberId())
                || (m.getTeam2().getPlayer2() != null && memberId.equals(m.getTeam2().getPlayer2().getMemberId()));
    }

}
