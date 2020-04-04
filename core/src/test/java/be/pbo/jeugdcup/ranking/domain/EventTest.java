package be.pbo.jeugdcup.ranking.domain;

import be.pbo.jeugdcup.ranking.infrastructure.db.TpRepository;
import be.pbo.jeugdcup.ranking.infrastructure.db.TpRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;

@Slf4j
public class EventTest {

    private static Event event;
    private static PBOJeugdCupTournament pboJeugdCupTournamentVlabad;
    private static PBOJeugdCupTournament pboJeugdCupTournamentWitWit;

    @BeforeAll
    public static void init() throws URISyntaxException {
        final TpRepository tpRepositoryVlabad2020 = new TpRepositoryImpl(Paths.get(TpRepositoryImpl.class.getResource("/tpFiles/PBO-Jeugdcuptour-VLABAD-2020.tp").toURI()));
        pboJeugdCupTournamentVlabad = new PBOJeugdCupTournament(tpRepositoryVlabad2020.getPlayers(), tpRepositoryVlabad2020.getEvents(), tpRepositoryVlabad2020.getMatches(), Boolean.TRUE);

        final TpRepository tpRepositoryWitWit2020 = new TpRepositoryImpl(Paths.get(TpRepositoryImpl.class.getResource("/tpFiles/PBO_Jeugdcuptour_Wit-Wit_2020.tp").toURI()));
        pboJeugdCupTournamentWitWit = new PBOJeugdCupTournament(tpRepositoryWitWit2020.getPlayers(), tpRepositoryWitWit2020.getEvents(), tpRepositoryWitWit2020.getMatches(), Boolean.TRUE);

    }

    @Test
    public void sortTeamByEvents_JEU13() {
        event = pboJeugdCupTournamentVlabad.getEvents().stream().filter(e -> "JEU13".equals(e.getName())).findAny().get();
        final SortedMap<Integer, List<Team>> sortTeamsByEventResult = event.sortTeamsByEventResult();
        final Map<Integer, String> collect = mapToString(sortTeamsByEventResult);
        MatcherAssert.assertThat(collect, Matchers.hasEntry(1, "24(Senne M.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(2, "14(Kas B.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(3, "50(Thomas V.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(4, "20(Emiel D.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(5, "16(Xander V.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(6, "56(Jorbe D.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(7, "37(Thomas B.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(8, "1(Emile M.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(9, "67(Maxime T.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(10, "11(Lukas V.),17(Leon D.),25(Félix V.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(11, "22(Jens C.)"));
    }


    @Test
    public void sortTeamByEvents_MEU15_havingAQualificationScheme() {
        event = pboJeugdCupTournamentVlabad.getEvents().stream().filter(e -> "MEU15".equals(e.getName())).findAny().get();
        final SortedMap<Integer, List<Team>> sortTeamsByEventResult = event.sortTeamsByEventResult();
        final Map<Integer, String> collect = mapToString(sortTeamsByEventResult);
        MatcherAssert.assertThat(collect, Matchers.hasEntry(1, "44(Lola G.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(2, "13(Gitte B.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(3, "6(Norah E.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(4, "47(Britt M.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(5, "27(Nette L.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(6, "9(Zoë E.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(7, "26(Leen V.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(8, "29(Sarah D.)"));
    }


    @Test
    public void sortTeamByEvents_JEU13_19subscriptions() {
        event = pboJeugdCupTournamentWitWit.getEvents().stream().filter(e -> "JE U13".equals(e.getName())).findAny().get();
        final SortedMap<Integer, List<Team>> sortTeamsByEventResult = event.sortTeamsByEventResult();
        final Map<Integer, String> collect = mapToString(sortTeamsByEventResult);
        MatcherAssert.assertThat(collect, Matchers.hasEntry(1, "75(Emiel D.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(2, "19(Thijs V.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(3, "30(Senne M.),35(Thomas V.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(4, "22(Kas B.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(5, "56(Jorbe D.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(6, "43(Thomas B.),7(Emile M.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(7, "78(Leon D.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(8, "40(Lukas V.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(9, "20(Bas A.),55(Corneel W.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(10, "27(Thomas D.),37(Xander V.),59(Maxime T.),92(Lander B.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(11, "50(Iñaki M.),53(Sander V.),79(Jules M.)"));
    }

    @Test
    public void sortTeamByEvents_MEU13_onlyASingleRound() {
        event = pboJeugdCupTournamentVlabad.getEvents().stream().filter(e -> "MEU13".equals(e.getName())).findAny().get();
        final SortedMap<Integer, List<Team>> sortTeamsByEventResult = event.sortTeamsByEventResult();
        final Map<Integer, String> collect = mapToString(sortTeamsByEventResult);
        MatcherAssert.assertThat(collect, Matchers.hasEntry(1, "33(Lente V.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(2, "54(Julie P.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(3, "49(Lara V.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(4, "46(Carole M.)"));
        MatcherAssert.assertThat(collect, Matchers.hasEntry(5, "10(Josefien J.)"));
    }

    private Map<Integer, String> mapToString(final SortedMap<Integer, List<Team>> sortTeamsByEventResult) {
        final Map<Integer, String> collect = sortTeamsByEventResult.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().map(Team::toStringShort).sorted().collect(Collectors.joining(","))));

        return collect;
    }
}