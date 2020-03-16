package be.pbo.jeugdcup.ranking;

import be.pbo.jeugdcup.ranking.domain.Event;
import be.pbo.jeugdcup.ranking.domain.Match;
import be.pbo.jeugdcup.ranking.domain.Player;
import be.pbo.jeugdcup.ranking.domain.Round;
import be.pbo.jeugdcup.ranking.domain.Team;
import be.pbo.jeugdcup.ranking.infrastructure.db.TpRepository;
import be.pbo.jeugdcup.ranking.infrastructure.db.TpRepositoryImpl;
import be.pbo.jeugdcup.ranking.services.PBOJeugdCupTournamentService;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.SortedMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RankingGenerator {
    private final Path filePath;
    private TpRepository tpRepository;

    public RankingGenerator(final Path filePath) {
        this.filePath = filePath;
    }

    public void generate() {
        tpRepository = new TpRepositoryImpl(this.filePath);
        final List<Event> events = tpRepository.getEvents();
        final List<Player> players = tpRepository.getPlayers();
        final List<Match> matches = tpRepository.getMatches();

        final Optional<Match> any = matches.stream().filter(m -> m.getId().equals(241)).findAny();

        final PBOJeugdCupTournamentService PBOJeugdCupTournamentService = new PBOJeugdCupTournamentService(matches);
        final List<Round> rounds = PBOJeugdCupTournamentService.getRounds();

        events.forEach(e -> {
            e.setRounds(PBOJeugdCupTournamentService.getRounds(e));
            e.setEliminationSchemes(PBOJeugdCupTournamentService.getEliminationSchemes(e));
            e.setQualificationSchemes(PBOJeugdCupTournamentService.getQualificationSchemes());
        });

        final SortedMap<Integer, List<Team>> integerListSortedMap = events.stream()
                .filter(e -> "JEU13".equals(e.getName()))
                .findAny().get()
                .generateRanking();

        final List<Match> rr = matches.stream().filter(matchWithMemberId("50828320")).collect(Collectors.toList());

        System.out.println(rr);
    }

    public Predicate<Match> matchWithMemberId(final String memberId) {
        return m -> memberId.equals(m.getTeam1().getPlayer1().getMemberId())
                || (m.getTeam1().getPlayer2() != null && memberId.equals(m.getTeam1().getPlayer2().getMemberId()))
                || memberId.equals(m.getTeam2().getPlayer1().getMemberId())
                || (m.getTeam2().getPlayer2() != null && memberId.equals(m.getTeam2().getPlayer2().getMemberId()));
    }

}
