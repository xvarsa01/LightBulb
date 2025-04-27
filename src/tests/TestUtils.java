package tests;

import logic.Game;
import logic.GameNode;
import logic.Position;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUtils {

    /**
     * Hra pro testování.
     */

    protected Game game;

    /**
     * Pomocná testovací metoda - ověří svítivost políček.
     * Ověří, zda zadaná políčka svítí a zda všechna ostatní políčka nesvítí.
     * @param lights Pole souřadnic [row,col] políček, která mají svítit.
     */
    public void testLight(Integer[][] lights) {
        List<Position> allpos = new ArrayList<>();
        for (int r = 1; r <= game.rows(); r++) {
            for (int c = 1; c <= game.cols(); c++) {
                allpos.add(new Position(r, c));
            }
        }

        for (Integer[] coord : lights) {
            int row = coord[0];
            int col = coord[1];
            Position pos = new Position(row, col);
            allpos.remove(pos);
            GameNode node = game.node(pos);
            assertTrue(node.light(), "Políčko " + pos + " má být pod proudem.");
        }

        for (Position pos : allpos) {
            GameNode node = game.node(pos);
            assertFalse(node.light(), "Políčko " + pos + " nemá být pod proudem.");
        }
    }

}
