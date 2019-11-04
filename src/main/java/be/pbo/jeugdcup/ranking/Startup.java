package be.pbo.jeugdcup.ranking;

public class Startup {

    public static void main(final String[] args) {
        final RankingGenerator rankingGenerator = new RankingGenerator("C:\\checkouts\\pbo-jeugdcupranking\\src\\main\\resources\\PBO-Jeugdcuptour-Lokerse-BC-2019.tp");
        rankingGenerator.generate();
    }
}
