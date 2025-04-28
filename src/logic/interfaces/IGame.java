package logic.interfaces;

import common.Observable;
import logic.Difficulty;
import logic.Position;
import logic.Side;

public interface IGame extends Observable
{
    void SeedBoard(Difficulty difficulty, int easyGamesPlayed, int mediumGamesPlayed, int hardGamesPlayed);
    void init();
    void randomlyTurnSomeNodes(float affectedNodesPercentage, int ms, int rotatedNodesAtSameTime);
    boolean GameFinished();
    int cols();
    int rows();

    void createBulbNode(Position p, Side side);
    void createPowerNode(Position p, Side... sides);
    void createLinkNode(Position p, Side... sides);

}
