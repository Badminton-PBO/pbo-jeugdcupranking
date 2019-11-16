package be.pbo.jeugdcup.ranking.infrastructure.db;

import be.pbo.jeugdcup.ranking.domain.Afvalschema;
import be.pbo.jeugdcup.ranking.domain.Draw;
import be.pbo.jeugdcup.ranking.domain.Event;
import be.pbo.jeugdcup.ranking.domain.EventType;
import be.pbo.jeugdcup.ranking.domain.Gender;
import be.pbo.jeugdcup.ranking.domain.Match;
import be.pbo.jeugdcup.ranking.domain.Player;
import be.pbo.jeugdcup.ranking.domain.Poule;
import be.pbo.jeugdcup.ranking.domain.Team;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TpRepositoryImpl implements TpRepository {

    private final Connection connection;
    private final Map<Integer, Team> teamById = new HashMap<>();
    private final Map<Integer, Event> eventById = new HashMap<>();
    private final Map<Integer, Draw> drawById = new HashMap<>();

    public TpRepositoryImpl(final Path filePath) {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

            if (!Files.exists(filePath)) {
                throw new IllegalArgumentException("File " + filePath + " is invalid!");
            }

            this.connection = DriverManager.getConnection("jdbc:ucanaccess://" + filePath.toFile(), "",
                    "d4R2GY76w2qzZ");
        } catch (final ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Player> getPlayers() {
        final List<Player> result = new ArrayList<>();
        try (final ResultSet rs = executeSql("SELECT firstName, name, memberId, gender FROM Player;")) {
            while (rs.next()) {
                final Player player = new Player();
                player.setFirstName(rs.getString("firstName"));
                player.setLastName(rs.getString("name"));
                player.setMemberId(rs.getString("memberId"));
                player.setGender(rs.getInt("gender"));
                result.add(player);
            }
        } catch (final SQLException e) {
            throw new RuntimeException("Unable to get players", e);
        }
        return result;
    }

    public List<Match> getMatches() {
        if (teamById.isEmpty()) {
            this.fillTeams();
        }
        if (drawById.isEmpty()) {
            this.getDraws();
        }

        final List<Match> result = new ArrayList<>();
        final String query = "SELECT hometeam.entry, awayteam.entry, "
                + "thematch.team1set1, thematch.team2set1, thematch.team1set2, "
                + "thematch.team2set2, thematch.team1set3, thematch.team2set3, "
                + "hometeam.walkover, awayteam.walkover, thematch.matchnr, thematch.roundnr, "
                + "thematch.draw, thematch.winner " + "FROM PlayerMatch thematch "
                + "INNER JOIN PlayerMatch AS hometeam ON thematch.van1 = hometeam.planning "
                + "INNER JOIN PlayerMatch AS awayteam ON thematch.van2 = awayteam.planning "
                + "AND thematch.draw = hometeam.draw AND thematch.draw = awayteam.draw " + "AND reversehomeaway=FALSE "
                + "AND thematch.roundnr>0 	AND plandate > #2000-01-01 00:00:00#" + "ORDER BY thematch.plandate;";
        try (final ResultSet rs = executeSql(query)) {
            while (rs.next()) {
                final Match match = Match.builder()
                        .team1(teamById.get(rs.getInt(1)))
                        .team2(teamById.get(rs.getInt(2)))
                        .set1(buildSet(rs, 3, 4))
                        .set2(buildSet(rs, 5, 6))
                        .set3(buildSet(rs, 7, 8))
                        .walkoverTeam1(rs.getBoolean(9))
                        .walkoverTeam2(rs.getBoolean(10))
                        .winner(teamById.get(rs.getInt(rs.getInt("winner"))))
                        .draw(drawById.get(rs.getInt("draw")))
                        .matchnr(rs.getInt(11))
                        .roundnr(rs.getInt(12))
                        .build();
                result.add(match);
            }
        } catch (final SQLException e) {
            throw new RuntimeException("Unable to get players", e);
        }

        return result;
    }

    private String buildSet(final ResultSet rs, final int team1Column, final int team2Column) throws SQLException {
        final int homePoints = rs.getInt(team1Column);
        final int awayPoints = rs.getInt(team2Column);
        String result = null;
        if (homePoints > 0 || awayPoints > 0) {
            result = homePoints + "-" + awayPoints;
        }
        return result;
    }

    public List<Draw> getDraws() {
        if (eventById.isEmpty()) {
            this.getEvents();
        }

        final ResultSet rs = executeSql("SELECT id, name, event, drawtype, drawsize from Draw;");
        try {
            while (rs.next()) {
                final Draw draw = toDraw(rs.getInt("drawtype"));
                draw.setId(rs.getInt("id"));
                draw.setName(rs.getString("name"));
                draw.setEvent(eventById.get(rs.getInt("event")));
                draw.setSize(rs.getInt("drawsize"));

                this.drawById.put(draw.getId(), draw);
            }
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
        return new ArrayList<>(drawById.values());
    }

    private Draw toDraw(final int d) {
        switch (d) {
            case 1:
                return new Afvalschema();
            case 2:
                return new Poule();
            default:
                throw new IllegalArgumentException("Unknown drawtype " + d);
        }
    }

    public List<Event> getEvents() {
        final ResultSet rs = executeSql("SELECT id, name, gender, eventtype from Event;");
        try {
            while (rs.next()) {
                final Event event = Event.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .gender(eventToGender(rs.getInt("gender")))
                        .eventType(getEventType(rs.getInt("eventtype")))
                        .build();
                this.eventById.put(event.getId(), event);
            }
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }

        return new ArrayList<>(eventById.values());
    }

    private EventType getEventType(final Integer e) {
        switch (e) {
            case 1:
                return EventType.SINGLE;
            case 2:
                return EventType.DOUBLE;
            case 3:
                return EventType.MIX;
            default:
                throw new IllegalArgumentException("Unknown eventtype " + e);
        }
    }

    private Gender eventToGender(final Integer g) {
        switch (g) {
            case 4:
                return Gender.MALE;
            case 5:
                return Gender.FEMALE;
            default:
                return Gender.UNKNOWN;
        }
    }


    private void fillTeams() {
        final ResultSet resultSet = executeSql("SELECT DISTINCT player1.name, player1.firstname, player1.memberid, "
                + "player2.name, player2.firstname, player2.memberid, entry.id, club1.name, club2.name "
                + "FROM Draw INNER JOIN PlayerMatch ON Draw.id = PlayerMatch.draw "
                + "INNER JOIN Entry ON PlayerMatch.entry = Entry.id "
                + "INNER JOIN Player AS player1 ON Entry.player1 = player1.id "
                + "LEFT JOIN Player AS player2 ON Entry.player2 = player2.id "
                + "LEFT JOIN Club AS club1 ON club1.id = player1.club "
                + "LEFT JOIN Club AS club2 ON club2.id = player2.club ;");
        try {
            convertTeams(resultSet);
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void convertTeams(final ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            final Team currentTeam = new Team();
            final int teamid = resultSet.getInt(7);
            currentTeam.setId(teamid);
            teamById.put(teamid, currentTeam);

            final Player player1 = Player.builder()
                    .lastName(resultSet.getString(1))
                    .firstName(resultSet.getString(2))
                    .memberId(resultSet.getString(3))
                    .clubName(resultSet.getString(8))
                    .build();

            Player player2 = null;
            final String name2 = resultSet.getString(4);
            if (name2 != null) {
                player2 = Player.builder()
                        .lastName(resultSet.getString(4))
                        .firstName(resultSet.getString(5))
                        .memberId(resultSet.getString(6))
                        .clubName(resultSet.getString(9))
                        .build();
            }

            currentTeam.setPlayer1(player1);
            currentTeam.setPlayer2(player2);
        }
    }


    protected ResultSet executeSql(final String sql) {
        try {
            final Statement statement = connection.createStatement();
            return statement.executeQuery(sql);
        } catch (final SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
