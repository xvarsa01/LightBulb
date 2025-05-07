package logic;

import javafx.application.Platform;
import logic.interfaces.IRandomizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Randomizer implements IRandomizer
{
    private final Game game;

    public Randomizer(Game game) {
        this.game = game;
    }

    /**
     * Randomly rotates one node after other, until no Bulb is connected to power
     * @param affectedNodesPercentage probability, that node will be affected
     * @param ms number of ms that thread will sleep after node is rotated
     */
    public void randomlyTurnSomeNodes(float affectedNodesPercentage, int ms, int rotatedNodesAtSameTime){
        int nodesAlreadyRotated = 0;
        List<Position> positions = new ArrayList<>();

        // Collect all positions
        for (int i = 1; i <= game.rows(); i++) {
            for (int j = 1; j <= game.cols(); j++) {
                positions.add(new Position(i, j));
            }
        }

        // Shuffle to randomize order
        Collections.shuffle(positions, new Random());

        Random rand = new Random();
        for (Position p : positions) {
            if (rand.nextFloat() <= affectedNodesPercentage){

                // rotate node 1-3 times
                int numberOfTurns = rand.nextInt(3)+1;

                for (int k = 0; k < numberOfTurns; k++){
                    Platform.runLater(() -> game.node(p).turn(false));
                }
                nodesAlreadyRotated++;

                if (nodesAlreadyRotated % rotatedNodesAtSameTime == 0) {
                    System.out.println("small sleep started");
                    sleep(ms);
                }
                System.out.println("sleep ended");
            }
        }
    }
    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
