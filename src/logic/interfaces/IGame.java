/**
 * Authors: xvarsa01, xhavli59
 * Date: 09.05.2025
 *
 * Description: Interface for core game operations.
 */

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
