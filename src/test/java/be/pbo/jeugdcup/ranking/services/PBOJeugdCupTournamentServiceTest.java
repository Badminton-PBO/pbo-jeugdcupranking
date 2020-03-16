package be.pbo.jeugdcup.ranking.services;

import be.pbo.jeugdcup.ranking.domain.EliminationScheme;
import be.pbo.jeugdcup.ranking.domain.QualificationScheme;
import be.pbo.jeugdcup.ranking.domain.Round;
import be.pbo.jeugdcup.ranking.infrastructure.db.TpRepositoryImpl;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

class PBOJeugdCupTournamentServiceTest {


    private static PBOJeugdCupTournamentService cutLokerse2019;
    private static PBOJeugdCupTournamentService cutVlabad2020;

    @BeforeAll
    public static void init() throws URISyntaxException {
        Path path = Paths.get(TpRepositoryImpl.class.getResource("/tpFiles/PBO-Jeugdcuptour-Lokerse-BC-2019.tp").toURI());
        cutLokerse2019 = new PBOJeugdCupTournamentService(new TpRepositoryImpl(path).getMatches());

        path = Paths.get(TpRepositoryImpl.class.getResource("/tpFiles/PBO-Jeugdcuptour-VLABAD-2020.tp").toURI());
        cutVlabad2020 = new PBOJeugdCupTournamentService(new TpRepositoryImpl(path).getMatches());

    }

    @Test
    void getPoules() {
        final List<Round> rounds = cutLokerse2019.getRounds();
        MatcherAssert.assertThat("Poules are getting extracted", rounds, Matchers.hasSize(34));
        rounds.forEach(p -> MatcherAssert.assertThat("Round should be valid " + p, p.isValid(), Matchers.equalTo(Boolean.TRUE)));
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