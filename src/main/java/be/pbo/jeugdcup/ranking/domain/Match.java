package be.pbo.jeugdcup.ranking.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Match {
    public static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM HH:mm");

    private static final String UNKNOWN = "<Bye>";

    private static final Date MINDATE = new Date(946681200000L); // 1.1.2000
    // 0:00

    private static final String NOMATCH = "<Kein Spiel>";

    private final static Pattern PATTERN = Pattern.compile("(\\d+)[-](\\d+)");

    private Team team1;

    private Team team2;

    private Team winner;

    private String set1;

    private String set2;

    private String set3;

    private boolean walkoverTeam1;

    private boolean walkoverTeam2;

    private int matchnr;

    private int roundnr;

    private Draw draw;

}
