package be.pbo.jeugdcup.ranking.infrastructure.db;

import be.pbo.jeugdcup.ranking.domain.AgeCategory;
import be.pbo.jeugdcup.ranking.domain.Draw;
import be.pbo.jeugdcup.ranking.domain.EliminationScheme;
import be.pbo.jeugdcup.ranking.domain.Event;
import be.pbo.jeugdcup.ranking.domain.EventType;
import be.pbo.jeugdcup.ranking.domain.Gender;
import be.pbo.jeugdcup.ranking.domain.Match;
import be.pbo.jeugdcup.ranking.domain.Player;
import be.pbo.jeugdcup.ranking.domain.QualificationScheme;
import be.pbo.jeugdcup.ranking.domain.Round;
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
    private final Map<Integer, Player> playerById = new HashMap<>();
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
        try (final ResultSet rs = executeSql("SELECT theplayer.id, theplayer.firstName, theplayer.name, theplayer.memberId, theplayer.gender, theclub.name as clubname FROM Player as theplayer " +
                "JOIN Club as theclub on theclub.id = theplayer.club;")) {
            while (rs.next()) {
                final Player player = new Player();
                player.setId(rs.getInt("id"));
                player.setFirstName(rs.getString("firstName"));
                player.setLastName(rs.getString("name"));
                player.setMemberId(rs.getString("memberId"));
                player.setGender(playerToGender(rs.getInt("gender")));
                player.setClubName(rs.getString("clubname"));

                this.playerById.put(player.getId(), player);
            }
        } catch (final SQLException e) {
            throw new RuntimeException("Unable to get players", e);
        }
        return new ArrayList<>(playerById.values());
    }

    public List<Match> getMatches() {
        if (playerById.isEmpty()) {
            this.getPlayers();
        }
        if (drawById.isEmpty()) {
            this.getDraws();
        }
        if (teamById.isEmpty()) {
            this.fillTeams();
        }


        final List<Match> result = new ArrayList<>();
        final String query = "SELECT hometeam.entry, awayteam.entry, "
                + "thematch.team1set1, thematch.team2set1, thematch.team1set2, "
                + "thematch.team2set2, thematch.team1set3, thematch.team2set3, "
                + "hometeam.walkover, awayteam.walkover, thematch.matchnr, thematch.roundnr, "
                + "thematch.draw, thematch.winner, thematch.id " + "FROM PlayerMatch thematch "
                + "INNER JOIN PlayerMatch AS hometeam ON thematch.van1 = hometeam.planning "
                + "INNER JOIN PlayerMatch AS awayteam ON thematch.van2 = awayteam.planning "
                + "AND thematch.draw = hometeam.draw AND thematch.draw = awayteam.draw " + "AND reversehomeaway=FALSE "
                + "AND thematch.roundnr>=0 AND thematch.winner>0 " + "ORDER BY thematch.plandate;";
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
                        .id(rs.getInt(15))
                        .build();
                result.add(match);
            }
        } catch (final SQLException e) {
            throw new RuntimeException("Unable to get matches", e);
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
                return new EliminationScheme();
            case 2:
                return new Round();
            case 6:
                return new QualificationScheme();
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

    private Gender playerToGender(final Integer g) {
        switch (g) {
            case 1:
                return Gender.MALE;
            case 2:
                return Gender.FEMALE;
            default:
                return Gender.UNKNOWN;
        }
    }


    private void fillTeams() {
        final ResultSet resultSet = executeSql("SELECT DISTINCT Entry.player1 player1_id, Entry.player2 player2_id, entry.id team_id, Draw.id draw_id "
                + "FROM Draw INNER JOIN PlayerMatch ON Draw.id = PlayerMatch.draw "
                + "INNER JOIN Entry ON PlayerMatch.entry = Entry.id;");
        try {
            convertTeams(resultSet);
        } catch (final SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void convertTeams(final ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            final Team currentTeam = new Team();
            final int teamid = resultSet.getInt("team_id");
            currentTeam.setId(teamid);
            teamById.put(teamid, currentTeam);

            final Player player1 = playerById.get(resultSet.getInt("player1_id"));
            final Player player2 = playerById.get(resultSet.getInt("player2_id"));

            final AgeCategory ageCategory = drawById.get(resultSet.getInt("draw_id")).getEvent().getAgeCategory();
            if (ageCategory != null && ageCategory != AgeCategory.UNKNOWN) {
                player1.setAgeCategory(ageCategory);
                if (player2 != null) {
                    player2.setAgeCategory(ageCategory);
                }
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
