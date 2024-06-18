package be.pbo.jeugdcup.ranking.infrastructure.db;

import be.pbo.jeugdcup.ranking.domain.Match;
import be.pbo.jeugdcup.ranking.domain.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class TpRepositoryCardImplTest {

    private static TpRepositoryCardImpl tpRepository;

    @BeforeAll
    public static void init() throws URISyntaxException {
        final Path path = Paths.get(TpRepositoryImpl.class.getResource("/tpFiles/PBOJeugdcupBCDePluimplukkers2024Voor.tp").toURI());
        tpRepository = new TpRepositoryCardImpl(path);
    }

    @Test
    public void testPocBK() {
        List<Match> m = tpRepository.getMatches();

        Map<Player, List<TpRepositoryCardImpl.EventNameWithDate>> result = tpRepository.getFirstMatchPerPlayerPerEventType(m);
        System.out.println(result);
    }


}