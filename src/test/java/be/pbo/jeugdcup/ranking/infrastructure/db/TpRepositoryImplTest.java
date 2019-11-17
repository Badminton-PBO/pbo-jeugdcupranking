package be.pbo.jeugdcup.ranking.infrastructure.db;

import be.pbo.jeugdcup.ranking.domain.Draw;
import be.pbo.jeugdcup.ranking.domain.Event;
import be.pbo.jeugdcup.ranking.domain.EventType;
import be.pbo.jeugdcup.ranking.domain.Gender;
import be.pbo.jeugdcup.ranking.domain.Match;
import be.pbo.jeugdcup.ranking.domain.Player;
import be.pbo.jeugdcup.ranking.domain.Round;
import be.pbo.jeugdcup.ranking.domain.Team;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TpRepositoryImplTest {

    private static TpRepositoryImpl tpRepository;

    @BeforeAll
    public static void init() throws URISyntaxException {
        final Path path = Paths.get(TpRepositoryImpl.class.getResource("/tpFiles/PBO-Jeugdcuptour-Lokerse-BC-2019.tp").toURI());
        tpRepository = new TpRepositoryImpl(path);
    }

    @Test
    public void testEvents() {
        final List<Event> events = tpRepository.getEvents();
        MatcherAssert.assertThat("All events must be read", events, Matchers.hasSize(17));
        MatcherAssert.assertThat("JD U17 event was found", events,
                Matchers.hasItem(
                        Matchers.equalTo(Event.builder()
                                .id(5)
                                .name("JD U17")
                                .gender(Gender.MALE)
                                .eventType(EventType.DOUBLE)
                                .build())));
    }


    @Test
    public void testDraws() {
        final List<Draw> draws = tpRepository.getDraws();
        MatcherAssert.assertThat("All draws must be read", draws, Matchers.hasSize(68));


        MatcherAssert.assertThat("JE U13 A-Reeks Groep B event was found", draws,
                Matchers.hasItem(
                        Matchers.equalTo(new Round.Builder(12)
                                .name("JE U13 A-Reeks Groep B")
                                .size(4)
                                .event(Event.builder()
                                        .id(7)
                                        .name("JE U13")
                                        .gender(Gender.MALE)
                                        .eventType(EventType.SINGLE)
                                        .build())
                                .build())));
    }

    @Test
    public void testPlayers() {
        final List<Player> players = tpRepository.getPlayers();
        MatcherAssert.assertThat("All players must be read", players, Matchers.hasSize(113));
        MatcherAssert.assertThat("Bluey was found", players,
                Matchers.hasItem(
                        Matchers.equalTo(Player.builder()
                                .firstName("Bluey")
                                .lastName("Buseyne")
                                .memberId("50754416")
                                .gender(1)
                                .build())));
    }

    @Test
    public void testMatches() {
        final List<Match> matches = tpRepository.getMatches();
        MatcherAssert.assertThat("All matches must be read", matches, Matchers.hasSize(242));
        MatcherAssert.assertThat("Match was found", matches,
                Matchers.hasItem(
                        Matchers.equalTo(Match.builder()
                                .team1(Team.builder()
                                        .id(17)
                                        .player1(Player.builder()
                                                .firstName("Bluey")
                                                .lastName("Buseyne")
                                                .memberId("50754416")
                                                .clubName("PLUIMPLUKKERS BC")
                                                .build())
                                        .player2(Player.builder()
                                                .firstName("Maurits")
                                                .lastName("Rooselaer")
                                                .memberId("50653644")
                                                .clubName("PLUIMPLUKKERS BC")
                                                .build())
                                        .build())
                                .team2(Team.builder()
                                        .id(136)
                                        .player1(Player.builder()
                                                .firstName("Ewout")
                                                .lastName("Maes")
                                                .memberId("50843192")
                                                .clubName("WIT-WIT BC")
                                                .build())
                                        .player2(Player.builder()
                                                .firstName("Jasper")
                                                .lastName("Vanden Broecke")
                                                .memberId("50610651")
                                                .clubName("LOKERSE BC")
                                                .build())
                                        .build())
                                .winner(
                                        Team.builder()
                                                .id(17)
                                                .player1(Player.builder()
                                                        .firstName("Bluey")
                                                        .lastName("Buseyne")
                                                        .memberId("50754416")
                                                        .clubName("PLUIMPLUKKERS BC")
                                                        .build())
                                                .player2(Player.builder()
                                                        .firstName("Maurits")
                                                        .lastName("Rooselaer")
                                                        .memberId("50653644")
                                                        .clubName("PLUIMPLUKKERS BC")
                                                        .build())
                                                .build()
                                )
                                .set1("21-12")
                                .set2("21-8")
                                .matchnr(5)
                                .roundnr(1)
                                .draw(new Round.Builder(75)
                                        .name("JD U15 Groep A")
                                        .size(3)
                                        .event(Event.builder()
                                                .id(4)
                                                .name("JD U15")
                                                .gender(Gender.MALE)
                                                .eventType(EventType.DOUBLE)
                                                .build())
                                        .build())
                                .build())));
    }

}