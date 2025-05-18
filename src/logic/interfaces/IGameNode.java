/**
 * Authors: xvarsa01, xhavli59
 * Date: 09.05.2025
 *
 * Description: Interface for node behavior in the game.
 */

package logic.interfaces;

import common.Observable;

public interface IGameNode extends Observable {
    void turn(boolean userRotated);
    boolean isLighted();

    boolean north();
    boolean east();
    boolean south();
    boolean west();

    boolean isLink();
    boolean isBulb();
    boolean isPower();
}
