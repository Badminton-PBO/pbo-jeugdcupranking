package be.pbo.jeugdcup.ranking;

import be.pbo.jeugdcup.ranking.domain.Player;
import be.pbo.jeugdcup.ranking.services.RankingGenerator;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Startup {

    public static void main(final String[] args) throws URISyntaxException {
        //final String tpFile = "/PBO-Jeugdcuptour-Lokerse-BC-2019.tp";
        final String tpFile = "/../../../PBO_JEUGDCUPTOUR_BC_DENDERLEEUW_2020.TP";

        final Path path = Paths.get("./PBO_JEUGDCUPTOUR_BC_DENDERLEEUW_2020.TP");
        final RankingGenerator rankingGenerator = new RankingGenerator(path, false);
        final Map<Player, Integer> results = rankingGenerator.generate();

        results.forEach((player, point) -> {
            System.out.println(player.getId() + ";" + player.getFirstName() + ";" + player.getLastName() + ";" + player.getAgeCategory() + ";" + point);
        });
    }
}
