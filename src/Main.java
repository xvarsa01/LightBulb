import logic.Difficulty;
import logic.Game;

public class Main {

    private static int easyGamesPlayed = 0;
    private static int mediumGamesPlayed = 0;
    private static int hardGamesPlayed = 0;
    private static Difficulty selectedDifficulty = Difficulty.easy;

    private static int affectedNodesPercentage = 100;
    private static int rotatedNodesAtTheSameTime = 1;


    public static void main(String... args) {



        int rows = switch (selectedDifficulty) {
            case easy -> {
                easyGamesPlayed++;
                affectedNodesPercentage = 50;
                rotatedNodesAtTheSameTime = 1;
                yield 5;
            }
            case medium -> {
                mediumGamesPlayed++;
                affectedNodesPercentage = 75;
                rotatedNodesAtTheSameTime = 2;
                yield 6;
            }
            case hard -> {
                hardGamesPlayed++;
                affectedNodesPercentage = 100;
                rotatedNodesAtTheSameTime = 3;
                yield 7;
            }
        };

        Game game = new Game(rows, rows);
        game.SeedBoard(selectedDifficulty, easyGamesPlayed, mediumGamesPlayed, hardGamesPlayed);
        game.init();
        game.randomlyTurnSomeNodes(affectedNodesPercentage, 500, rotatedNodesAtTheSameTime);

//        start UI
//        EnvPresenter presenter = new EnvPresenter(game);
//        presenter.open();
    }
}
