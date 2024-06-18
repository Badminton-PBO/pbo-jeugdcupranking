package be.pbo.jeugdcup.ranking.domain;

import lombok.Builder;
import lombok.Data;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@Builder(toBuilder = true, builderClassName = "MatchInternalBuilder", builderMethodName = "internalBuilder")
public class Match {
    public static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd.MM HH:mm");

    private static final String UNKNOWN = "<Bye>";

    private static final Date MINDATE = new Date(946681200000L); // 1.1.2000
    // 0:00

    private static final String NOMATCH = "<Kein Spiel>";

    public final static Pattern SET_PATTERN = Pattern.compile("(\\d+)[-](\\d+)");

    private Integer id;

    private Team team1;

    private Team team2;

    private Team winner;

    private String set1;

    private String set2;

    private String set3;

    private boolean isWalkOverMatch;

    private boolean isLostByGivingUp;

    private int matchnr;

    private int roundnr;

    private Draw draw;

    private Optional<Team> teamThatGaveUp;

    private boolean ignoreMatchInThisDraw;

    private Date planDate;


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends MatchInternalBuilder {

        Builder() {
            super();
        }

        @Override
        public Match build() {
            final Match match = super.build();
            if (match.getTeam1() != null ) {
                match.getTeam1().assignMatch(match);
            }
            if (match.getTeam2() != null) {
                match.getTeam2().assignMatch(match);
            }
            return match;
        }
    }

    private Map<Team, Long> getNumberOfSetsWonPerTeam() {
        return Stream.of(set1, set2, set3)
                .filter(set -> set != null && SET_PATTERN.matcher(set).matches())
                .map(set -> {
                    final Matcher matcher = SET_PATTERN.matcher(set);
                    matcher.matches();
                    int team1points = Integer.parseInt(matcher.group(1));
                    int team2points = Integer.parseInt(matcher.group(2));
                    if (team1points == 0 && team2points == 0) { // Walkover game
                        return null;
                    }
                    return team1points > team2points ? team1 : team2;
                })
                .filter(Objects::nonNull)
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
                .filter(set -> set != null && SET_PATTERN.matcher(set).matches())
                .map(set -> {
                    final Matcher matcher = SET_PATTERN.matcher(set);
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

    public Optional<Team> getLoser() {
        return team1 != null && team1.equals(getWinner()) ? Optional.ofNullable(team2) : Optional.ofNullable(team1);
    }

    public boolean isPlayed() {
        return set1 != null;
    }

}
