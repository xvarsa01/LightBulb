/*
 * Author:  Varsányi Adam, VUT FIT
 * Created: 27/04/2025
 */

import logic.Difficulty;
import logic.Game;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CustomTests extends TestUtils {


    @Test
    public void EasyGameSeedsAllHaveCorrectStartPosition() {
        for(int easyGamesPlayed = 0; easyGamesPlayed < 8; easyGamesPlayed++) {
            game = new Game(5, 5);
            game.SeedBoard(Difficulty.easy, easyGamesPlayed, 0, 0);
            game.init();

            List<Integer[]> lightsList = CreateArrayOfPositions();
            if (easyGamesPlayed == 5) {
                lightsList.remove(24);      // this one seed has position [5][5] empty node
            }
            Integer[][] lights = lightsList.toArray(new Integer[0][0]);

            testLight(lights);
        }
    }

    // TODO medium seeds not yet seeded
    @Test
    public void MediumGameSeedsAllHaveCorrectStartPosition() {
        for(int mediumGamesPlayed = 0; mediumGamesPlayed < 8; mediumGamesPlayed++) {
            game = new Game(6, 6);
            game.SeedBoard(Difficulty.medium, 0, mediumGamesPlayed, 0);
            game.init();

            List<Integer[]> lightsList = CreateArrayOfPositions();
            Integer[][] lights = lightsList.toArray(new Integer[0][0]);

            testLight(lights);
        }
    }

    private List<Integer[]> CreateArrayOfPositions(){
        List<Integer[]> lightsList = new ArrayList<>();

        for (int r = 1; r <= game.rows(); r++) {
            for (int c = 1; c <= game.cols(); c++) {
                // Add whatever positions you want
                lightsList.add(new Integer[]{r, c});
            }
        }
        return lightsList;
    }
}
