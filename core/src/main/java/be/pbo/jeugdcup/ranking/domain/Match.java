package be.pbo.jeugdcup.ranking.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private Integer id;

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

    public Team getWinnerBasedOnSetResults() {
        final Map<Team, Long> numberOfSetsWonPerTeam = getNumberOfSetsWonPerTeam();
        final Integer setsWonByTeam1 = numberOfSetsWonPerTeam.getOrDefault(team1, 0L).intValue();
        final Integer setsWonByTeam2 = numberOfSetsWonPerTeam.getOrDefault(team2, 0L).intValue();

        if (setsWonByTeam1 > setsWonByTeam2) {
            return team1;
        } else if (setsWonByTeam1 < setsWonByTeam2) {
            return team2;
        } else {
            throw new IllegalStateException("Unable to detect a winner based on sets for match " + this);
        }
    }

    private Map<Team, Long> getNumberOfSetsWonPerTeam() {
        return Stream.of(set1, set2, set3)
                .filter(set -> set != null && PATTERN.matcher(set).matches())
                .map(set -> {
                    final Matcher matcher = PATTERN.matcher(set);
                    matcher.matches();
                    int team1points = Integer.parseInt(matcher.group(1));
                    int team2points = Integer.parseInt(matcher.group(2));
                    return team1points > team2points ? team1 : team2;
                })
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }


    public boolean isPlayedByTeams(final Team t1, final Team t2) {
        return (t1.equals(this.getTeam1()) && t2.equals(this.getTeam2())) ||
                (t2.equals(this.getTeam1()) && t1.equals(this.getTeam2()));
    }

    public boolean isPlayedWithTeam(final Team t) {
        return t.equals(this.getTeam1()) || t.equals(this.getTeam2());
    }

    public int gameSaldo(final Team t) {
        if (!isPlayedWithTeam(t)) {
            throw new IllegalArgumentException(String.format("Match %s is not played by Team %s", this, t.toStringShort()));
        }

        final Map<Team, Long> numberOfSetsWonPerTeam = getNumberOfSetsWonPerTeam();
        final Integer setsWonByTeam1 = numberOfSetsWonPerTeam.getOrDefault(team1, 0L).intValue();
        final Integer setsWonByTeam2 = numberOfSetsWonPerTeam.getOrDefault(team2, 0L).intValue();

        if (t.equals(team1)) {
            return setsWonByTeam1 - setsWonByTeam2;
        } else {
            return setsWonByTeam2 - setsWonByTeam1;
        }
    }

    public Integer pointsSaldo(final Team t) {
        if (!isPlayedWithTeam(t)) {
            throw new IllegalArgumentException(String.format("Match %s is not played by Team %s", this, t.toStringShort()));
        }

        return Stream.of(set1, set2, set3)
                .filter(set -> set != null && PATTERN.matcher(set).matches())
                .map(set -> {
                    final Matcher matcher = PATTERN.matcher(set);
                    matcher.matches();
                    int team1points = Integer.parseInt(matcher.group(1));
                    int team2points = Integer.parseInt(matcher.group(2));
                    if (t.equals(this.getTeam1())) {
                        return team1points - team2points;
                    } else {
                        return team2points - team1points;
                    }
                }).mapToInt(Integer::intValue).sum();
    }
}
