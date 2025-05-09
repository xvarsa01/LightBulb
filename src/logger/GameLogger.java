package logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class GameLogger {
    private static PrintWriter writer;
    private static String logFilePath;

    public static void startNewSession() {
        try {
            File logDir = new File("data/logs");
            if (!logDir.exists()) {
                logDir.mkdirs();
            }

            String timestamp = LocalDateTime.now().toString().replace(":", "-");
            logFilePath = "data/logs/" + timestamp + ".txt";

            writer = new PrintWriter(new FileWriter(logFilePath, false));
            writer.println("----- New Game Session: " + LocalDateTime.now() + " -----");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void log(String message) {
        if (writer != null) {
            writer.println(message);
            writer.flush();
        }
        System.out.println(message);
    }

    public static void close() {
        if (writer != null) {
            writer.close();
        }
    }

    public static String getLogFilePath() {
        return logFilePath;
    }
}
