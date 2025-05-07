package logic.interfaces;

import common.Observable;
import logic.Position;
import enums.Side;

public interface IGame extends Observable
{
    void init();
    boolean GameFinished();
    int cols();
    int rows();

    void createBulbNode(Position p, Side side);
    void createPowerNode(Position p, Side... sides);
    void createLinkNode(Position p, Side... sides);

}
