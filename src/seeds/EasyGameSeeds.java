package seeds;

import static enums.Side.*;

public class EasyGameSeeds {
    static final Object[][] seed1 = {
        {"L", 1, 1, EAST, SOUTH},
        {"B", 1, 2, WEST},
        {"L", 1, 3, EAST, SOUTH},
        {"L", 1, 4, EAST, SOUTH, WEST},
        {"B", 1, 5, WEST},

        {"L", 2, 1, NORTH, SOUTH},
        {"B", 2, 2, EAST},
        {"L", 2, 3, WEST, NORTH},
        {"L", 2, 4, NORTH, SOUTH, EAST},
        {"B", 2, 5, WEST},

        {"L", 3, 1, NORTH, SOUTH, EAST},
        {"L", 3, 2, WEST, SOUTH, EAST},
        {"L", 3, 3, WEST, SOUTH,},
        {"L", 3, 4, NORTH, SOUTH},
        {"B", 3, 5, SOUTH},

        {"P", 4, 1, NORTH, SOUTH},
        {"B", 4, 2, NORTH},
        {"L", 4, 3, NORTH, EAST},
        {"L", 4, 4, NORTH, EAST, WEST},
        {"L", 4, 5, NORTH, WEST, SOUTH},

        {"L", 5, 1, NORTH, EAST},
        {"L", 5, 2, EAST, WEST},
        {"L", 5, 3, EAST, WEST},
        {"B", 5, 4, WEST},
        {"B", 5, 5, NORTH},
    };

    static final Object[][] seed2 = {
        {"L", 1, 1, SOUTH, EAST},
        {"L", 1, 2, EAST, WEST},
        {"L", 1, 3, EAST, WEST},
        {"L", 1, 4, EAST, WEST, SOUTH},
        {"L", 1, 5, WEST, SOUTH},

        {"L", 2, 1, NORTH, EAST},
        {"L", 2, 2, EAST, WEST, SOUTH},
        {"B", 2, 3, WEST},
        {"L", 2, 4, NORTH, SOUTH},
        {"B", 2, 5, NORTH},

        {"B", 3, 1, EAST},
        {"L", 3, 2, WEST, NORTH, SOUTH},
        {"B", 3, 3, EAST},
        {"L", 3, 4, NORTH, WEST},
        {"B", 3, 5, SOUTH},

        {"P", 4, 1, SOUTH},
        {"L", 4, 2, NORTH, EAST},
        {"L", 4, 3, EAST, WEST},
        {"L", 4, 4, EAST, WEST, SOUTH},
        {"L", 4, 5, NORTH, SOUTH, WEST},

        {"L", 5, 1, NORTH, EAST},
        {"L", 5, 2, EAST, WEST},
        {"L", 5, 3, EAST, WEST},
        {"L", 5, 4, NORTH, WEST},
        {"B", 5, 5, NORTH},
    };
    static final Object[][] seed3 = {
        {"B", 1, 1, SOUTH},
        {"B", 1, 2, SOUTH},
        {"L", 1, 3, SOUTH, EAST},
        {"L", 1, 4, WEST, EAST},
        {"L", 1, 5, WEST, SOUTH},

        {"L", 2, 1, NORTH, EAST},
        {"L", 2, 2, NORTH, SOUTH, EAST, WEST},
        {"L", 2, 3, WEST, NORTH},
        {"B", 2, 4, EAST},
        {"L", 2, 5, NORTH, SOUTH, WEST},

        {"L", 3, 1, SOUTH, EAST},
        {"L", 3, 2, NORTH, WEST},
        {"L", 3, 3, SOUTH, EAST},
        {"B", 3, 4, WEST},
        {"L", 3, 5, NORTH, SOUTH},

        {"P", 4, 1, NORTH, EAST},
        {"B", 4, 2, WEST},
        {"L", 4, 3, NORTH, SOUTH},
        {"L", 4, 4, SOUTH, EAST},
        {"L", 4, 5, WEST, NORTH},

        {"B", 5, 1, EAST},
        {"L", 5, 2, EAST, WEST},
        {"L", 5, 3, EAST, WEST, NORTH},
        {"L", 5, 4, EAST, WEST, NORTH},
        {"B", 5, 5, WEST},
    };

