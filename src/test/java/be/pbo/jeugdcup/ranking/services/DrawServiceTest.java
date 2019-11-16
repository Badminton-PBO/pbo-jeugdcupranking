package be.pbo.jeugdcup.ranking.services;

import be.pbo.jeugdcup.ranking.domain.Poule;
import be.pbo.jeugdcup.ranking.infrastructure.db.TpRepository;
import be.pbo.jeugdcup.ranking.infrastructure.db.TpRepositoryImpl;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

class DrawServiceTest {


    private static DrawService drawService;

    @BeforeAll
    public static void init() throws URISyntaxException {
        final Path path = Paths.get(TpRepositoryImpl.class.getResource("/tpFiles/PBO-Jeugdcuptour-Lokerse-BC-2019.tp").toURI());
        final TpRepository tpRepository = new TpRepositoryImpl(path);
        drawService = new DrawService(tpRepository.getMatches());
    }

    @Test
    void getPoules() {
        final List<Poule> poules = drawService.getPoules();
        MatcherAssert.assertThat("Poules are getting extracted", poules, Matchers.hasSize(34));
        poules.forEach(p -> MatcherAssert.assertThat("Poule should be valid " + p, p.isValid(), Matchers.equalTo(Boolean.TRUE)));
    }

}