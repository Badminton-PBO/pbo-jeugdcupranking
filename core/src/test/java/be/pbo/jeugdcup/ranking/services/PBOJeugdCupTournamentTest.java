package be.pbo.jeugdcup.ranking.services;

import be.pbo.jeugdcup.ranking.domain.EliminationScheme;
import be.pbo.jeugdcup.ranking.domain.PBOJeugdCupTournament;
import be.pbo.jeugdcup.ranking.domain.QualificationScheme;
import be.pbo.jeugdcup.ranking.domain.Round;
import be.pbo.jeugdcup.ranking.infrastructure.db.TpRepository;
import be.pbo.jeugdcup.ranking.infrastructure.db.TpRepositoryImpl;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

class PBOJeugdCupTournamentTest {


    private static PBOJeugdCupTournament cutLokerse2019;
    private static PBOJeugdCupTournament cutVlabad2020;

    @BeforeAll
    public static void init() throws URISyntaxException {
        final TpRepository tpRepositoryLokerse2019 = new TpRepositoryImpl(Paths.get(TpRepositoryImpl.class.getResource("/tpFiles/PBO-Jeugdcuptour-Lokerse-BC-2019.tp").toURI()));
        cutLokerse2019 = new PBOJeugdCupTournament(tpRepositoryLokerse2019.getPlayers(),
                tpRepositoryLokerse2019.getEvents(),
                tpRepositoryLokerse2019.getMatches(),
                Boolean.FALSE,
                tpRepositoryLokerse2019.getTournamentDay().orElse(null));

        final TpRepository tpRepositoryVlabad2020 = new TpRepositoryImpl(Paths.get(TpRepositoryImpl.class.getResource("/tpFiles/PBO-Jeugdcuptour-VLABAD-2020.tp").toURI()));
        cutVlabad2020 = new PBOJeugdCupTournament(tpRepositoryVlabad2020.getPlayers(),
                tpRepositoryVlabad2020.getEvents(),
                tpRepositoryVlabad2020.getMatches(),
                Boolean.TRUE);
    }

    @Test
    void getPoules() {
        final List<Round> rounds = cutLokerse2019.getRounds();
        MatcherAssert.assertThat("Poules are getting extracted", rounds, Matchers.hasSize(34));
        rounds.forEach(p -> MatcherAssert.assertThat("Round should be valid " + p, p.isValid(), Matchers.equalTo(Boolean.TRUE)));
    }

    @Test
    void playersWithoutMemberIdGetACustomNMemberId() {
        final List<String> players = cutLokerse2019.getPlayers().stream()
                .filter(player -> player.getMemberId().startsWith("99"))
                .map(player -> player.getFirstName() + " " + player.getLastName() + ";" + player.getMemberId())
                .sorted()
                .collect(Collectors.toList());

        MatcherAssert.assertThat("Players without number receive a custom memberId",
                players,
                Matchers.contains("Aya Dandash;99102001", "Jelle Schiettecat;99102002"));
    }

    @Test
    void getEliminationSchemas() {
        final List<EliminationScheme> eliminationSchemes = cutLokerse2019.getEliminationSchemes();
        MatcherAssert.assertThat("Eliminationschemes are getting extracted", eliminationSchemes, Matchers.hasSize(34));
        eliminationSchemes.forEach(p -> MatcherAssert.assertThat("EliminationScheme should be valid " + p, p.isValid(), Matchers.equalTo(Boolean.TRUE)));
    }

    @Test
    void getQualificationSchemas() {
        final List<QualificationScheme> qualificationSchemes = cutVlabad2020.getQualificationSchemes();
        MatcherAssert.assertThat("QualificationSchemes are getting extracted", qualificationSchemes, Matchers.hasSize(1));
    }


}