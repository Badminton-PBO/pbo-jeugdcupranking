package be.pbo.jeugdcup.ranking.services;

import be.pbo.jeugdcup.ranking.domain.Event;
import be.pbo.jeugdcup.ranking.domain.Match;
import be.pbo.jeugdcup.ranking.domain.PBOJeugdCupTournament;
import be.pbo.jeugdcup.ranking.domain.Player;
import be.pbo.jeugdcup.ranking.infrastructure.db.TpRepository;
import be.pbo.jeugdcup.ranking.infrastructure.db.TpRepositoryImpl;
import java.nio.file.Path;
import java.time.LocalDate;
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

        final PBOJeugdCupTournament pboJeugdCupTournament = new PBOJeugdCupTournament(players, events, matches, isAlwaysUsingDoubleSchemes, tpRepository.getTournamentDay().orElse(null));
        final PointService pointService = new PointService(pboJeugdCupTournament);

        events.forEach(pointService::addEventResultPerPlayer);

        final Map<Player, Integer> pointPerPlayer = pointService.getPointPerPlayer();

        return pointPerPlayer;
    }

    public Optional<LocalDate> getTournamentDate() {
        return  tpRepository.getTournamentDay();
    }

    public Predicate<Match> matchWithMemberId(final String memberId) {
        return m -> memberId.equals(m.getTeam1().getPlayer1().getMemberId())
                || (m.getTeam1().getPlayer2() != null && memberId.equals(m.getTeam1().getPlayer2().getMemberId()))
                || memberId.equals(m.getTeam2().getPlayer1().getMemberId())
                || (m.getTeam2().getPlayer2() != null && memberId.equals(m.getTeam2().getPlayer2().getMemberId()));
    }

}
