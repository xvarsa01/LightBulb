package logic;

import enums.Difficulty;
import enums.Side;
import seeds.EasyGameSeeds;
import seeds.HardGameSeeds;
import seeds.MediumGameSeeds;

public class Seeder {
    private final Game game;

    public Seeder(Game game) {
        this.game = game;
    }

    public void SeedBoard(Difficulty difficulty, int easyGamesPlayed, int mediumGamesPlayed, int hardGamesPlayed){
        Object[][] seed = switch (difficulty) {
            case easy -> SeedEasyBoard(easyGamesPlayed);
            case medium -> SeedMediumBoard(mediumGamesPlayed);
            case hard -> SeedHardBoard(hardGamesPlayed);
        };
        ApplySeed(seed);
    }

    private Object[][] SeedEasyBoard(int lastlyGenerated){
        if (lastlyGenerated >= 1) {
            return EasyGameSeeds.allSeeds[(lastlyGenerated % EasyGameSeeds.allSeeds.length)];
        } else {
            return EasyGameSeeds.allSeeds[0]; // default seed
        }
    }

    private Object[][] SeedMediumBoard(int lastlyGenerated){
        if (lastlyGenerated >= 1) {
            return MediumGameSeeds.allSeeds[lastlyGenerated % MediumGameSeeds.allSeeds.length];
        } else {
            return MediumGameSeeds.allSeeds[0]; // default seed
        }
    }

    private Object[][] SeedHardBoard(int lastlyGenerated){
        if (lastlyGenerated >= 1) {
            return HardGameSeeds.allSeeds[lastlyGenerated % HardGameSeeds.allSeeds.length];
        } else {
            return HardGameSeeds.allSeeds[0]; // default seed
        }
    }

    private void ApplySeed(Object[][] seed){
        for (Object[] n : seed) {
            String type = (String) n[0];
            int row = (Integer) n[1];
            int col = (Integer) n[2];
            Position p = new Position(row, col);
            Side[] sides = new Side[n.length - 3];
            for (int i = 3; i < n.length; i++) {
                sides[i - 3] = (Side) n[i];
            }
            switch (type) {
                case "L" -> game.createLinkNode(p, sides);
                case "B" -> game.createBulbNode(p, sides[0]);
                case "P" -> game.createPowerNode(p, sides);
            }
        }
    }
}
