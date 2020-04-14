package be.pbo.jeugdcup.ranking.domain;

import lombok.Data;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Data
public class PBOJeugdCupTournament {

    private final DateTimeFormatter monthDayDateTimeFormatter = DateTimeFormatter.ofPattern("MMdd");

    private final List<Round> rounds = new ArrayList<>();
    private final List<EliminationScheme> eliminationSchemes = new ArrayList<>();
    private final List<QualificationScheme> qualificationSchemes = new ArrayList<>();

    private final List<Player> players;
    private final List<Event> events;
    private final boolean isAlwaysUsingDoubleSchemes;
    private final AtomicInteger memberIdCounter = new AtomicInteger(1);
    private final LocalDate tournamentDate;

    public PBOJeugdCupTournament(final List<Player> players, final List<Event> events,
                                 final List<Match> matches,
                                 final boolean isAlwaysUsingDoubleSchemes) {
        this(players, events, matches, isAlwaysUsingDoubleSchemes, null);
    }

    public PBOJeugdCupTournament(final List<Player> players, final List<Event> events,
                                 final List<Match> matches,
                                 final boolean isAlwaysUsingDoubleSchemes,
                                 final LocalDate tournamentDate) {
        this.players = players;
        this.events = events;
        this.isAlwaysUsingDoubleSchemes = isAlwaysUsingDoubleSchemes;
        this.tournamentDate = tournamentDate;


        matches.stream()
                .filter(match -> match.getDraw().getClass().equals(Round.class))
                .collect(Collectors.groupingBy(match -> match.getDraw().getId()))
                .forEach((drawId, matches1) -> {
                    final Round draw = (Round) matches1.get(0).getDraw();
                    draw.setMatches(matches1);
                    rounds.add(draw);
                });
        rounds.forEach(Round::handleMatchesOfTeamsThatGaveUp);

        matches.stream()
                .filter(match -> match.getDraw().getClass().equals(EliminationScheme.class))
                .collect(Collectors.groupingBy(match -> match.getDraw().getId()))
                .forEach((drawId, matches1) -> {
                    final EliminationScheme draw = (EliminationScheme) matches1.get(0).getDraw();
                    draw.setMatches(matches1);
                    eliminationSchemes.add(draw);
                });

        matches.stream()
                .filter(match -> match.getDraw().getClass().equals(QualificationScheme.class))
                .collect(Collectors.groupingBy(match -> match.getDraw().getId()))
                .forEach((drawId, matches1) -> {
                    final QualificationScheme draw = (QualificationScheme) matches1.get(0).getDraw();
                    draw.setMatches(matches1);
                    qualificationSchemes.add(draw);

                    // QualificationSchemes are not part of a normal PBO Jeugdcup tour but can be converted in a List of EliminationSchemes
                    eliminationSchemes.addAll(draw.convertToEliminationSchemes());
                });

        events.forEach(e -> {
            e.setRounds(this.getRounds(e));
            e.setEliminationSchemes(this.getEliminationSchemes(e));
        });

        players.stream()
                .filter(p -> p.getMemberId() == null || p.getMemberId().isEmpty())
                .forEach(p -> p.setMemberId(generateCustomerMemberId(tournamentDate)));
    }

    private String generateCustomerMemberId(final LocalDate tournamentDate) {
        final String suffix = "" + memberIdCounter.getAndIncrement();
        final StringBuilder sb = new StringBuilder("99");
        sb.append(tournamentDate != null ? tournamentDate.format(monthDayDateTimeFormatter) : "0000");
        sb.append("00".substring(suffix.length()));
        sb.append(suffix);


        return sb.toString();
    }

    public List<Round> getRounds(final Event event) {
        return getRounds().stream()
                .filter(r -> event.getId().equals(r.getEvent().getId()))
                .collect(Collectors.toList());
    }

    public List<EliminationScheme> getEliminationSchemes(final Event event) {
        return getEliminationSchemes().stream()
                .filter(e -> event.getId().equals(e.getEvent().getId()))
                .collect(Collectors.toList());
    }

    public List<QualificationScheme> getQualificationSchemes(final Event event) {
        return getQualificationSchemes().stream()
                .filter(e -> event.getId().equals(e.getEvent().getId()))
                .collect(Collectors.toList());

    }

}
