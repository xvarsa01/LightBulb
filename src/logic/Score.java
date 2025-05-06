package logic;

import enums.Difficulty;

import java.io.*;
import java.util.EnumMap;
import java.util.Properties;

public class Score {
    private static final String FILE_NAME = "data/scores.txt";
    private static final EnumMap<Difficulty, String> keys = new EnumMap<>(Difficulty.class);

    static {
        keys.put(Difficulty.easy, "easyGamesPlayed");
        keys.put(Difficulty.medium, "mediumGamesPlayed");
        keys.put(Difficulty.hard, "hardGamesPlayed");
    }

    private static final Properties properties = new Properties();

    static {
        load();
    }

    private static void load() {
        try (FileInputStream fis = new FileInputStream(FILE_NAME)) {
            properties.load(fis);
        } catch (IOException e) {
            // If file doesn't exist, initialize with 0
            for (Difficulty diff : Difficulty.values()) {
                properties.setProperty(keys.get(diff), "0");
            }
        }
    }

    private static void save() {
        try (FileOutputStream fos = new FileOutputStream(FILE_NAME)) {
            properties.store(fos, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int get(Difficulty difficulty) {
        return Integer.parseInt(properties.getProperty(keys.get(difficulty), "0"));
    }

    public static void increment(Difficulty difficulty) {
        int current = get(difficulty);
        properties.setProperty(keys.get(difficulty), String.valueOf(current + 1));
        save();
    }

    // Shortcut methods like Score.easy()
    public static int easy() {
        return get(Difficulty.easy);
    }

    public static int medium() {
        return get(Difficulty.medium);
    }

    public static int hard() {
        return get(Difficulty.hard);
    }

    public static void incrementEasy() {
        increment(Difficulty.easy);
    }

    public static void incrementMedium() {
        increment(Difficulty.medium);
    }

    public static void incrementHard() {
        increment(Difficulty.hard);
    }

    public static void reset() {
        for (Difficulty diff : Difficulty.values()) {
            properties.setProperty(keys.get(diff), "0");
        }
        save();
    }
}