    static final Object[][] seed4 = {
        {"L", 1, 1, SOUTH, EAST},
        {"L", 1, 2, EAST, WEST},
        {"L", 1, 3, EAST, WEST},
        {"L", 1, 4, EAST, WEST, SOUTH},
        {"B", 1, 5, WEST},

        {"B", 2, 1, NORTH},
        {"L", 2, 2, SOUTH, EAST},
        {"L", 2, 3, WEST, SOUTH},
        {"L", 2, 4, NORTH, EAST},
        {"L", 2, 5, WEST, SOUTH},

        {"L", 3, 1, SOUTH, EAST},
        {"L", 3, 2, WEST, NORTH},
        {"L", 3, 3, NORTH, SOUTH, EAST},
        {"L", 3, 4, WEST, EAST},
        {"L", 3, 5, WEST, NORTH},

        {"P", 4, 1, NORTH, SOUTH, EAST},
        {"B", 4, 2, WEST},
        {"B", 4, 3, NORTH},
        {"L", 4, 4, SOUTH, EAST},
        {"B", 4, 5, WEST},

        {"L", 5, 1, NORTH, EAST},
        {"L", 5, 2, WEST, EAST},
        {"L", 5, 3, WEST, EAST},
        {"L", 5, 4, WEST, EAST, NORTH},
        {"B", 5, 5, WEST},
    };
    static final Object[][] seed5 = {
        {"P", 1, 1, SOUTH, EAST},
        {"L", 1, 2, WEST, EAST},
        {"L", 1, 3, WEST, EAST},
        {"L", 1, 4, WEST, EAST, SOUTH},
        {"L", 1, 5, WEST, SOUTH},

        {"B", 2, 1, NORTH},
        {"B", 2, 2, SOUTH},
        {"B", 2, 3, SOUTH},
        {"B", 2, 4, NORTH},
        {"L", 2, 5, SOUTH, NORTH},

        {"L", 3, 1, SOUTH, EAST},
        {"L", 3, 2, WEST, EAST, NORTH},
        {"L", 3, 3, WEST, EAST, NORTH},
        {"L", 3, 4, EAST, WEST},
        {"L", 3, 5, WEST, NORTH},

        {"L", 4, 1, NORTH, EAST},
        {"L", 4, 2, EAST, WEST, SOUTH},
        {"B", 4, 3, WEST},
        {"L", 4, 4, SOUTH, EAST},
        {"L", 4, 5, WEST, SOUTH},

        {"B", 5, 1, EAST},
        {"L", 5, 2, WEST, EAST, NORTH},
        {"L", 5, 3, WEST, EAST},
        {"L", 5, 4, WEST, NORTH},
        {"B", 5, 5, NORTH},
    };

    static final Object[][] seed6 = {
        {"L", 1, 1, SOUTH, EAST},
        {"B", 1, 2, WEST},
        {"L", 1, 3, SOUTH, EAST},
        {"L", 1, 4, SOUTH, EAST, WEST},
        {"L", 1, 5, SOUTH, WEST},

        {"L", 2, 1, NORTH, SOUTH, EAST},
        {"L", 2, 2, WEST, EAST},
        {"L", 2, 3, WEST, NORTH},
        {"L", 2, 4, NORTH, SOUTH},
        {"L", 2, 5, NORTH, SOUTH},

        {"L", 3, 1, NORTH, EAST},
        {"L", 3, 2, WEST, EAST},
        {"L", 3, 3, WEST, SOUTH},
        {"B", 3, 4, NORTH},
        {"P", 3, 5, NORTH},

        {"L", 4, 1, SOUTH, EAST},
        {"L", 4, 2, WEST, SOUTH},
        {"L", 4, 3, NORTH, SOUTH, EAST},
        {"L", 4, 4, WEST, EAST},
        {"B", 4, 5, WEST},

        {"B", 5, 1, NORTH},
        {"L", 5, 2, NORTH, EAST},
        {"L", 5, 3, WEST, EAST, NORTH},
        {"B", 5, 4, WEST},
//            EMPTY
    };

    static final Object[][] seed7 = {
        {"B", 1, 1, EAST},
        {"L", 1, 2, WEST, SOUTH},
        {"L", 1, 3, EAST, SOUTH},
        {"L", 1, 4, EAST, WEST, SOUTH},
        {"L", 1, 5, WEST, SOUTH},

        {"B", 2, 1, SOUTH},
        {"L", 2, 2, NORTH, SOUTH, EAST},
        {"L", 2, 3, WEST, NORTH},
        {"L", 2, 4, NORTH, SOUTH},
        {"L", 2, 5, NORTH, SOUTH},

        {"L", 3, 1, NORTH, SOUTH, EAST},
        {"L", 3, 2, WEST, NORTH},
        {"B", 3, 3, SOUTH},
        {"P", 3, 4, NORTH},
        {"B", 3, 5, NORTH},

        {"L", 4, 1, NORTH, EAST},
        {"L", 4, 2, WEST, EAST},
        {"L", 4, 3, WEST, EAST, NORTH},
        {"L", 4, 4, WEST, EAST, SOUTH},
        {"B", 4, 5, WEST},

        {"B", 5, 1, EAST},
        {"L", 5, 2, EAST, WEST},
        {"L", 5, 3, EAST, WEST},
        {"L", 5, 4, NORTH, EAST, WEST},
        {"B", 5, 5, WEST},
    };

    static final Object[][] seed8 = {
        {"L", 1, 1, SOUTH, EAST},
        {"L", 1, 2, SOUTH, WEST, EAST},
        {"L", 1, 3, SOUTH, WEST},
        {"L", 1, 4, SOUTH, EAST},
        {"L", 1, 5, SOUTH, WEST},

        {"L", 2, 1, NORTH, SOUTH},
        {"B", 2, 2, NORTH},
        {"L", 2, 3, NORTH, SOUTH},
        {"L", 2, 4, NORTH, SOUTH},
        {"B", 2, 5, NORTH},

        {"L", 3, 1, NORTH, SOUTH},
        {"B", 3, 2, SOUTH},
        {"L", 3, 3, NORTH, SOUTH},
        {"L", 3, 4, NORTH, SOUTH, EAST},
        {"B", 3, 5, WEST},

        {"L", 4, 1, NORTH, EAST},
        {"L", 4, 2, SOUTH, WEST, NORTH},
        {"B", 4, 3, NORTH},
        {"L", 4, 4, SOUTH, NORTH},
        {"P", 4, 5, SOUTH},

        {"B", 5, 1, EAST},
        {"L", 5, 2, WEST, EAST, NORTH},
        {"L", 5, 3, WEST, EAST},
        {"L", 5, 4, WEST, EAST, NORTH},
        {"L", 5, 5, WEST, NORTH},
    };

    public static Object[][][] allSeeds = { seed1, seed2, seed3, seed4, seed5, seed6, seed7, seed8 };
}
