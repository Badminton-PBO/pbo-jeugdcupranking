package be.pbo.jeugdcup.ranking;

import java.net.URISyntaxException;
import java.nio.file.Paths;

public class Startup {

    public static void main(final String[] args) throws URISyntaxException {
        final String tpFile = "/PBO-Jeugdcuptour-Lokerse-BC-2019.tp";

        final RankingGenerator rankingGenerator = new RankingGenerator(Paths.get(Startup.class.getResource(tpFile).toURI()));
        rankingGenerator.generate();
    }
}
