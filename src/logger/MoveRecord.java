/**
 * Authors: xvarsa01, xhavli59
 * Date: 09.05.2025
 *
 * Description: Represents a record of a move for logging or undo functionality.
 */

package logger;

import logic.Position;
import enums.NodeType;

public record MoveRecord(Position position, NodeType nodeType, boolean wasLighted, int previousRotation) {
}
