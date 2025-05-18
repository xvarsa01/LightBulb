/**
 * Authors: xvarsa01, xhavli59
 * Date: 09.05.2025
 *
 * Description: Interface for randomization logic in the game.
 */

package logic.interfaces;

public interface IRandomizer {
    void randomlyTurnSomeNodes(float affectedNodesPercentage, int ms, int rotatedNodesAtSameTime);
}
