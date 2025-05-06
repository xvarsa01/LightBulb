package logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class GameLogger {

    private static PrintWriter writer;

    static {
        try {
            writer = new PrintWriter(new FileWriter("src/logger/game_log.txt", false));
            writer.println("----- New Game Session: " + LocalDateTime.now() + " -----");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void log(String message) {
        System.out.println(message);
        writer.println(message);
        writer.flush();
    }

    public static void close() {
        writer.close();
    }
}
