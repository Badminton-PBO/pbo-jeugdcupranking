package be.pbo.jeugdcup.ranking.services;

import be.pbo.jeugdcup.ranking.domain.Afvalschema;
import be.pbo.jeugdcup.ranking.domain.Match;
import java.util.ArrayList;
import java.util.List;

public class AfvalschemaService {

    private final List<Afvalschema> afvalschemas = new ArrayList<>();

    public AfvalschemaService(final List<Match> matches) {

    }

    public List<Afvalschema> getAfvalschemas() {
        return afvalschemas;
    }
}
