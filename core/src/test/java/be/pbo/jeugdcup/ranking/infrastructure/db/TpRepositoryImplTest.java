package be.pbo.jeugdcup.ranking.infrastructure.db;

import be.pbo.jeugdcup.ranking.domain.AgeCategory;
import be.pbo.jeugdcup.ranking.domain.Draw;
import be.pbo.jeugdcup.ranking.domain.Event;
import be.pbo.jeugdcup.ranking.domain.EventType;
import be.pbo.jeugdcup.ranking.domain.Gender;
import be.pbo.jeugdcup.ranking.domain.Match;
import be.pbo.jeugdcup.ranking.domain.Player;
import be.pbo.jeugdcup.ranking.domain.Reeks;
import be.pbo.jeugdcup.ranking.domain.Round;
import be.pbo.jeugdcup.ranking.domain.Team;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
                                .id(1)
                                .firstName("Bluey")
                                .lastName("Buseyne")
                                .memberId("50754416")
                                .gender(Gender.MALE)
                                .clubName("PLUIMPLUKKERS BC")
                                .ageCategory(AgeCategory.UNKNOWN)
                                .eventName(new ArrayList<>())
                                .build())));
    }

    @Test
    public void testMatches() {
        final List<Match> matches = tpRepository.getMatches();
        MatcherAssert.assertThat("All matches must be read", matches, Matchers.hasSize(242));
        MatcherAssert.assertThat("Match was found", matches,
                Matchers.hasItem(
                        Matchers.equalTo(Match.builder()
                                .id(886)
                                .team1(Team.builder()
                                        .id(17)
                                        .numberOfMatchesPlayedExcludingWalkOverMatches(2)
                                        .player1(Player.builder()
                                                .id(1)
                                                .firstName("Bluey")
                                                .lastName("Buseyne")
                                                .memberId("50754416")
                                                .gender(Gender.MALE)
                                                .clubName("PLUIMPLUKKERS BC")
                                                .ageCategory(AgeCategory.U15)
                                                .eventName(new ArrayList<>())
                                                .build())
                                        .player2(Player.builder()
                                                .id(13)
                                                .firstName("Maurits")
                                                .lastName("Rooselaer")
                                                .memberId("50653644")
                                                .gender(Gender.MALE)
                                                .clubName("PLUIMPLUKKERS BC")
                                                .ageCategory(AgeCategory.U15)
                                                .eventName(new ArrayList<>())
                                                .build())
                                        .build())
                                .team2(Team.builder()
                                        .id(136)
                                        .numberOfMatchesPlayedExcludingWalkOverMatches(2)
                                        .player1(Player.builder()
                                                .id(57)
                                                .firstName("Ewout")
                                                .lastName("Maes")
                                                .memberId("50843192")
                                                .gender(Gender.MALE)
                                                .clubName("WIT-WIT BC")
                                                .ageCategory(AgeCategory.U15)
                                                .eventName(new ArrayList<>())
                                                .build())
                                        .player2(Player.builder()
                                                .id(41)
                                                .firstName("Jasper")
                                                .lastName("Vanden Broecke")
                                                .memberId("50610651")
                                                .gender(Gender.MALE)
                                                .clubName("LOKERSE BC")
                                                .ageCategory(AgeCategory.U15)
                                                .eventName(new ArrayList<>())
                                                .build())
                                        .build())
                                .winner(
                                        Team.builder()
                                                .id(17)
                                                .numberOfMatchesPlayedExcludingWalkOverMatches(3)
                                                .player1(Player.builder()
                                                        .id(1)
                                                        .firstName("Bluey")
                                                        .lastName("Buseyne")
                                                        .memberId("50754416")
                                                        .gender(Gender.MALE)
                                                        .clubName("PLUIMPLUKKERS BC")
                                                        .ageCategory(AgeCategory.U15)
                                                        .eventName(new ArrayList<>())
                                                        .build())
                                                .player2(Player.builder()
                                                        .id(13)
                                                        .firstName("Maurits")
                                                        .lastName("Rooselaer")
                                                        .memberId("50653644")
                                                        .gender(Gender.MALE)
                                                        .clubName("PLUIMPLUKKERS BC")
                                                        .ageCategory(AgeCategory.U15)
                                                        .eventName(new ArrayList<>())
                                                        .build())
                                                .build())
                                .set1("21-12")
                                .set2("21-8")
                                .matchnr(5)
                                .roundnr(1)
                                .isWalkOverMatch(false)
                                .draw(new Round.Builder(75)
                                        .name("JD U15 Groep A")
                                        .size(3)
                                        .event(Event.builder()
                                                .id(4)
                                                .name("JD U15")
                                                .gender(Gender.MALE)
                                                .eventType(EventType.DOUBLE)
                                                .ageCategory(AgeCategory.U15)
                                                .reeks(Reeks.NA)
                                                .build())
                                        .build())
                                .build())));
    }

}